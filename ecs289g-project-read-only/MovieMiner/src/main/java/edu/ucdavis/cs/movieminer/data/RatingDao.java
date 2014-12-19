package edu.ucdavis.cs.movieminer.data;

import java.util.List;

import javax.persistence.EntityManager;

/**
 * 
 * @author pfishero
 * @version $Id$
 */
public interface RatingDao {
	/**
	 * @param userId
	 * @param movieId
	 * @return the Rating for the given <code>userId</code> and 
	 * <code>movieId</code>, if one is found.
	 * If one is not found in the persistent store, then null is returned.
	 */
	Rating findById(long userId, long movieId);
	/**
	 * @param userId
	 * @return the Rating for the given <code>userId</code>, if any are found.
	 * If none are found in the persistent store, then null is returned.
	 */
	List<Rating> findByUserId(long userId);
	
	void save(Rating movie);
	
	void update(Rating movie);
	
	void delete(Rating movie);
	
	EntityManager getEm();
}
