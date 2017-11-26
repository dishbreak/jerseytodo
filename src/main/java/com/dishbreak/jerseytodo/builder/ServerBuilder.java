package com.dishbreak.jerseytodo.builder;

import com.dishbreak.jerseytodo.module.TasksModule;
import com.dishbreak.jerseytodo.rest.TasksResource;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletModule;
import com.squarespace.jersey2.guice.BootstrapUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.DispatcherType;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;

public class ServerBuilder {

    private static final int DEFAULT_PORT = 8080;

    public static Server build() {
        ServiceLocator locator = BootstrapUtils.newServiceLocator();
        Injector injector = BootstrapUtils.newInjector(locator, Arrays.asList(
                new ServletModule(), new TasksModule()));

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

        FilterHolder filterHolder = new FilterHolder(GuiceFilter.class);
        contextHandler.addFilter(filterHolder, "/*",
                EnumSet.allOf(DispatcherType.class));

        Server server = new Server(serverPort);
        server.setHandler(contextHandler);

        return server;
    }

}
