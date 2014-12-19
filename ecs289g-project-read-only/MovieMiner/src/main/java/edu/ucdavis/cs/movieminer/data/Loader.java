package edu.ucdavis.cs.movieminer.data;

import edu.ucdavis.cs.movieminer.ServiceLocator;

public class Loader {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		DataLoader dataLoader = (DataLoader)ServiceLocator.getInstance().
								getAppContext().getBean("movieTitleDataLoader");
		dataLoader.doLoad();
		
		DataLoader ratingDataLoader = (DataLoader)ServiceLocator.getInstance().
								getAppContext().getBean("ratingDataLoader");
		ratingDataLoader.doLoad();
	}

}
