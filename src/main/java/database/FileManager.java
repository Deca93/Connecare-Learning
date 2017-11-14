package database;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.extended.JavaClassConverter;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.security.AnyTypePermission;
import exception.CreatingFileException;
import org.apache.commons.io.FileUtils;
import smile.classification.LogisticRegression;
import smile.clustering.PartitionClustering;
import smile.clustering.XMeans;
import smile.data.AttributeDataset;

import java.io.*;

/**
 * Created by Andrea De Castri on 09/11/2017.
 *
 */
public class FileManager {

    private static final String MODELS_DIR = "C:" + File.separator + "Users" + File.separator + "Andrea" + File.separator +
            "Desktop" + File.separator + "Connecare"+ File.separator + "Models";

    public static void saveNewModel(String modelID, AttributeDataset dataset, PartitionClustering<double[]> clustering,
                                    LogisticRegression[] logisticRegressions) throws CreatingFileException, IOException {

        boolean dirCreated = createModelDir(modelID);
        if(!dirCreated){
            throw new CreatingFileException();
        }

        OutputStream osDataset = new FileOutputStream(MODELS_DIR + File.separator + modelID + File.separator + "dataset.xml");
        OutputStream osCluster = new FileOutputStream(MODELS_DIR + File.separator + modelID + File.separator + "cluster.xml");
        OutputStream osClassifiers = new FileOutputStream(MODELS_DIR + File.separator + modelID + File.separator + "classifiers.xml");

        XStream xStream = new XStream();
        xStream.toXML(dataset, osDataset);
        xStream.toXML(clustering, osCluster);
        xStream.toXML(logisticRegressions, osClassifiers);

        osDataset.close();
        osCluster.close();
        osClassifiers.close();
    }

    public static void replaceModel(String modelID, AttributeDataset dataset, PartitionClustering<double[]> clustering,
                               LogisticRegression[] logisticRegressions) throws CreatingFileException, FileNotFoundException{

        OutputStream osDataset = new FileOutputStream(MODELS_DIR + File.separator + modelID + File.separator + "dataset.xml");
        OutputStream osCluster = new FileOutputStream(MODELS_DIR + File.separator + modelID + File.separator + "cluster.xml");
        OutputStream osClassifiers = new FileOutputStream(MODELS_DIR + File.separator + modelID + File.separator + "classifiers.xml");

        XStream xStream = new XStream();
        xStream.toXML(dataset, osDataset);
        xStream.toXML(clustering, osCluster);
        xStream.toXML(logisticRegressions, osClassifiers);
    }

    public static PartitionClustering<double[]> getClusterOfModel(String modelID){
        XStream xStream = new XStream();
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[]{XMeans.class});
        File file = new File(MODELS_DIR + File.separator + modelID + File.separator + "cluster.xml");
        PartitionClustering<double[]> xMeans = (PartitionClustering<double[]>) xStream.fromXML(file);
        return xMeans;
    }

    public static LogisticRegression[] getClassifiersOfModel(String modelID){
        XStream xStream = new XStream();
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[]{LogisticRegression.class});
        File file = new File(MODELS_DIR + File.separator + modelID + File.separator + "classifiers.xml");
        LogisticRegression[] classifiers = (LogisticRegression[]) xStream.fromXML(file);
        return classifiers;
    }

    public static AttributeDataset getTrainingData(String modelID){
        XStream xStream = new XStream();
        XStream.setupDefaultSecurity(xStream);
        xStream.addPermission(AnyTypePermission.ANY);
        //xStream.allowTypes(new Class[]{AttributeDataset.class});
        File file = new File(MODELS_DIR + File.separator + modelID + File.separator + "dataset.xml");
        AttributeDataset dataset = (AttributeDataset) xStream.fromXML(file);
        return dataset;
    }

    public static void snapshotModelFolder(String oldModelID, String newModelID) throws IOException {
        File f1 = new File(MODELS_DIR + File.separator + oldModelID);
        File f2 = new File(MODELS_DIR + File.separator + newModelID);

        FileUtils.copyDirectory(f1, f2);
    }

    public static void deleteDirectory(String modelID) throws IOException {
        File directory = new File(MODELS_DIR + File.separator + modelID);
        FileUtils.deleteDirectory(directory);
    }

    private static boolean createModelDir(String modelID){
        File newDirectory = new File(MODELS_DIR + File.separator + modelID);
        return newDirectory.mkdir();
    }

    public static File getClusteringFileOfModel(String modelID){
        return new File(MODELS_DIR + File.separator + modelID + File.separator + "cluster.xml");
    }

    public static File getClassifiersFileOfModel(String modelID){
        return new File(MODELS_DIR + File.separator + modelID + File.separator + "classifiers.xml");
    }

}
