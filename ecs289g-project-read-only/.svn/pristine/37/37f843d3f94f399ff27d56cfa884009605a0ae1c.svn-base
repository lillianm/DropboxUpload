/**
 * 
 */
package edu.ucdavis.cs.movieminer.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.log4j.Logger;

/**
 * 
 * @author pfishero
 * @version $Id$
 */
@Entity
@NamedQueries({
	@NamedQuery(name="MovieTitle.byId", query="FROM MovieTitle mt WHERE mt.id = :id")
})
public class MovieTitle {
	public static final Logger logger = Logger.getLogger(MovieTitle.class);
	
	@Id
	private long id;
	private Date releaseDate;
	private String title;
	
	private static final DateFormat formatter = new SimpleDateFormat("yyyy");
	
	public MovieTitle() {
		
	}
	
	public static final MovieTitle convert(LinkedList<String> fields) {
		MovieTitle movie = new MovieTitle();
		
		try {
			movie.setId(Long.parseLong(fields.get(0)));
			movie.setReleaseDate(formatter.parse(fields.get(1)));
			movie.setTitle(fields.get(2));
		} catch (ParseException e) {
			logger.error("error parsing "+fields.get(1)+" as a date");
		}
		
		return movie;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the releaseDate
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}

	/**
	 * @param releaseDate the releaseDate to set
	 */
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
