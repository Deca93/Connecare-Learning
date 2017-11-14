package resources;

import database.DBManager;
import model.Model;
import model.interfaces.IModel;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrea De Castri on 08/11/2017.
 *
 */
@Path("models")
public class ModelsResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getModels(){
        List<Model> result = null;
        try {
            DBManager dbmanager = new DBManager();
            Connection connection = dbmanager.getConnection();
            result = dbmanager.getModels(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return Response.ok(result, MediaType.APPLICATION_JSON)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "Access-Control-Allow-Origin, Content-Type, Access-Control-Allow-Headers")
                .build();
    }

}
