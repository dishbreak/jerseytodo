package com.dishbreak.jerseytodo;

import com.dishbreak.jerseytodo.builder.ServerBuilder;
import org.eclipse.jetty.server.Server;

public class Application {

    public static void main(String[] args) {
        Server server = ServerBuilder.build();

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            server.destroy();
        }
    }
}
