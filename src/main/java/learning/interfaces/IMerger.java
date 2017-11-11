package learning.interfaces;

import smile.data.AttributeDataset;

/**
 * Created by Andrea De Castri on 11/11/2017.
 *
 */
public interface IMerger {

    /**
     * Merge the data from two AttributeDataset
     * @param first AttributeDataset first
     * @param second AttributeDataset second
     * @return AttributeDataset with merged data
     */
    AttributeDataset merge(AttributeDataset first, AttributeDataset second);

}
