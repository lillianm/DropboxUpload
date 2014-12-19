/**
 * 
 */
package edu.ucdavis.cs.movieminer.taste.recommender;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import com.planetj.taste.common.TasteException;
import com.planetj.taste.model.DataModel;
import com.planetj.taste.model.Item;
import com.planetj.taste.model.Preference;
import com.planetj.taste.model.User;
import com.planetj.taste.recommender.RecommendedItem;
import com.planetj.taste.recommender.Recommender;
import com.planetj.taste.recommender.Rescorer;

/**
 * If the prediction returns NaN a weighted average is performed.
 * 
 * @author jbeck
 *
 */
public class WeightedAverageRecommender extends RecommenderDecorator {

	private static final Logger logger = Logger.getLogger(WeightedAverageRecommender.class);
	private double userWeight;
	private double itemWeight;
	private int averagedTotal = 0;
	
	public WeightedAverageRecommender(Recommender recommender, double userWeight, double itemWeight) {
		super(recommender);
		this.userWeight = userWeight;
		this.itemWeight = itemWeight;
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#estimatePreference(java.lang.Object, java.lang.Object)
	 */
	@Override
	public double estimatePreference(Object userID, Object itemID)
			throws TasteException {
		double recommendation = decoratedRecommender.estimatePreference(userID, itemID);
		if (Double.isNaN(recommendation)){
			recommendation = score(getDataModel().getUser(userID), Integer.parseInt(itemID.toString()));
		}
		return recommendation;
	}
	
	private int score(User user, int movieId) throws TasteException{
		int score = 0;
		// first try calculating an average
		logger.debug("Score using a weighted average of user and item averages.");
		int userAverage = userAverage(user);
		int itemAverage = itemAverage(movieId);
		BigDecimal bd = new BigDecimal(userAverage*userWeight + itemAverage*itemWeight);
		score = bd.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		averagedTotal++;
		logger.info("The total predictions scored with a weighted average: "+averagedTotal);
		return score;
	}
	
	private int userAverage(User user){
		return preferenceAverage(user.getPreferencesAsArray());
	}
	
	private int itemAverage(int itemId) throws TasteException{
		return preferenceAverage(getDataModel().getPreferencesForItemAsArray(Integer.toString(itemId)));
	}
	
	private int preferenceAverage(Preference[] preferences){
		int sum = 0;
		for (Preference preference : preferences){
			sum += preference.getValue();
		}
		return (preferences.length != 0) ? sum / preferences.length : 0;
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#getDataModel()
	 */
	@Override
	public DataModel getDataModel() {
		return decoratedRecommender.getDataModel();
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#recommend(java.lang.Object, int)
	 */
	@Override
	public List<RecommendedItem> recommend(Object userID, int howMany)
			throws TasteException {
		return decoratedRecommender.recommend(userID, howMany);
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#recommend(java.lang.Object, int, com.planetj.taste.recommender.Rescorer)
	 */
	@Override
	public List<RecommendedItem> recommend(Object userID, int howMany,
			Rescorer<Item> rescorer) throws TasteException {
		return decoratedRecommender.recommend(userID, howMany);
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#removePreference(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void removePreference(Object userID, Object itemID)
			throws TasteException {
		decoratedRecommender.removePreference(userID, itemID);
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#setPreference(java.lang.Object, java.lang.Object, double)
	 */
	@Override
	public void setPreference(Object userID, Object itemID, double value)
			throws TasteException {
		decoratedRecommender.setPreference(userID, itemID, value);
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.common.Refreshable#refresh()
	 */
	@Override
	public void refresh() {
		decoratedRecommender.refresh();
	}

	public int getAveragedTotal() {
		return averagedTotal;
	}
}
