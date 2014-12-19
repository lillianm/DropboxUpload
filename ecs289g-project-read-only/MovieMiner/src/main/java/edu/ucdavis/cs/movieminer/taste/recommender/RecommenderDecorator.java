/**
 * 
 */
package edu.ucdavis.cs.movieminer.taste.recommender;

import com.planetj.taste.recommender.Recommender;

/**
 * Standard decorator .
 * 
 * @author jbeck
 *
 */
public abstract class RecommenderDecorator implements Recommender {

	protected Recommender decoratedRecommender;
	
	public RecommenderDecorator(Recommender recommender){
		decoratedRecommender = recommender;
	}
}
