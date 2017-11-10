package resources;

import database.DBManager;
import database.FileManager;
import exception.CreatingFileException;
import learning.Trainer;
import model.Message;
import model.Pair;
import model.interfaces.IMessage;
import model.interfaces.IModel;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import smile.classification.LogisticRegression;
import smile.clustering.XMeans;
import smile.data.AttributeDataset;
import utils.FileType;
import utils.Parser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
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
            AttributeDataset dataset = Parser.retrieveData(uploadedInputStream, fileType, responseIndex);

            double[][] xData = dataset.toArray(new double[dataset.size()][]);
            int[] yData = dataset.toArray(new int[dataset.size()]);

            String xVariables = "";
            String yVariable = dataset.response().getName().replaceAll("\"", "");
            for(int i=0; i<dataset.attributes().length; i++){
                xVariables += dataset.attributes()[i].getName();
                if(i<dataset.attributes().length - 1){
                    xVariables += ", ";
                }
            }
            xVariables = xVariables.replaceAll("\"", "");

            Pair<XMeans,LogisticRegression[]> result = Trainer.train(xData, yData);

            String modelID = UUID.randomUUID().toString();
            DBManager manager = new DBManager();
            Connection connection = manager.getConnection();
            manager.saveNewModel(connection, modelID, "modelName", "clinic-00", "description",
                    xVariables, yVariable);

            connection.close();

            FileManager.saveNewModel(modelID, dataset, result.getFirst(), result.getSecond());

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
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response apply(){
        return null;
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

}
