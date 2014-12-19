/**
 * 
 */
package edu.ucdavis.cs.movieminer.taste;

import static edu.ucdavis.cs.movieminer.taste.Rating.createRating;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.base.Join;
import com.planetj.taste.common.TasteException;
import com.planetj.taste.impl.model.netflix.NetflixDataModel;
import com.planetj.taste.model.DataModel;
import com.planetj.taste.recommender.Recommender;

import edu.ucdavis.cs.movieminer.taste.recommender.EvaluatingRecommender;

/**
 * Predicts a single rating for the passed in data and
 * recommender.
 * 
 * 
 * @author jbeck
 *
 */
public class MovieMiner {

	private static final Logger logger = Logger.getLogger(MovieMiner.class);
	private Collection<Rating> data;
	private Recommender recommender;
	
	public MovieMiner(Collection<Rating> data, Recommender recommender){
		this.data = data;
		this.recommender = recommender;
	}
	
	public Collection<Rating> getData(){
		return data;
	}
	
	/**
	 * Predicts a rating using the recommender implementation.
	 * If the estimate returned is NaN the estimate is the average
	 * rating for the user.  If no values exist in the training
	 * set to predict the estimate, it is chosen at random.
	 * 
	 * @throws TasteException
	 */
	public void recommend() throws TasteException{
		logger.debug("Recommending ratings.");
		for(Rating rating : data){
			double estimate = recommender.estimatePreference(rating.getUserId(), rating.getMovieId());
			logger.debug("rating <userid, movieid> "+"<"+rating.getUserId()+","+"<"+rating.getMovieId()+">");	
			BigDecimal bd = new BigDecimal(estimate);
			bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
			logger.debug("setting the rating value to: "+bd.intValue());
			rating.setRating(bd.intValue());
		}
	}
	
	/**
	 * Writes ratings to writer.
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void write(Writer writer) throws IOException{
		logger.debug("Writing ratings to file.");
		Rating.write(data, writer);
	}
	
	/**
	 * args[0] is the test file.
	 * args[1] is the netflix home directory, containing training_set directory and movie_titles.txt
	 * args[2] number of neighbors used in prediction
	 * args[3] output file where predicted ratings are stored
	 * args[4] start
	 * args[5] end
	 * 
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws TasteException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String args[]) throws IOException, ClassNotFoundException, TasteException, InstantiationException, IllegalAccessException{
		if (args.length < 4) {
			System.out.println("Usage:");
			System.out.println(" java MovieMiner " +
					"{netflix_test_file} {netflix_home_dir} " +
					"{k_neighbor_value} " +
					"{output file} " +
					"{start} {end} "+
					"_optional params:_ "+
					"{runName} {userWeight} {itemWeight} {slopeOneWeight} {useSlopeOne} ");
			System.exit(-1);
		} else {
			System.out.println("using command line args: "+Join.join(", ", args));
		}
		
		String runName = args.length > 6 ? '['+args[6]+']' : ""; 
		double userWeight = args.length > 7 ? Double.parseDouble(args[7]) : 0.30d;
		double itemWeight = args.length > 8 ? Double.parseDouble(args[8]) : 0.00d;
		double slopeOneWeight = args.length > 9 ? Double.parseDouble(args[9]) : 0.70d;
		boolean useSlopeOne = args.length > 10 ? Boolean.valueOf(args[10]) : true;
		
		final int neighbors = Integer.parseInt(args[2]);
		// Read in the ratings from the data set.
		List<Rating> ratings = new LinkedList<Rating>(); 
		final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
		String line;
		// Parse file
		while ( (line = reader.readLine()) != null){
			ratings.add(createRating(line));
		}
		
		int start = Integer.parseInt(args[4]);
		int end = Integer.parseInt(args[5]);	
		ratings = ratings.subList(start,end+1);
		
		// Build data model
		DataModel myModel = new NetflixDataModel(new File(args[1]));
		SpelunkerRecommenderBuilder builder = new SpelunkerRecommenderBuilder();
		builder.setUserNeighbors(neighbors);
		builder.setUserWeight(userWeight);
		builder.setItemWeight(itemWeight);
		builder.setSlopeOneWeight(slopeOneWeight);
		builder.setUseSlopeOne(useSlopeOne);
		Recommender recommender = builder.buildRecommender(myModel);
		
		// Recommend
		MovieMiner miner = new MovieMiner(ratings, recommender);
		miner.recommend();
		
		// Log useful stats
		EvaluatingRecommender evalRecommender = builder.getEvalRecommender();
		logger.info(runName+" Eval stats: "+evalRecommender.getAccuracyOutput());
		logger.info(runName+" Eval stats! "+
				evalRecommender.getEstimateCount()+','+
				evalRecommender.getCorrectCount()+','+
				evalRecommender.getIncorrectCount()+','+
				evalRecommender.getTotalLoss());
		
		// Output the recommendation to a file.
		Writer writer = new BufferedWriter(new FileWriter(args[3]));
		miner.write(writer);
		writer.flush();
		writer.close();
	}
}
