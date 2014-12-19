/**
 * 
 */
package edu.ucdavis.cs.movieminer.data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * 
 * @author pfishero
 * @version $Id$
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Rating.byId", query="FROM Rating umr " +
										"WHERE umr.primaryKey=:key"),
	@NamedQuery(name="Rating.byUserId", query="FROM Rating umr " +
										"WHERE umr.primaryKey.userId=:userId")
})
public class Rating {
	public static final Logger logger = Logger.getLogger(Rating.class);
	@EmbeddedId
	private RatingPK primaryKey;
	private int rating;
	@Column(name="RECO_DATE")
	private Date ratingRecommendedDate;
	
	private static final DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
	
	
	public Rating() {
		
	}
	
	public static final Rating convert(String[] fields) {
		Rating rating = new Rating();
		
		try {
			RatingPK key = new RatingPK();
			key.setMovieId(Long.parseLong(fields[0]));
			key.setUserId(Long.parseLong(fields[1]));
			rating.setPrimaryKey(key);
			rating.setRating(Integer.parseInt(fields[2]));
			rating.setRatingRecommendedDate(formatter.parse(fields[3]));
		} catch (ParseException e) {
			logger.error("error parsing "+fields[3]+" as a date");
		}
		
		return rating;
	}

	/**
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * @return the ratingRecommendedDate
	 */
	public Date getRatingRecommendedDate() {
		return ratingRecommendedDate;
	}

	/**
	 * @param ratingRecommendedDate the ratingRecommendedDate to set
	 */
	public void setRatingRecommendedDate(Date ratingRecommendedDate) {
		this.ratingRecommendedDate = ratingRecommendedDate;
	}
	
	/**
	 * @return the primaryKey
	 */
	public RatingPK getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * @param primaryKey the primaryKey to set
	 */
	public void setPrimaryKey(RatingPK primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	@Embeddable
	public static final class RatingPK implements Serializable {
		private Long userId;
		private Long movieId;
		
		public RatingPK() {
			
		}

		/**
		 * @return the userId
		 */
		public Long getUserId() {
			return userId;
		}

		/**
		 * @param userId the userId to set
		 */
		public void setUserId(Long userId) {
			this.userId = userId;
		}

		/**
		 * @return the movieId
		 */
		public Long getMovieId() {
			return movieId;
		}

		/**
		 * @param movieId the movieId to set
		 */
		public void setMovieId(Long movieId) {
			this.movieId = movieId;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof RatingPK == false) {
				return false;
			}
			if (this == obj) {
				return true;
			}
			RatingPK rhs = (RatingPK) obj;
			return new EqualsBuilder().
						append(userId, rhs.userId).
						append(movieId, rhs.movieId).
						isEquals();
		}
		
		@Override
		public int hashCode() {
			return new HashCodeBuilder(171, 371).
		       append(userId).
		       append(movieId).
		       toHashCode();
		}
	}
}
