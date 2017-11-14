package main;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import resources.ModelResource;
import resources.ModelsResource;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Andrea De Castri on 08/11/2017.
 *
 * Starts the web server.
 */
public class Main {

    private static final URI BASE_URI = URI.create("http://localhost:8080/");

    public static void main(String[] args) {
        try {
            System.out.println("Connecare-learning starts!");

            Set<Class<?>> set = new HashSet<>();
            set.add(ModelResource.class);
            set.add(ModelsResource.class);

            final ResourceConfig resourceConfig = new ResourceConfig(set);
            resourceConfig.register(MultiPartFeature.class);
            final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI,resourceConfig,false);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> server.shutdownNow()));
            server.start();

            Thread.currentThread().join();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

}
