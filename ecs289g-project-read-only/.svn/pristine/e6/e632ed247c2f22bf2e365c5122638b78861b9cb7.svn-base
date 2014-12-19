/**
 * 
 */
package edu.ucdavis.cs.movieminer.taste;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

/**
 * Simple data structure that represent a movie rating.
 * 
 * @author jbeck
 *
 */
public class Rating {

	private Integer userId;
	private Integer movieId;
	private Integer rating;
	private String date;
	
	public Rating(Integer userId, Integer movieId, Integer rating, String date){
		this.userId = userId;
		this.movieId = movieId;
		this.rating = rating;
		this.date = date;
	}

	public Integer getUserId() {
		return userId;
	}

	public Integer getMovieId() {
		return movieId;
	}

	public Integer getRating() {
		return rating;
	}
	
	public void setRating(int rating){
		this.rating = rating;
	}
	
	public String getDate(){
		return date;
	}
	
	public static void write(Rating rating, Writer writer) throws IOException{
		writer.write(rating.getMovieId()+","+rating.getUserId()+","+rating.getRating()+","+rating.getDate());
	}
	
	public static void write(Collection<Rating> ratings, Writer writer) throws IOException{
		for(Rating rating : ratings){
			write(rating, writer);
			writer.write('\n');
		}
	}
	
	/**
	 * Expected line format: movieId,userId,rating,date
	 * 
	 * NOTE: If the rating is not specified (marked with a '?') the rating
	 * value remains null.
	 * 
	 * @param line
	 * @return
	 */
	public static Rating createRating(String line){
		int firstComma = line.indexOf(',');
		int movieId = Integer.valueOf(line.substring(0, firstComma));
		int secondComma = line.indexOf(',', firstComma+1);
		int userId = Integer.parseInt(line.substring(firstComma+1, secondComma));
		int thirdComma = line.indexOf(',', secondComma + 1);
		String ratingValue = line.substring(secondComma+1, thirdComma);
		Integer rating = null;
		if (!"?".equals(ratingValue)){
			rating = Integer.parseInt(ratingValue);
		}
		String date = line.substring(thirdComma+1);
		return new Rating(userId, movieId, rating, date);
	}
	
}
