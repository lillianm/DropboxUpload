/**
 * 
 */
package edu.ucdavis.cs.movieminer.data;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author pfishero
 * @version $Id$
 */
public class RatingItemProcessor implements ItemProcessor {
	public static final Logger logger = Logger.getLogger(RatingItemProcessor.class);

	protected RatingDao dao;
	
	public RatingItemProcessor() { }
	
	public void setDao(RatingDao dao) {
		this.dao = dao;
	}
	
	public RatingDao getDao() {
		return dao;
	}
	
	@Transactional
	public void process(Object item) {
		processItem(item);
	}

	@Transactional
	public void process(Collection items) {
		for (Object entry : items) {
			processItem(entry);
		}
	}
	
	/**
	 * @param entry
	 */
	protected void processItem(Object entry) {
		Rating rating;

		rating = Rating.convert((String[])entry);

		logger.debug(rating.getPrimaryKey().getUserId()+' '+
				rating.getPrimaryKey().getMovieId()+' '+rating.getRating());
		dao.update(rating);
	}
}
