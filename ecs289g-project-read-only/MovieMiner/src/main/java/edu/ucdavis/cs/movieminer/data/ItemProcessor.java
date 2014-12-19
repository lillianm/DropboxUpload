/**
 * 
 */
package edu.ucdavis.cs.movieminer.data;

import java.util.Collection;

/**
 * 
 * @author pfishero
 * @version $Id$
 *
 */
public interface ItemProcessor<T extends Object> {
	/**
	 * Process one item.
	 * @param item
	 */
	void process(T item);
	
	/**
	 * Process a collection of items.
	 * @param items
	 */
	void process(Collection<T> items);
}
