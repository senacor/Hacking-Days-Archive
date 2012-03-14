package com.senacor.hd11;

import com.senacor.hd11.messages.MessageService;
import com.senacor.hd11.model.Message;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;

/**
 * Ralph Winzinger, Senacor
 *
 * POST /messages/{username} legt eine neue Nachricht unter /messages/{username}/{msguuid} an (in das System darf nur gelangen, was von hier wieder zurückgegeben wird!)
 * GET  /messages liefert alle Nachrichten, Filterkriterien?
 * GET  /messages/{username} liefert alle Nachrichten des Users, Filterkriterien
 * PUT  /messages/{username}/{msguuid} aktualisiert die Nachricht, Push & Replace im Chat-System?
 */
@Path("/messages")
@Component
public class MessageServiceResource {
    @Context
    private UriInfo uriInfo;

    @Path("/ping")
    @GET
    @Produces("text/plain")
    public String ping() {
        return "pong";
    }

    /**
     * 201 created, falls alles ok
     * TODO: 401 unauthorized, falls user.state == muted
     */
    @Path("/{username}")
    @POST
    @Produces("application/xml")
    public Message createMessage(String text, @PathParam("username") String username) {
        String msgUUID = UUID.randomUUID().toString();

        Message message = new Message();
        message.setMsguuid(msgUUID);
        message.setUsername(username);
        message.setText(text);

        URI uri = uriInfo.getAbsolutePathBuilder().path(msgUUID).build();
        throw new WebApplicationException(
            Response.status(Response.Status.CREATED)
                    .header("location", uri.toString())
                    .header("mock-service", "true")
                    .entity(MessageService.getInstance().createMessage(message))
                    .build()
        );
    }
}
