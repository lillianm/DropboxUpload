/**
 * 
 */
package edu.ucdavis.cs.movieminer.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * DataLoader for CSV data files.
 * 
 * @author pfishero
 * @version $Id$
 */
public class CsvDataLoader implements DataLoader {
	public static final Logger logger = Logger.getLogger(CsvDataLoader.class);

	private InputSourceItemProvider<Object> provider;
	private ItemProcessor<Object> processor;
	
	private int batchSize;
	
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	
	public int getBatchSize() {
		return batchSize;
	}
	
	/* (non-Javadoc)
	 * @see edu.ucdavis.cs.wikipedia.data.DataLoader#doLoad()
	 */
	public void doLoad() throws Exception {
		if (null == provider) {
			throw new IllegalStateException(
					"provider must be set before calling doLoad()");
		} else if (null == processor) {
			throw new IllegalStateException(
					"processor must be set before calling doLoad()");
		}

		try {
			provider.open();
			
			if (0 == batchSize) { // load one at a time
				for (Object item : provider) {
					processor.process(item);
				}
			} else { // do a bulk load
				List<Object> items = new ArrayList<Object>(batchSize);
				int i=0;
				for (Object item : provider) {
					items.add(item);
					if ((i > 0) && (i % (batchSize/10) == 0)) {
						logger.info("processing " + i);
					}
					if ((i > 0) && (i % batchSize == 0)) {
						processor.process(items);
						logger.info("committed after "+ i + " processed");
						items.clear();
					}
					i++;
				}
				processor.process(items);
				logger.info(i + " processed");
			}
		} finally {
			provider.close();
		}
	}

	/* (non-Javadoc)
	 * @see edu.ucdavis.cs.wikipedia.data.DataLoader#setInputSourceItemProvider(edu.ucdavis.cs.wikipedia.data.InputSourceItemProvider)
	 */
	public void setInputSourceItemProvider(
			InputSourceItemProvider<Object> provider) {
		this.provider = provider;
	}

	/* (non-Javadoc)
	 * @see edu.ucdavis.cs.wikipedia.data.DataLoader#setItemProcessor(edu.ucdavis.cs.wikipedia.data.ItemProcessor)
	 */
	public void setItemProcessor(ItemProcessor<Object> processor) {
		this.processor = processor;
	}

}
