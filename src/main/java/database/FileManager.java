package database;

import com.thoughtworks.xstream.XStream;
import exception.CreatingFileException;
import org.apache.commons.io.FileUtils;
import smile.classification.LogisticRegression;
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

    public static void saveNewModel(String modelID, AttributeDataset dataset, XMeans xMeans,
                                    LogisticRegression[] logisticRegressions) throws CreatingFileException, FileNotFoundException {

        boolean dirCreated = createModelDir(modelID);
        if(!dirCreated){
            throw new CreatingFileException();
        }

        OutputStream osDataset = new FileOutputStream(MODELS_DIR + File.separator + modelID + File.separator + "dataset.xml");
        OutputStream osCluster = new FileOutputStream(MODELS_DIR + File.separator + modelID + File.separator + "cluster.xml");
        OutputStream osClassification = new FileOutputStream(MODELS_DIR + File.separator + modelID + File.separator + "classification.xml");

        XStream xStream = new XStream();
        xStream.toXML(dataset, osDataset);
        xStream.toXML(xMeans, osCluster);
        xStream.toXML(logisticRegressions, osClassification);
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

}
