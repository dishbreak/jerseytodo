package com.dishbreak.jerseytodo;

import com.dishbreak.jerseytodo.rest.TasksResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.Optional;

public class Application {

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        int serverPort = DEFAULT_PORT;
        Optional<String> portVar = Optional.ofNullable(System.getenv("PORT"));
        if (portVar.isPresent()) {
            try {
                serverPort = Integer.parseInt(portVar.get());
            } catch (NumberFormatException e) {
                System.err.println("Unsupported value for PORT: '" + portVar.get() + "'");
            }
        }
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages(TasksResource.class.getPackage().getName());
        resourceConfig.register(JacksonFeature.class);


        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder servletHolder = new ServletHolder(servletContainer);
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        contextHandler.addServlet(servletHolder, "/*");
        Server server = new Server(serverPort);
        server.setHandler(contextHandler);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
