/**
 * 
 */
package edu.ucdavis.cs.movieminer.taste;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import static edu.ucdavis.cs.movieminer.taste.Rating.*;

/**
 * Input: CSV file containing a list of customer movie ratings.
 * Ouput: Set of files, where each file is a movie and a set of ratings.
 * 
 * 
 * NOTE: By default the application will create all the files in the
 * current working directory.
 * 
 * @author jbeck
 *
 */
public class DataFormatter {
	
	private static final String FILE_PREFIX = "mv_";
	private List<Rating> ratings = new LinkedList<Rating>();
	private File dataFile;
	
	/**
	 * CSV data file.
	 * 
	 * @param dataFile
	 */
	DataFormatter(File dataFile){
		this.dataFile = dataFile;
	}
	
	public void format() throws IOException{
		final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));
		String line;
		// Parse file
		while ( (line = reader.readLine()) != null){
			ratings.add(createRating(line));
		}
		Comparator<Rating> movieIdSort = new Comparator<Rating>(){

			@Override
			public int compare(Rating row1, Rating row2) {
				return row1.getMovieId().compareTo(row2.getMovieId());
			}
			
		};
		// Sort by movieId
		Collections.sort(ratings, movieIdSort);
		// Create a file for each movieId
		int currentMovieId = -1;
		BufferedWriter currentMovieFile = null; 
		for(Rating rating : ratings){
			if (rating.getMovieId() == currentMovieId){
				// Add an entry to the current movie file
				write(rating, currentMovieFile);
				currentMovieFile.newLine();
			}else{
				// Close previous file
				if (currentMovieFile != null){
					currentMovieFile.flush();
					currentMovieFile.close();
				}
				// Create a new file and add an entry
				currentMovieFile = new BufferedWriter(new FileWriter(FILE_PREFIX+rating.getMovieId()));
				currentMovieFile.write(Integer.toString(rating.getMovieId())+" ");
				currentMovieFile.newLine();
				write(rating, currentMovieFile);
				currentMovieFile.newLine();
				currentMovieId = rating.getMovieId();
			}
		}
		currentMovieFile.flush();
		currentMovieFile.close();
	}
	
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String dataFile = args[0];
		DataFormatter formatter = new DataFormatter(new File(dataFile));
		formatter.format();

	}

}
