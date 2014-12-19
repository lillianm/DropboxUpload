/**
 * 
 */
package edu.ucdavis.cs.movieminer.taste;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.planetj.taste.correlation.ItemCorrelation;
import com.planetj.taste.impl.common.IteratorUtils;
import com.planetj.taste.impl.correlation.PearsonCorrelation;
import com.planetj.taste.model.DataModel;
import com.planetj.taste.model.Item;

/**
 * 
 * @author pfishero
 * @version $Id$
 */
public class PrecomputeItemSimilarities {
	public static final Logger logger = Logger.getLogger(PrecomputeItemSimilarities.class);
	
	/**
	 * Contains Object[20] of SimilarityScores.
	 * ItemID=1 will have its 20 most similar neighbors in slot 1 in this array.
	 * Slot 0 is unused so that an Item's index is equal to its Id, rather than
	 * its Id-1.
	 */
	private static final Object[] correlations = new Object[17771];
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			logger.info("Usage:");
			logger.info(" java TasteTest {netflix_data_dir} {chunk_start} {chunk_end}");
			System.exit(-1);
		} else {
			logger.info("using command line args: "+StringUtils.join(args, ", "));
		}
		
		final String netflixDataDir = args[0];
		final int chunkStart = Integer.parseInt(args[1]);
		final int chunkEnd = Integer.parseInt(args[2]);
		logger.info("netflixDataDir="+netflixDataDir+" start="+chunkStart+" end="+chunkEnd);
		
		final DataModel dataModel = new ECS289GNetflixDataModel(new File(netflixDataDir));
		
		final ItemCorrelation otherCorrelation = new PearsonCorrelation(dataModel);
		
		final List<? extends Item> items = IteratorUtils.iterableToList(dataModel.getItems());
		final int size = items.size();
		for (int i = 0; i < size; i++) {
			logger.info("item "+i);
			if (i >= chunkStart && i <= chunkEnd) {
				final Item item1 = items.get(i);
				for (int j = i + 1; j < size; j++) {
					final Item item2 = items.get(j);
					final double correlation = otherCorrelation.itemCorrelation(item1, item2);
					List<SimilarityScore> scores = (List<SimilarityScore>)correlations[(Integer)item1.getID()];
					if (scores == null) {
						scores = new ArrayList<SimilarityScore>();
						correlations[(Integer)item1.getID()] = scores;
					}
					scores.add(new SimilarityScore((Integer)item2.getID(), correlation));
				}
				if (null != correlations[(Integer)item1.getID()]) {
					// sort the items by similarity and then keep only the top 20
					Collections.sort(
							(List<SimilarityScore>)correlations[(Integer)item1.getID()],
							new Comparator<SimilarityScore>() {
								/**
								 * @param o1
								 * @param o2
								 * @return 1 if o1 should be before o2,
								 * -1 if o1 should be after o2, else 0 if their ratings 
								 * are equivalent.
								 */
								public int compare(SimilarityScore o1,
										SimilarityScore o2) {
									if (Double.isNaN(o1.getRating()) &&
											Double.isNaN(o2.getRating())) {
										return 0;
									} else if (Double.isNaN(o1.getRating()) &&
											!Double.isNaN(o2.getRating())) {
										return -1;
									} else if (!Double.isNaN(o1.getRating()) &&
											Double.isNaN(o2.getRating())) {
										return 1;
									} else {
										double diff = o1.getRating() - o2.getRating();
										if (diff > 0.0000001d) {
											return 1;
										} else if (diff < -0.0000001d) {
											return -1;
										} else { // close to 0 or at 0
											return 0;
										}
									}
								}
							});
					Collections.reverse((List<SimilarityScore>)correlations[(Integer)item1.getID()]);
					
					Object[] similarItems = new Object[20];
					int itemIndex=0;
					for (SimilarityScore simScore : 
							(List<SimilarityScore>)correlations[(Integer)item1.getID()]) {
						if (itemIndex < 20 && !Double.isNaN(simScore.getRating())) {
							similarItems[itemIndex] = simScore;
							logger.info("item1="+((NetflixMovie)item1).getTitle()+
									" otherItem="+((NetflixMovie)dataModel.getItem(simScore.getItemID())).getTitle()+
									" simScore="+simScore.getRating());
						}
						itemIndex++;
					}
					correlations[(Integer)item1.getID()] = similarItems;
				}
			}
		}
		logger.info("saving simliarity scores");
		ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(new File(netflixDataDir, "simScore"+chunkStart)));
		objOut.writeObject(correlations);
		objOut.close();
	}

}
