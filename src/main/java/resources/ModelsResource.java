package resources;

import model.Model;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Andrea De Castri on 08/11/2017.
 *
 */
@Path("models")
public class ModelsResource {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getModels(){
        return "CIAO";
    }

}
