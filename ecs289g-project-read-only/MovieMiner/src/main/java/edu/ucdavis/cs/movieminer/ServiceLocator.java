/**
 * 
 */
package edu.ucdavis.cs.movieminer;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.ucdavis.cs.movieminer.data.MovieTitleDao;
import edu.ucdavis.cs.movieminer.data.RatingDao;

/**
 * 
 * @author pfishero
 * @version $Id$
 *
 */
public class ServiceLocator {
	private static final Logger logger = Logger.getLogger(ServiceLocator.class);
	
	private static final ServiceLocator instance = new ServiceLocator();
	private final ApplicationContext appContext;
	
	private ServiceLocator() {
		appContext = new ClassPathXmlApplicationContext(
				new String[] {"spring/movieminerApplicationContext.xml"});
	}

	public static final ServiceLocator getInstance() {
		return instance;
	}
	
	public ApplicationContext getAppContext() {
		return appContext;
	}
	
	public MovieTitleDao getMovieTitleDao () {
		return (MovieTitleDao)appContext.getBean("movieTitleDao");
	}
	
	public RatingDao getRatingDao () {
		return (RatingDao)appContext.getBean("ratingDao");
	}
	
}
