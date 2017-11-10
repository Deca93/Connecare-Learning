package client;

import smile.clustering.XMeans;
import smile.data.AttributeDataset;
import smile.data.NominalAttribute;
import smile.data.parser.DelimitedTextParser;
import smile.mds.MDS;
import smile.plot.PlotCanvas;
import smile.plot.ScatterPlot;
import utils.Matrix;
import view.MainFrame;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Andrea De Castri on 09/11/2017.
 *
 */
public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        //testExample("src/test/data.csv", 2);
        testExample("src/test/note_auth.csv", 4);
    }

    private static void testExample(String path, int responseIndex) throws IOException, ParseException {
        DelimitedTextParser parser = new DelimitedTextParser();
        parser.setColumnNames(true);
        parser.setDelimiter(",");
        parser.setResponseIndex(new NominalAttribute("Label"),responseIndex);

        AttributeDataset dataset = parser.parse("Parser", new FileInputStream(path));

        double[][] x = dataset.toArray(new double[dataset.size()][]);
        int[] y = dataset.toArray(new int[dataset.size()]);

        XMeans xMeans = new XMeans(x, 5);

        double[][] centroids = xMeans.centroids();
        int numClusters = xMeans.getNumClusters();
        int[] labels = xMeans.getClusterLabel();
        int[] clustersSize = xMeans.getClusterSize();

        drawScatterPlot(x, labels, -1, centroids);

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

            drawScatterPlot(x1, y1, i, null);
        }
    }

    private static void drawScatterPlot(double[][] data, int[] labels, int index, double[][] centroids){
        double[][] coordinates = null;
        if(data[0].length > 3){
            double[][] proximity = Matrix.proximityMatrix(data);
            MDS mds = new MDS(proximity);
            coordinates = mds.getCoordinates();
        } else {
            coordinates = data;
        }


        PlotCanvas p = ScatterPlot.plot(coordinates, labels, null, new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN});
        if(centroids != null){
            double[][] clusterCenters = null;
            if(centroids[0].length > 3){
                double[][] proximity = Matrix.proximityMatrix(centroids);
                MDS mds = new MDS(proximity);
                clusterCenters = mds.getCoordinates();
            } else {
                clusterCenters = centroids;
            }
            p.points(clusterCenters, '@');
        }

        MainFrame f = new MainFrame("Classificazione cluster " + index);
        f.getContentPane().add(p);
    }

}
