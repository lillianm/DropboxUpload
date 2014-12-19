/**
 * 
 */
package edu.ucdavis.cs.movieminer.data;

import java.io.InputStream;

import org.springframework.core.io.Resource;

/**
 * 
 * @author pfishero
 * @version $Id$
 *
 */
public interface InputSourceItemProvider<T extends Object> extends Iterable<T> {
	void setInputSource(Resource source);
	/**
	 * Open any internal resources to prepare to iterate.
	 *  
	 * @throws Exception
	 */
	void open() throws Exception;
	/**
	 * Close any internal resources, but not including the source passed in via 
	 * {@link #setInputSource(InputStream)}.
	 * 
	 * @throws Exception
	 */
	void close() throws Exception;
}
