package learning;

import com.thoughtworks.xstream.XStream;
import model.Pair;
import smile.classification.LogisticRegression;
import smile.clustering.GMeans;
import smile.clustering.XMeans;
import smile.data.AttributeDataset;
import smile.data.NominalAttribute;
import smile.data.parser.DelimitedTextParser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

/**
 * Created by Andrea De Castri on 08/11/2017.
 *
 */
public class Trainer {

    private Trainer(){
        super();
    }

    public static Pair<XMeans,LogisticRegression[]> train(double[][] x, int[] y) throws IOException, ParseException {

        XMeans xMeans = new XMeans(x, 100);

        int numClusters = xMeans.getNumClusters();
        int[] labels = xMeans.getClusterLabel();
        int[] clustersSize = xMeans.getClusterSize();

        LogisticRegression[] logisticRegressions = new LogisticRegression[numClusters];

        for(int i=0; i<numClusters; i++){
            double[][] x1 = new double[clustersSize[i]][];
            int[] y1 = new int[clustersSize[i]];
            for(int j=0, k=0; j<x.length && k<clustersSize[i]; j++){
                if(labels[j] == i){
                    x1[k] = x[j];
                    y1[k] = y[j];
                    k++;
                }
            }
            logisticRegressions[i] = new LogisticRegression(x1, y1);
        }

        return new Pair<>(xMeans, logisticRegressions);
    }

}
