/**
 * 
 */
package edu.ucdavis.cs.movieminer.data;

import java.util.List;

import org.apache.log4j.Logger;

import edu.ucdavis.cs.movieminer.ServiceLocator;

/**
 * 
 * @author pfishero
 * @version $Id$
 */
public class QuickTest {
	public static final Logger logger = Logger.getLogger(QuickTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Rating> userRatings = ServiceLocator.getInstance().getRatingDao().findByUserId(134);
		for (Rating rating : userRatings) {
			MovieTitle title = ServiceLocator.getInstance().getMovieTitleDao().findById(rating.getPrimaryKey().getMovieId());
			if (null != title) {
				logger.info("userId="+rating.getPrimaryKey().getUserId()+
						" rating="+rating.getRating()+
						" movie="+title.getTitle());
			} else {
				logger.error("movie not found - id="+rating.getPrimaryKey().getMovieId());
			}
		}
	}

}
