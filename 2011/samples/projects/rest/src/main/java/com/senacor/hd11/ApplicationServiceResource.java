package com.senacor.hd11;

import com.hd11.process.ihu.Ihu;
import com.hd11.process.ihu.TaskServerCommunication;
import com.senacor.hd11.listener.FinishedAnmeldungBearbeitenProcess;
import com.senacor.hd11.model.User;
import com.senacor.hd11.model.UserApplication;
import com.senacor.hd11.persistence.NoSuchUserException;
import com.senacor.hd11.persistence.PersistenceService;
import com.senacor.hd11.persistence.UserExistsException;
import com.sun.jersey.api.JResponse;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Ralph Winzinger, Senacor
 *
 * GET  /applications liefert alle Anmeldungen, Filterung Ÿber "state" == "PENDING, ACCEPTED, REJECTED, REWORK"
 * POST /applications legt neue Anmeldung unter /applications/{appuuid} an und einen User unter /users/{username} im state pending
 * GET  /applications/{appuuid} liefert die Anmeldung
 * PUT  /applications/{appuuid} aktualisiert die Anmeldung, Achtung! Nur falls state==pending
 */
@Path("/applications")
@Component
public class ApplicationServiceResource {
    @Context
    private UriInfo uriInfo;

    private TaskServerCommunication tsc = new TaskServerCommunication();
    private Ihu ihu = new Ihu(new FinishedAnmeldungBearbeitenProcess());

    @Path("/ping")
    @GET
    @Produces("text/plain")
    public String ping() {
        return "pong";
    }

    @Path("/")
    @POST
    @Produces("application/xml")
    public void createApplication(UserApplication userApplication) {
        String appUUID = UUID.randomUUID().toString();
        userApplication.setAppuuid(appUUID);
        userApplication.setState(UserApplication.ApplicationState.PENDING);
        userApplication.getUser().setState(User.UserState.PENDING);

        ihu.startProcess(userApplication);

        try {
            PersistenceService.getInstance().addApplication(appUUID, userApplication);
            // todo: create user (pending)
            URI uri = uriInfo.getAbsolutePathBuilder().path(appUUID).build();
            throw new WebApplicationException(
                    Response.status(Response.Status.CREATED)
                            .header("location", uri.toString())
                            .entity(appUUID)
                            .build()
            );
        } catch (UserExistsException e) {
            throw new WebApplicationException(
                    Response.status(Response.Status.CONFLICT)
                            .entity("user already exists or is pending")
                            .build()
            );
        }

    }

    @Path("/{uuid}")
    @GET
    @Produces("application/xml")
    public UserApplication readApplication(@PathParam("uuid") String uuid) {
        try {
            return PersistenceService.getInstance().getApplication(uuid);
        } catch (NoSuchUserException e) {
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity("no such user")
                            .build()
            );
        }
    }

    @Path("/")
    @GET
    @Produces("application/xml")
    public JResponse<List<UserApplication>> readPendingApplications(@QueryParam("owner") String owner) {
        if (owner == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("'owner' must be set")
                            .build()
            );
        }

        List<UserApplication> userApplications = tsc.getTasksByOwner(owner);
        return JResponse.ok(userApplications).build();
    }

    @Path("/{uuid}")
    @PUT
    @Produces("application/xml")
    public void updateApplication(@PathParam("uuid") String uuid, UserApplication userApplication, @QueryParam("owner") String owner) {
        if ((owner == null) || (uuid == null)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("'owner' and 'uuid' must be set")
                            .build()
            );
        }

        tsc.startAndCompleteTaskForUUID(owner, uuid, userApplication);

        throw new WebApplicationException(
                Response.status(Response.Status.ACCEPTED)
                        .entity("entry updated")
                        .build()
        );
    }
}
