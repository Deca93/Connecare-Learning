package learning.interfaces;

import smile.classification.LogisticRegression;
import smile.clustering.PartitionClustering;
import smile.data.AttributeDataset;

/**
 * Created by Andrea De Castri on 11/11/2017.
 *
 */
public interface IApplier {

    /**
     * Evaluates the data passed by user
     * @param data Data to evaluate
     * @return AttributeDataset: dataset within the data
     */
    AttributeDataset apply(double[][] data);

}
