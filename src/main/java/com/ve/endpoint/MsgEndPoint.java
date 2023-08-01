package com.ve.endpoint;

import com.ve.domain.Metier;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/msg")
public class MsgEndPoint {
           
    @Inject
    private Metier controleur;

    @GET
    @Path("/{msg}")
    @Produces(MediaType.TEXT_PLAIN)
    public String add(String msg) {
        controleur.sendMessage(msg);
        return msg;
    }

}
