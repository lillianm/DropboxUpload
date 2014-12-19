package edu.ucdavis.cs.movieminer.taste.recommender;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;

import com.planetj.taste.common.TasteException;
import com.planetj.taste.model.DataModel;
import com.planetj.taste.model.Item;
import com.planetj.taste.recommender.RecommendedItem;
import com.planetj.taste.recommender.Recommender;
import com.planetj.taste.recommender.Rescorer;

import edu.ucdavis.cs.movieminer.taste.InitialResults;

public class EvaluatingRecommender extends RecommenderDecorator {

	private static final Logger logger = Logger.getLogger(LoggingRecommender.class);
	int estimateCount;
	int correctCount;
	int incorrectCount;
	long totalLoss;
	private InitialResults results;
	private NumberFormat percentFormatter = NumberFormat.getPercentInstance();	
	
	public EvaluatingRecommender(Recommender recommender) {
		super(recommender);
		results = new InitialResults();
		results.readAnswers();
	}

	@Override
	public double estimatePreference(Object userID, Object itemID)
			throws TasteException {
		double preference = decoratedRecommender.estimatePreference(userID, itemID);
		BigDecimal bd = new BigDecimal(preference); 
		int intValue = bd.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

		trackAccuracy(userID, itemID, intValue);
		
		return preference;
	}
	
	public void trackAccuracy(Object userID, Object itemID, int rating) {
		int actualValue = results.getRatingFor(Integer.parseInt(itemID.toString()), Integer.parseInt(userID.toString()));

		estimateCount++;
		if (rating == actualValue) {
			correctCount++;
			logger.info("[eval] Correct");
		} else {
			incorrectCount++;
			double loss = (rating-actualValue)*(rating-actualValue);
			logger.info("[eval] Incorrect: Actual "+actualValue+", Predicted "+rating+", Loss "+loss);
			totalLoss += loss;
		}
		
		if (estimateCount%100 == 0) {			
			logger.info("Number of estimated values: "+estimateCount);
			logger.info(getAccuracyOutput());
		}		
	}

	public String getAccuracyOutput() {
		return "Accuracy: total="+estimateCount+" correct="+correctCount+
				" ("+percentFormatter.format(correctCount/(double)estimateCount)+") "+
				"incorrect="+incorrectCount+
				" ("+percentFormatter.format(incorrectCount/(double)estimateCount)+") "+
				"loss="+totalLoss;
	}

	@Override
	public List<RecommendedItem> recommend(Object userID, int howMany,
			Rescorer<Item> rescorer) throws TasteException {
		return decoratedRecommender.recommend(userID, howMany, rescorer);
	}

	@Override
	public List<RecommendedItem> recommend(Object userID, int howMany)
			throws TasteException {
		return decoratedRecommender.recommend(userID, howMany);
	}

	@Override
	public DataModel getDataModel() {
		return decoratedRecommender.getDataModel();
	}

	@Override
	public void removePreference(Object userID, Object itemID)
			throws TasteException {
		decoratedRecommender.removePreference(userID, itemID);
	}

	@Override
	public void setPreference(Object userID, Object itemID, double value)
			throws TasteException {
		decoratedRecommender.setPreference(userID, itemID, value);
	}

	@Override
	public void refresh() {
		decoratedRecommender.refresh();
	}

	public int getEstimateCount() {
		return estimateCount;
	}

	public int getCorrectCount() {
		return correctCount;
	}

	public int getIncorrectCount() {
		return incorrectCount;
	}

	public long getTotalLoss() {
		return totalLoss;
	}

	public InitialResults getResults() {
		return results;
	}
	
	public static void main(String[] args) {
		// first arg is the predictions file
		File predictions = new File(args[0]);
		EvaluatingRecommender rec = new EvaluatingRecommender(null);
		
		LineIterator it = null;
		try {
			it = FileUtils.lineIterator(predictions);
			while (it.hasNext()) {
				String line = it.nextLine();
				// movie is first, then user, then rating
				String[] entries = line.split(",");
				int rating = Integer.parseInt(entries[2]);
				rec.trackAccuracy(entries[1], entries[0], rating);
			}
		} catch (IOException ioex) {
			logger.error(ioex);
		} finally {
			LineIterator.closeQuietly(it);
		}
		
		logger.info(rec.getAccuracyOutput());
	}
}
