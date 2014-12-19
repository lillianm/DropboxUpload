package edu.ucdavis.cs.movieminer.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import edu.ucdavis.cs.movieminer.data.Rating.RatingPK;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class RatingDaoImpl implements RatingDao {
	public static final Logger logger = Logger.getLogger(RatingDaoImpl.class);

	@PersistenceContext
    private EntityManager em;
	
	public EntityManager getEm() {
		return em;
	}
	
	public void delete(Rating Rating) {
		em.remove(Rating);
	}
	
	/* (non-Javadoc)
	 * @see edu.ucdavis.cs.movieminer.data.RatingDao#findById(java.lang.Long)
	 */
	public Rating findById(long userId, long movieId) {
		Rating movie = null;
		
		try {
			Query query = em.createNamedQuery("Rating.byId");
			RatingPK key = new RatingPK();
			key.setUserId(userId);
			key.setMovieId(movieId);
			query.setParameter("key", key);
			movie = (Rating)query.getSingleResult();
		} catch (javax.persistence.NoResultException nre) {
			logger.warn("didn't find Rating with id: "+userId+','+movieId);
		}
		
		return movie;
	}
	
	public List<Rating> findByUserId(long userId) {
		List<Rating> movies = Lists.newArrayList();
		
		try {
			Query query = em.createNamedQuery("Rating.byUserId");
			query.setParameter("userId", userId);
			movies = (List<Rating>)query.getResultList();
		} catch (javax.persistence.NoResultException nre) {
			logger.warn("didn't find Rating with userId: "+userId);
		}
		
		return movies;
	}

	public void save(Rating movie) {
		em.persist(movie);
	}

	/**
	 * Note: page must have its Id set or else this may fail with an 
	 * "entity already exists exception".
	 * 
	 * @param page
	 */
	public void update(Rating movie) {
		em.merge(movie);
	}
}
