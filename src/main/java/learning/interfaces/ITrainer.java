package learning.interfaces;

import learning.ClusterType;
import smile.classification.LogisticRegression;
import smile.clustering.PartitionClustering;

/**
 * Created by Andrea De Castri on 11/11/2017.
 *
 */
public interface ITrainer {

    /**
     * Returns the clustering
     * @return Partition clustering
     */
    PartitionClustering<double[]> getClustering();

    /**
     * Returns the list of the classifiers
     * @return Classifiers list
     */
    LogisticRegression[] getClassifiers();

    /**
     * Returns the type of the cluster
     * @return Cluster type
     */
    ClusterType getClusterType();

}
