package learning;

import learning.interfaces.IApplier;
import smile.classification.LogisticRegression;
import smile.clustering.PartitionClustering;
import smile.data.AttributeDataset;

/**
 * Created by Andrea De Castri on 11/11/2017.
 *
 */
public class ModelApplier implements IApplier {

    private static final String TITLE = "Result Dataset";

    private AttributeDataset trainingData;
    private PartitionClustering<double[]> clustering;
    private LogisticRegression[] classifiers;

    public ModelApplier(AttributeDataset trainingData, PartitionClustering<double[]> clustering, LogisticRegression[] classifiers){
        super();
        this.trainingData = trainingData;
        this.clustering = clustering;
        this.classifiers = classifiers;
    }

    @Override
    public AttributeDataset apply(double[][] data) {
        AttributeDataset resultDataset = new AttributeDataset(TITLE, this.trainingData.attributes(), this.trainingData.response());
        for(int record=0; record<data.length; record++){
            int clusterLabel = clustering.predict(data[record]);
            int y = classifiers[clusterLabel].predict(data[record]);
            resultDataset.add(data[record], y);
        }
        return resultDataset;
    }

}
