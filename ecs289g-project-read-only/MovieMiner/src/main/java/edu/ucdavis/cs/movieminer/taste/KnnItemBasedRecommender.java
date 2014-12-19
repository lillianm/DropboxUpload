/**
 * 
 */
package edu.ucdavis.cs.movieminer.taste;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.planetj.taste.common.TasteException;
import com.planetj.taste.impl.common.Pair;
import com.planetj.taste.impl.recommender.GenericItemBasedRecommender;
import com.planetj.taste.model.DataModel;
import com.planetj.taste.model.Item;
import com.planetj.taste.model.Preference;
import com.planetj.taste.model.User;
import com.planetj.taste.recommender.ItemBasedRecommender;
import com.planetj.taste.recommender.RecommendedItem;
import com.planetj.taste.recommender.Rescorer;

/**
 * Decorates {@link GenericItemBasedRecommender} with limits on the number of
 * similar items that it retrieves when doing the preference estimation in 
 * {@link #estimatePreference(Object, Object)}.
 * 
 * @author pfishero
 * @version $Id$
 */
public class KnnItemBasedRecommender implements ItemBasedRecommender {
	public static final Logger logger = Logger.getLogger(KnnItemBasedRecommender.class);
	
	private final DataModel model;
	
	/**
	 * Contains Object[20] of SimilarityScores.
	 * ItemID=1 will have its 20 most similar neighbors in slot 1 in this array.
	 * Slot 0 is unused so that an Item's index is equal to its Id, rather than
	 * its Id-1.
	 */
	private Object[] correlations = new Object[17771];
	
	public KnnItemBasedRecommender(DataModel model, Resource corrSerialized) {
		this.model = model;
		try {
			loadCorrelations(corrSerialized);
		} catch (Exception e) {
			logger.error("error deserializing the correlations object - "+e);
			throw new RuntimeException(e);
		}
	}
	
	private final void loadCorrelations(Resource corrSerialized) throws Exception {
		ObjectInputStream ois = new ObjectInputStream(corrSerialized.getInputStream());
		correlations = (Object[])ois.readObject();
		ois.close();
		logger.info("deserialized "+correlations.length+" items");
	}
	
	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.ItemBasedRecommender#mostSimilarItems(java.util.List, int, com.planetj.taste.recommender.Rescorer)
	 */
	@Override
	public List<RecommendedItem> mostSimilarItems(List<Object> arg0, int arg1,
			Rescorer<Pair<Item, Item>> arg2) throws TasteException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.ItemBasedRecommender#mostSimilarItems(java.util.List, int)
	 */
	@Override
	public List<RecommendedItem> mostSimilarItems(List<Object> arg0, int arg1)
			throws TasteException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.ItemBasedRecommender#mostSimilarItems(java.lang.Object, int, com.planetj.taste.recommender.Rescorer)
	 */
	@Override
	public List<RecommendedItem> mostSimilarItems(Object arg0, int arg1,
			Rescorer<Pair<Item, Item>> arg2) throws TasteException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.ItemBasedRecommender#mostSimilarItems(java.lang.Object, int)
	 */
	@Override
	public List<RecommendedItem> mostSimilarItems(Object arg0, int arg1)
			throws TasteException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.ItemBasedRecommender#recommendedBecause(java.lang.Object, java.lang.Object, int)
	 */
	@Override
	public List<RecommendedItem> recommendedBecause(Object arg0, Object arg1,
			int arg2) throws TasteException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#estimatePreference(java.lang.Object, java.lang.Object)
	 */
	@Override
	public double estimatePreference(final Object userID, final Object itemID) throws TasteException {
		final DataModel model = getDataModel();
		final User theUser = model.getUser(userID);
		final Preference actualPref = theUser.getPreferenceFor(itemID);
		if (actualPref != null) {
			return actualPref.getValue();
		}
		final Item item = model.getItem(itemID);
		return doEstimatePreference(theUser, item);
	}
	
	private double doEstimatePreference(final User theUser, final Item item) throws TasteException {
		if (item == null || item.getID() == null || theUser == null) {
			logger.error("item or user was null - skipping");
			return Double.NaN;
		} else if (correlations[Integer.parseInt(item.getID().toString())] == null) {
			logger.error("no correlation found for "+item+" - skipping");
			return Double.NaN;
		}
		double preference = 0.0;
		double totalCorrelation = 0.0;
		
		// item-based KNN:
		// 1. Find the K nearest neighbors to item
		// 2. Determine which of those K were also rated by theUser
		// 3. Weighted interpolation between those in-common neighbors items' ratings	
		Set<SimilarityScore> itemNeighbors = Sets.newHashSet();
		
		List<SimilarityScore> simScores = new ArrayList<SimilarityScore>(20);
		for (Object obj : (Object[])correlations[Integer.parseInt(item.getID().toString())]) {
			if (obj != null
					&& obj instanceof SimilarityScore) {
				simScores.add((SimilarityScore)obj);
			} else {
//				logger.error("Object[] element was null or " +
//						"was not a simscore:"+obj);
			}
		}
		// retain only the items that have also been rated by the user 
		Iterables.addAll(itemNeighbors,
					Iterables.filter(
						simScores, 
					new Predicate<SimilarityScore>() {
						@Override
						public boolean apply(SimilarityScore itemIn) {
							boolean keep = true;
							
							Preference pref = 
								theUser.getPreferenceFor(itemIn.getItemID());
							if (pref == null) {
								keep = false;
							}						
							
							return keep;
						}
					}));
		
		for (final SimilarityScore scoredItem : itemNeighbors) {
			Item similarItem = this.getDataModel().getItem(scoredItem.getItemID());
			Preference pref = theUser.getPreferenceFor(similarItem.getID());
			double theCorrelation = scoredItem.getRating();
			if (!Double.isNaN(theCorrelation)) {
				// Why + 1.0? correlation ranges from -1.0 to 1.0, and we want to use it as a simple
				// weight. To avoid negative values, we add 1.0 to put it in
				// the [0.0,2.0] range which is reasonable for weights
				theCorrelation += 1.0;
				preference += theCorrelation * pref.getValue();
				totalCorrelation += theCorrelation;
			}
		}
		
		return totalCorrelation == 0.0 ? Double.NaN : preference / totalCorrelation;
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#getDataModel()
	 */
	@Override
	public DataModel getDataModel() {
		return model;
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#recommend(java.lang.Object, int, com.planetj.taste.recommender.Rescorer)
	 */
	@Override
	public List<RecommendedItem> recommend(Object arg0, int arg1,
			Rescorer<Item> arg2) throws TasteException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#recommend(java.lang.Object, int)
	 */
	@Override
	public List<RecommendedItem> recommend(Object arg0, int arg1)
			throws TasteException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#removePreference(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void removePreference(Object arg0, Object arg1)
			throws TasteException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#setPreference(java.lang.Object, java.lang.Object, double)
	 */
	@Override
	public void setPreference(Object arg0, Object arg1, double arg2)
			throws TasteException {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.common.Refreshable#refresh()
	 */
	@Override
	public void refresh() {
		throw new UnsupportedOperationException();
	}
}
