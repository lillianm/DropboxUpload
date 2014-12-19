package edu.ucdavis.cs.movieminer.taste;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class InitialResults {
	public static final Logger logger = Logger.getLogger(InitialResults.class);
	private Resource test;
	private Resource answers;
	public Map<String, Integer> userMovieRating;
	
	public InitialResults() {
		userMovieRating = new HashMap<String, Integer>();
		load();
	}
	
	private final void load() {
		setTest(new FileSystemResource("/home/fisherog/dev/netflix_data/test-labeled.dat"));
		setAnswers(new FileSystemResource("/home/fisherog/dev/netflix_data/InitialResults.txt"));
	}
	
	public void setTest(Resource test) {
		this.test = test;
	}
	public void setAnswers(Resource answers) {
		this.answers = answers;
	}
	
	public int getRatingFor(Integer movieId, Integer userId) {
		Integer rating = userMovieRating.get(movieId+","+userId);
		
		return rating == null ? -1 : rating;
	}
	
	public void readAnswers() {
		try {
			LineIterator iterator = FileUtils.lineIterator(test.getFile());
			LineIterator iterator2 = FileUtils.lineIterator(answers.getFile());
			while (iterator.hasNext() && iterator2.hasNext()) {
				String trainLine = iterator.nextLine();
				String prediction = iterator2.nextLine();
				// movie is first, then user, then rating
				String[] entries = trainLine.split(",");
				if (prediction.trim().endsWith("Correct")) {
					userMovieRating.put(Integer.parseInt(entries[0])+","+Integer.parseInt(entries[1]), Integer.parseInt(entries[2]));
				} else if (prediction.indexOf("Incorrect") != -1) {
					String actualRating = prediction.split(":")[2].
											trim().split(",")[0].
											trim().split(" ")[1];
					int entry = Integer.parseInt(actualRating);
					userMovieRating.put(Integer.parseInt(entries[0])+","+Integer.parseInt(entries[1]),
							entry);
				} else {
					throw new IllegalStateException();
				}
				
			}
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	public static void main(String[] args) {
		InitialResults results = new InitialResults();
		results.readAnswers();
		logger.info("answers: "+results.userMovieRating.size());
	}
	
	
}
