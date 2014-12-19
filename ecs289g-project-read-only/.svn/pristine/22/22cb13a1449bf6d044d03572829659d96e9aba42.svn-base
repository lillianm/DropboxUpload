/**
 * 
 */
package edu.ucdavis.cs.movieminer.taste;

import java.io.File;

import org.apache.log4j.Logger;

import com.planetj.taste.eval.RecommenderEvaluator;
import com.planetj.taste.impl.eval.RMSRecommenderEvaluator;
import com.planetj.taste.impl.model.netflix.NetflixDataModel;
import com.planetj.taste.model.DataModel;

import edu.ucdavis.cs.movieminer.taste.recommender.EvaluatingRecommender;

/**
 * Requires the following file and directory
 * to be in the netflix_data_dir.  
 * 
 * movie_titles.txt (file)
 * training_set (directory containing movie ratings)
 * 
 * 
 * @author pfishero
 * @version $Id$
 */
public class TasteTest {
	public static final Logger logger = Logger.getLogger(TasteTest.class);


	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.out.println("Usage:");
			System.out.println(" java TasteTest {netflix_data_dir} {k_neighbor_value}");
			System.exit(-1);
		} else {
			System.out.println("using command line args: "+args);
		}
		
		final String netflixDataDir = args[0];
		final int userNeighbors = Integer.parseInt(args[1]);
		final int itemNeighbors = Integer.parseInt(args[2]);
		logger.info("netflixDataDir="+netflixDataDir+
				" user_neighbors="+userNeighbors+
				" item_neighbors="+itemNeighbors);
		
		DataModel myModel = new NetflixDataModel(new File(netflixDataDir));
		SpelunkerRecommenderBuilder builder = new SpelunkerRecommenderBuilder();
		builder.setUserNeighbors(userNeighbors);
		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
		double evaluation = evaluator.evaluate(builder, myModel, 0.9, 1.0);
		logger.info("evaluation = "+evaluation);
		EvaluatingRecommender evalRecommender = builder.getEvalRecommender();
		logger.info("Eval recommender correct count: "+evalRecommender.getCorrectCount());
		logger.info("Eval recommender incorrect count: "+evalRecommender.getIncorrectCount());
		logger.info("Eval recommender estimated count: "+evalRecommender.getEstimateCount());
		logger.info("Eval recommender loss count: "+evalRecommender.getTotalLoss());
	}

}
