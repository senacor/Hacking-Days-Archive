package com.senacor.hd11;

import com.senacor.hd11.model.User;
import com.senacor.hd11.persistence.PersistenceService;
import com.sun.jersey.api.JResponse;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Ralph Winzinger, Senacor
 * <p/>
 * POST /users/{username} legt einen neuen Benutzer an, state==pending
 * PUT  /users/{username} aktualisiert den Benutzer
 * GET  /users/{username}/authorization?pwd=xy prüft ein Passwort
 */
@Path("/users")
@Component
public class UserServiceResource {
    @Context
    private UriInfo uriInfo;

    @Path("/ping")
    @GET
    @Produces("text/plain")
    public String ping() {
        return "pong";
    }

    @Path("/")
    @GET
    @Produces("application/xml")
    public JResponse<List<User>> readUsers(@QueryParam("state") String state) {
        List<User> users = PersistenceService.getInstance().getUsers();

        return JResponse.ok(users).build();
    }

    @Path("/{username}/authorization")
    @GET
    @Produces("application/xml")
    public String checkAuthorization(@PathParam("username") String username, @QueryParam("pwd") String password, @QueryParam("mock") String mock) {
        if ("force-ok".equalsIgnoreCase(mock)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.OK)
                            .header("mock-service", "true")
                            .entity("<auth-token>")
                            .build()
            );
        } else if ("force-nok".equalsIgnoreCase(mock)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .header("mock-service", "true")
                            .entity("unauthorized")
                            .build()
            );
        }

        User user = PersistenceService.getInstance().getUser(username);
        if (user == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .build()
            );
        } else {
            if (user.getState() == User.UserState.PENDING) {
                throw new WebApplicationException(
                        Response.status(Response.Status.UNAUTHORIZED)
                                .entity("activation is still pending")
                                .build()
                );

            } else {
                if (!user.getPassword().equals(password)) {
                    throw new WebApplicationException(
                            Response.status(Response.Status.UNAUTHORIZED)
                                    .build()
                    );
                }
            }
        }

        throw new WebApplicationException(
                Response.status(Response.Status.OK).entity("toll gemacht")
                        .build()
        );
    }
}