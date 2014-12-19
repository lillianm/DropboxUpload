/**
 * 
 */
package edu.ucdavis.cs.movieminer.taste.recommender;

import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.planetj.taste.common.TasteException;
import com.planetj.taste.model.DataModel;
import com.planetj.taste.model.Item;
import com.planetj.taste.recommender.RecommendedItem;
import com.planetj.taste.recommender.Recommender;
import com.planetj.taste.recommender.Rescorer;

/**
 * If all else fails, guess.
 * 
 * @author jbeck
 *
 */
public class RandomRecommender extends RecommenderDecorator {

	private static final Logger logger = Logger.getLogger(RandomRecommender.class);
	private int randomTotal = 0;
	
	public RandomRecommender(Recommender recommender) {
		super(recommender);
	}

	/* (non-Javadoc)
	 * @see com.planetj.taste.recommender.Recommender#estimatePreference(java.lang.Object, java.lang.Object)
	 */
	@Override
	public double estimatePreference(Object userID, Object itemID)
			throws TasteException {
		double recommendation = decoratedRecommender.estimatePreference(userID, itemID);
		if (Double.isNaN(recommendation) || recommendation == 0){
			logger.debug("Generating random score");
			recommendation = guess();
			randomTotal++;
			logger.info("The total predictions scored with a random value: "+randomTotal);
		}
		return recommendation;
	}
	
	private int guess(){
		Random random = new Random();
		int score = random.nextInt(6);
		if (score == 0){
			score++;
		} 
		return score;
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

	public int getRandomTotal(){
		return randomTotal;
	}
}
