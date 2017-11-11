package learning;

import learning.interfaces.ITrainer;
import model.Pair;
import smile.classification.LogisticRegression;
import smile.clustering.GMeans;
import smile.clustering.PartitionClustering;
import smile.clustering.XMeans;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Andrea De Castri on 08/11/2017.
 *
 */
public class ModelTrainer implements ITrainer {

    private static final int MAX_CLUSTER = 100;

    private double[][] data;
    private int[] classification;
    private ClusterType clusterType;

    private PartitionClustering<double[]> clustering;
    private LogisticRegression[] logisticRegressions;

    public ModelTrainer(double[][] data, int[] classification, ClusterType clusterType){
        super();
        this.data = data;
        this.classification = classification;
        this.clusterType = clusterType;

        this.clustering(clusterType);
        this.classification();
    }

    private void clustering(ClusterType clusterType){
        switch (clusterType){
            case XMEANS:
                this.clustering = new XMeans(this.data, MAX_CLUSTER);
                break;
            case GMEANS:
                this.clustering = new GMeans(this.data, MAX_CLUSTER);
                break;
        }
    }

    private void classification(){
        int numClusters = this.clustering.getNumClusters();
        int[] labels = this.clustering.getClusterLabel();
        int[] clustersSize = this.clustering.getClusterSize();

        this.logisticRegressions = new LogisticRegression[numClusters];

        for(int i=0; i<numClusters; i++){
            double[][] x1 = new double[clustersSize[i]][];
            int[] y1 = new int[clustersSize[i]];
            for(int j=0, k=0; j<this.data.length && k<clustersSize[i]; j++){
                if(labels[j] == i){
                    x1[k] = this.data[j];
                    y1[k] = this.classification[j];
                    k++;
                }
            }
            this.logisticRegressions[i] = new LogisticRegression(x1, y1);
        }
    }

    @Override
    public PartitionClustering<double[]> getClustering() {
        return this.clustering;
    }

    @Override
    public LogisticRegression[] getClassifiers() {
        return this.logisticRegressions;
    }

    @Override
    public ClusterType getClusterType() {
        return this.clusterType;
    }

}
