/**
 * 
 */
package edu.ucdavis.cs.movieminer.data;

import javax.persistence.EntityManager;

/**
 * 
 * @author pfishero
 * @version $Id$
 */
public interface MovieTitleDao {
	/**
	 * @param id
	 * @return the MovieTitle for the given <code>id</code>, if one is found.
	 * If one is not found in the persistent store, then null is returned.
	 */
	MovieTitle findById(Long id);
	
	void save(MovieTitle movie);
	
	void update(MovieTitle movie);
	
	void delete(MovieTitle movie);
	
	EntityManager getEm();
}