package com.senacor.hd11.process;

import com.senacor.hd11.model.User;
import com.senacor.hd11.model.UserApplication;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Ralph Winzinger, Senacor
 */
public class ApplicationProcessTest {
    @Test
    public void testApplicationProcess() throws Exception {
        ClientResponse response = null;

        response = startProcess("diddi-"+System.currentTimeMillis(), "Dieter", "Ditfurt", "secret");
        String newLocation = response.getHeaders().getFirst("location");
        assertEquals("response was not '201 created'", ClientResponse.Status.CREATED.getStatusCode(), response.getStatus());

        System.err.println("process started!");

        ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
        client.resource("http://localhost:18080/rest-1.0/rest").path("/applications").queryParam("owner", "Fix").get(ClientResponse.class);

        System.err.println("liste für fix geholt");

        config = new DefaultClientConfig();
		client = Client.create(config);
		WebResource service = client.resource(newLocation);
        response = service.get(ClientResponse.class);
        assertEquals("response was not '200 ok'", ClientResponse.Status.OK.getStatusCode(), response.getStatus());

        System.err.println("userapplication geholt");

        UserApplication returnedApplication = response.getEntity(UserApplication.class);
        returnedApplication.setState(UserApplication.ApplicationState.ACCEPTED);

        response = service.queryParam("owner", "Fix").put(ClientResponse.class, returnedApplication);
        assertEquals("application was not updated", ClientResponse.Status.ACCEPTED.getStatusCode(), response.getStatus());

        System.err.println("ua als fix geändert");

        response = client.resource("http://localhost:18080/rest-1.0/rest").path("/applications").queryParam("owner", "Foxi").get(ClientResponse.class);
        assertEquals("response was not '200 ok'", ClientResponse.Status.OK.getStatusCode(), response.getStatus());

        System.err.println("liste für foxi geholt");

        response = service.queryParam("owner", "Foxi").put(ClientResponse.class, returnedApplication);
        assertEquals("application was not updated", ClientResponse.Status.ACCEPTED.getStatusCode(), response.getStatus());

        System.err.println("ua als foxi geändert");
    }

    private ClientResponse startProcess(String username, String firstname, String lastname, String password) {
        UserApplication userApplication = new UserApplication();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        userApplication.setUser(user);

        ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		return client.resource("http://localhost:18080/rest-1.0/rest").path("/applications").post(ClientResponse.class, userApplication);
    }
}
