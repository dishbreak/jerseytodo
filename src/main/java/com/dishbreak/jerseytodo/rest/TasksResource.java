package com.dishbreak.jerseytodo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello")
public class TasksResource {
    @GET
    @Path("/")
    public String ping() {
        return "pong!";
    }
}
