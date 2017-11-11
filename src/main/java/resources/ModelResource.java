package resources;

import database.DBManager;
import database.FileManager;
import exception.CreatingFileException;
import learning.ClusterType;
import learning.DataModelMerger;
import learning.ModelApplier;
import learning.ModelTrainer;
import learning.interfaces.IApplier;
import learning.interfaces.IMerger;
import learning.interfaces.ITrainer;
import model.Message;
import model.interfaces.IMessage;
import model.interfaces.IModel;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.w3c.dom.Attr;
import smile.classification.LogisticRegression;
import smile.clustering.PartitionClustering;
import smile.data.AttributeDataset;
import utils.CsvBuilder;
import utils.FileType;
import utils.Parser;
import utils.interfaces.IParser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Andrea De Castri on 08/11/2017.
 *
 */
@Path("model")
public class ModelResource {

    @Path("create")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({MediaType.APPLICATION_JSON})
    public Response createModel(@FormDataParam("file") InputStream uploadedInputStream,
                                @FormDataParam("file") FormDataContentDisposition fileDetail,
                                @FormDataParam("modelName") String modelName, @FormDataParam("description") String description,
                                @FormDataParam("responseIndex") int responseIndex){
        // TODO Async is better
        try {
            FileType fileType = FileType.getFileTypeFromExtension(fileDetail.getFileName());

            // Parse the file and retrieve data for training
            IParser parser = new Parser(uploadedInputStream, fileType, responseIndex);
            AttributeDataset dataset = parser.parse();

            // Retrieve columns name of the training file
            String independentVariablesLabels = parser.getIndependentVariablesToString();
            String dependentVariableLabel = parser.getDependentVariable();

            double[][] independentVariables = dataset.toArray(new double[dataset.size()][]);
            int[] dependentVariables = dataset.toArray(new int[dataset.size()]);

            // Training a model with a specific clustering and Logistic regression
            ITrainer trainer = new ModelTrainer(independentVariables, dependentVariables, ClusterType.XMEANS);
            PartitionClustering<double[]> clustering = trainer.getClustering();
            LogisticRegression[] classifiers = trainer.getClassifiers();

            // Create new model id and save it into a database
            String modelID = UUID.randomUUID().toString();
            DBManager dbmanager = new DBManager();
            Connection connection = dbmanager.getConnection();
            dbmanager.saveNewModel(connection, modelID, modelName, "clinic-00", description,
                    independentVariablesLabels, dependentVariableLabel);

            connection.close();

            // Save the model on file
            FileManager.saveNewModel(modelID, dataset, clustering, classifiers);

        } catch (IOException | SQLException | ParseException | CreatingFileException e) {
            e.printStackTrace();
            IMessage message = new Message(true, e.getMessage());
            return Response.ok().entity(message).build();
        }

        IMessage message = new Message(false, "Done");
        return Response.ok().entity(message).build();
    }

    @Path("upload")
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response uploadModel(){
        // TODO caricare modelli già creati (RDS, PMML, ...)
        return null;
    }

    @Path("{model-id}/apply")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON})
    public Response apply(@FormDataParam("file") InputStream inputStream, @FormDataParam("file") FormDataContentDisposition fileDetail,
                          @PathParam("model-id") String modelID){

        byte[] csvFile = null;

        try {

            // Retrieve model information from database
            DBManager manager = new DBManager();
            Connection connection = manager.getConnection();
            IModel model = manager.getModelFromID(connection, modelID);
            connection.close();

            // Parse testing data
            FileType fileType = FileType.getFileTypeFromExtension(fileDetail.getFileName());
            Parser parser = new Parser(inputStream, fileType);

            AttributeDataset datasetTestingData = parser.parse();
            AttributeDataset datasetTrainingData = FileManager.getTrainingData(modelID);

            List<String> independentLabels = parser.getIndependentVariables();
            int numIndependentVariable = independentLabels.size();
            double[][] testingData = datasetTestingData.toArray(new double[datasetTestingData.size()][]);

            if(testingData.length < 1 || testingData[0].length != numIndependentVariable){
                IMessage message = new Message(true, "No data or wrong independent variable!");
                return Response.ok(message, MediaType.APPLICATION_JSON).build();
            }

            PartitionClustering<double[]> clustering = FileManager.getClusterOfModel(modelID);
            LogisticRegression[] classifiers = FileManager.getClassifiersOfModel(modelID);

            IApplier applier = new ModelApplier(datasetTrainingData, clustering, classifiers);
            AttributeDataset resultDataset = applier.apply(testingData);

            csvFile = CsvBuilder.createCsv(independentLabels, resultDataset.toArray(new double[testingData.length][]),
                    resultDataset.toArray(new int[testingData.length]));

            if(model.isTrainable() && model.isOnlineTrainable()){
                // Merge the dataset
                IMerger merger = new DataModelMerger();
                AttributeDataset mergedDataset = merger.merge(datasetTrainingData, resultDataset);

                double[][] xMerged = mergedDataset.toArray(new double[mergedDataset.size()][]);
                int[] yMerged = mergedDataset.toArray(new int[mergedDataset.size()]);

                // Retrain the model with merged data
                ITrainer mergedTrainer = new ModelTrainer(xMerged, yMerged, ClusterType.XMEANS);
                PartitionClustering<double[]> mergedClustering = mergedTrainer.getClustering();
                LogisticRegression[] mergedClassifiers = mergedTrainer.getClassifiers();

                // Save the model on file
                FileManager.replaceModel(modelID, mergedDataset, mergedClustering, mergedClassifiers);
                // TODO aggiornare data di last update
            }

        } catch (IOException | ParseException | SQLException | CreatingFileException e) {
            e.printStackTrace();
            IMessage message = new Message(true, e.getMessage());
            return Response.ok(message, MediaType.APPLICATION_JSON).build();
        }

        return Response.ok(getFileOutputStream(csvFile), MediaType.APPLICATION_OCTET_STREAM).build();
    }

    @Path("{model-id}/retrain")
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@FormDataParam("file") InputStream uploadedInputStream,
                           @FormDataParam("file") FormDataContentDisposition fileDetail,
                           @PathParam("model-id") String modelID){


        return Response.ok().build();
    }

    @Path("{model-id}/onlineTraining")
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response onlineTraining(@PathParam("model-id") String modelID, @FormParam("onlineTrainable") boolean onlineTrainable){
        try {
            DBManager manager = new DBManager();
            Connection connection = manager.getConnection();

            manager.updateOnlineTraining(connection, modelID, onlineTrainable);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            IMessage message = new Message(true, e.getMessage());
            return Response.ok().entity(message).build();
        }

        IMessage message = new Message(false, "Done");
        return Response.ok().entity(message).build();
    }

    @Path("{model-id}/globalAccess")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateGlobalAccess(@PathParam("model-id") String modelID, @FormParam("globalAccess") boolean globalAccess){
        try {
            DBManager manager = new DBManager();
            Connection connection = manager.getConnection();

            manager.updateGlobalAccess(connection, modelID, globalAccess);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            IMessage message = new Message(true, e.getMessage());
            return Response.ok().entity(message).build();
        }

        IMessage message = new Message(false, "Done");
        return Response.ok().entity(message).build();
    }

    @Path("{model-id}/snapshot")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response snapshotModel(@PathParam("model-id") String oldModelID){
        try {
            DBManager manager = new DBManager();
            Connection connection = manager.getConnection();

            // Retrieve the "old" model from database
            IModel model = manager.getModelFromID(connection, oldModelID);

            // Create new model id
            String newModelID = UUID.randomUUID().toString();

            // Save new model with the same features
            manager.saveNewModel(connection, newModelID, model.getName(), model.getProviderId(), model.getDescription(),
                    model.getIndependentVariables(), model.getDependentVariable());

            // Set trainable = false and onlineTrainable = false to oldModel
            manager.snapshotModel(connection, oldModelID);

            // Copy models and data in another folder
            FileManager.snapshotModelFolder(oldModelID, newModelID);

            connection.close();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            IMessage message = new Message(true, e.getMessage());
            return Response.ok().entity(message).build();
        }

        IMessage message = new Message(false, "Done");
        return Response.ok().entity(message).build();
    }

    @Path("{model-id}/download")
    @GET
    //@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response downloadModel(@PathParam("model-id") String oldModelID){
        // TODO zip and download
        /*return Response
                .ok(FileUtils.readFileToByteArray(file))
                .type("application/zip")
                .header("Content-Disposition", "attachment; filename=\"filename.zip\"")
                .build();*/
        IMessage message = new Message(false, "Done");
        return Response.ok().entity(message).build();
    }

    @Path("{model-id}/delete")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteModel(@PathParam("model-id") String modelID) {
        try {
            DBManager manager = new DBManager();
            Connection connection = manager.getConnection();
            manager.deleteModel(connection, modelID);

            FileManager.deleteDirectory(modelID);

            connection.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            IMessage message = new Message(true, e.getMessage());
            return Response.ok().entity(message).build();
        }

        IMessage message = new Message(false, "Done");
        return Response.ok().entity(message).build();
    }

    private StreamingOutput getFileOutputStream(byte[] excelBytes) {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
                out.write(excelBytes);
            }
        };
    }

}
