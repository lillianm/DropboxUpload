/**
 * 
 */
package edu.ucdavis.cs.movieminer.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author pfishero
 * @version $Id$
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class MovieTitleDaoImpl implements MovieTitleDao {
	public static final Logger logger = Logger.getLogger(MovieTitleDaoImpl.class);

	@PersistenceContext
    private EntityManager em;
	
	public EntityManager getEm() {
		return em;
	}
	
	public void delete(MovieTitle movieTitle) {
		em.remove(movieTitle);
	}
	
	/* (non-Javadoc)
	 * @see edu.ucdavis.cs.movieminer.data.MovieTitleDao#findById(java.lang.Long)
	 */
	public MovieTitle findById(Long id) {
		MovieTitle movie = null;
		
		try {
			Query query = em.createNamedQuery("MovieTitle.byId");
			query.setParameter("id", id);
			movie = (MovieTitle)query.getSingleResult();
		} catch (javax.persistence.NoResultException nre) {
			logger.warn("didn't find movietitle with id: "+id);
		}
		
		return movie;
	}

	public void save(MovieTitle movie) {
		em.persist(movie);
	}

	/**
	 * Note: page must have its Id set or else this may fail with an 
	 * "entity already exists exception".
	 * 
	 * @param page
	 */
	public void update(MovieTitle movie) {
		em.merge(movie);
	}

}
