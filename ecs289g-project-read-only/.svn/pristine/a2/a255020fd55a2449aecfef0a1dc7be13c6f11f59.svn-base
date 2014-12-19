/**
 * 
 */
package edu.ucdavis.cs.movieminer.taste.recommender;

import java.util.List;

import com.google.common.base.Preconditions;
import com.planetj.taste.common.TasteException;
import com.planetj.taste.impl.recommender.AbstractRecommender;
import com.planetj.taste.model.DataModel;
import com.planetj.taste.model.Item;
import com.planetj.taste.recommender.RecommendedItem;
import com.planetj.taste.recommender.Recommender;
import com.planetj.taste.recommender.Rescorer;

/**
 * CompositeRecommender allows you to leverage more than one Recommender
 * to estimate preferences.  The CompositeRecommender will take a weighted 
 * average of the estimated preferences from each recommender to produce the 
 * result, with the weights being specified via {@link #setWeights(double...)}.
 *  
 * @author pfishero
 * @version $Id$
 */
public class CompositeRecommender extends AbstractRecommender {
	private double[] weights;
	private double weightsTotal;
	private final Recommender[] recommenders;
	
	public CompositeRecommender(final DataModel dataModel, Recommender ... recommenders) {
		super(dataModel);
		this.recommenders = recommenders;
	}
	
	public CompositeRecommender setWeights(double ... weights) {
		Preconditions.checkArgument(weights.length == recommenders.length);
		this.weights = weights;
		for (int i=0; i < weights.length; i++) {
			weightsTotal += weights[i];
		}
		
		return this;
	}
	
	public double[] getWeights() {
		return weights;
	}
	
	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#estimatePreference(java.lang.Object, java.lang.Object)
	 */
	@Override
	public double estimatePreference(Object userId, Object itemId)
			throws TasteException {
		double preferrence = 0;
		double weightRunningTotal = 0;
		
		for (int i=0; i < recommenders.length; i++) {
			double prefEstimate = recommenders[i].estimatePreference(userId, itemId);
			if (!Double.isNaN(prefEstimate)) {
				preferrence += prefEstimate * weights[i];
				weightRunningTotal += weights[i];
			}
		}
		preferrence /= weightRunningTotal;
		
		return preferrence;
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#recommend(java.lang.Object, int)
	 */
	@Override
	public List<RecommendedItem> recommend(Object userId, int howMany)
			throws TasteException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#recommend(java.lang.Object, int, com.planetj.taste.recommender.Rescorer)
	 */
	@Override
	public List<RecommendedItem> recommend(Object userId, int howMany,
			Rescorer<Item> rescorer) throws TasteException {
		throw new UnsupportedOperationException();
	}

}
