package edu.ucdavis.cs.movieminer.data;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

public class MovieTitleItemProcessor implements ItemProcessor<Object> {
	public static final Logger logger = Logger.getLogger(MovieTitleItemProcessor.class);

	protected MovieTitleDao dao;
	
	public MovieTitleItemProcessor() { }
	
	public void setDao(MovieTitleDao dao) {
		this.dao = dao;
	}
	
	public MovieTitleDao getDao() {
		return dao;
	}
	
	@Transactional
	public void process(Object item) {
		processItem(item);
	}

	@Transactional
	public void process(Collection items) {
		for (Object entry : items) {
			processItem(entry);
		}
	}
	
	/**
	 * @param entry
	 */
	protected void processItem(Object entry) {
		MovieTitle movie;

		movie = MovieTitle.convert((LinkedList<String>)entry);

		logger.debug(movie.getId()+" "+movie.getTitle());
		dao.update(movie);
	}

}
