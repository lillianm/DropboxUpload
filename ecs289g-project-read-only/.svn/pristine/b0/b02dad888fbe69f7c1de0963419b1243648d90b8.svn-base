package edu.ucdavis.cs.movieminer.taste;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.planetj.taste.common.TasteException;
import com.planetj.taste.impl.common.IOUtils;
import com.planetj.taste.impl.model.GenericDataModel;
import com.planetj.taste.impl.model.GenericPreference;
import com.planetj.taste.impl.model.GenericUser;
import com.planetj.taste.model.DataModel;
import com.planetj.taste.model.Item;
import com.planetj.taste.model.Preference;
import com.planetj.taste.model.User;

import edu.ucdavis.cs.movieminer.ServiceLocator;
import edu.ucdavis.cs.movieminer.data.InputSourceItemProvider;

/**
 * @author pfishero (adapted from Sean Owen's NetflixDataModel class)
 */
public final class ECS289GNetflixDataModel implements DataModel {

	private static final Logger log = Logger.getLogger(ECS289GNetflixDataModel.class.getName());

	private final DataModel delegate;

	public ECS289GNetflixDataModel(final File dataDirectory) throws Exception {
		if (dataDirectory == null) {
			throw new IllegalArgumentException("dataDirectory is null");
		}
		if (!dataDirectory.exists() || !dataDirectory.isDirectory()) {
			throw new FileNotFoundException(dataDirectory.toString());
		}

		log.info("Creating NetflixDataModel for directory: " + dataDirectory);

		log.info("Reading movie data...");
		final List<NetflixMovie> movies = readMovies(dataDirectory);

		log.info("Reading preference data...");
		final List<User> users = readUsers(dataDirectory, movies);

		log.info("Creating delegate DataModel...");
		delegate = new GenericDataModel(users);
	}
	
	public Preference[] getPreferencesForItemAsArray(Object itemID)
			throws TasteException {
		return delegate.getPreferencesForItemAsArray(itemID);
	}
	
	private static List<User> readUsers(final File dataDirectory, final List<NetflixMovie> movies) throws Exception {
		final Map<Integer, List<Preference>> userIDPrefMap =
			new HashMap<Integer, List<Preference>>(104395301, 1.0f);

		InputSourceItemProvider<String[]> provider = 
			(InputSourceItemProvider<String[]>) 
			ServiceLocator.getInstance().getAppContext().getBean(
											"ratingInputSourceItemProviderBean");
		provider.open();
		int counter = 0;

		for (String[] fields : provider) {
			final int movieID = Integer.parseInt(fields[0]);
			// index of movie i is i-1 as movies are numbers starting with 1
			final NetflixMovie movie = movies.get(movieID - 1);
			final Integer userID = Integer.valueOf(fields[1]);
			final double rating = Double.parseDouble(fields[2]);
			List<Preference> userPrefs = userIDPrefMap.get(userID);
			if (userPrefs == null) {
				userPrefs = new ArrayList<Preference>();
				userIDPrefMap.put(userID, userPrefs);
			}
			userPrefs.add(new GenericPreference(null, movie, rating));
			
			counter++;
			if (counter % 50000 == 0) {
				log.info("Processed " + counter + " prefs");
			}
		}
		provider.close();

		final List<User> users = new ArrayList<User>(userIDPrefMap.size());
		for (final Map.Entry<Integer, List<Preference>> entry : userIDPrefMap.entrySet()) {
			users.add(new GenericUser<Integer>(entry.getKey(), entry.getValue()));
		}
		return users;
	}

	
	private static List<NetflixMovie> readMovies(final File dataDirectory) throws IOException {
		final List<NetflixMovie> movies = new ArrayList<NetflixMovie>(17770);
		final BufferedReader reader =
			new BufferedReader(new InputStreamReader(new FileInputStream(new File(dataDirectory, "movie_titles.txt"))));
		String line;
		while ((line = reader.readLine()) != null) {
			final int firstComma = line.indexOf((int) ',');
			final Integer id = Integer.valueOf(line.substring(0, firstComma));
			final int secondComma = line.indexOf((int) ',', firstComma + 1);
			final String title = line.substring(secondComma + 1);
			movies.add(new NetflixMovie(id, title));
		}
		IOUtils.quietClose(reader);
		return movies;
	}


	/**
	 * {@inheritDoc}
	 */
	
	public Iterable<? extends User> getUsers() throws TasteException {
		return delegate.getUsers();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws java.util.NoSuchElementException if there is no such user
	 */
	
	public User getUser(final Object id) throws TasteException {
		return delegate.getUser(id);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public Iterable<? extends Item> getItems() throws TasteException {
		return delegate.getItems();
	}

	/**
	 * {@inheritDoc}
	 */
	
	public Item getItem(final Object id) throws TasteException {
		return delegate.getItem(id);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public Iterable<? extends Preference> getPreferencesForItem(final Object itemID) throws TasteException {
		return delegate.getPreferencesForItem(itemID);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getNumItems() throws TasteException {
		return delegate.getNumItems();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getNumUsers() throws TasteException {
		return delegate.getNumUsers();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public void setPreference(final Object userID, final Object itemID, final double value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public void removePreference(final Object userID, final Object itemID) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public void refresh() {
		// do nothing
	}

}
