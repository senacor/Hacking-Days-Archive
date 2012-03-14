package com.senacor.hd11;

import com.senacor.hd11.model.User;
import com.senacor.hd11.model.UserApplication;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoaderListener;

import static org.junit.Assert.*;

/**
 * Ralph Winzinger, Senacor
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class UserServiceTest extends JerseyTest {
    private final static String HOST = "";

    public UserServiceTest() throws TestContainerException {
        super(new WebAppDescriptor.Builder("com.senacor.hd11")
        .contextParam("contextConfigLocation", "classpath:applicationContext.xml")
        .initParam("com.sun.jersey.spi.container.ContainerRequestFilters", "com.sun.jersey.api.container.filter.LoggingFilter")
        .initParam("com.sun.jersey.spi.container.ContainerResponseFilters", "com.sun.jersey.api.container.filter.LoggingFilter")
        .initParam("com.sun.jersey.config.feature.logging.DisableEntitylogging", "false")
        .servletClass(SpringServlet.class)
        .contextListenerClass(ContextLoaderListener.class)
        .build());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        ClientResponse response = resource().path("/users").get(ClientResponse.class);
        assertEquals("response was not '200 ok'", ClientResponse.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Ignore
    public void testUserApplication() {
        UserApplication userApplication = new UserApplication();
        User user = new User();
        user.setUsername("mmustermann-1");
        user.setPassword("secret");
        user.setFirstname("Michael");
        user.setLastname("Mustermann");
        userApplication.setUser(user);

        ClientResponse response = resource().path("/users/application").entity(userApplication).post(ClientResponse.class, userApplication);
        assertEquals("response was not '201 created'", ClientResponse.Status.CREATED.getStatusCode(), response.getStatus());
        String newLocation = response.getHeaders().getFirst("location");
        String appUUID = response.getEntity(String.class);

        response = resource().path("/users/application").entity(userApplication).post(ClientResponse.class, userApplication);
        assertEquals("response was not '409 conflict'", ClientResponse.Status.CONFLICT.getStatusCode(), response.getStatus());

        ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(newLocation);
        response = service.get(ClientResponse.class);
        assertEquals("response was not '200 ok'", ClientResponse.Status.OK.getStatusCode(), response.getStatus());

        UserApplication returnedApplication = response.getEntity(UserApplication.class);
        assertEquals("applications do not match", userApplication.getUser().getUsername(), returnedApplication.getUser().getUsername());

        returnedApplication.getUser().setUsername("jndoe");
        response = service.put(ClientResponse.class, returnedApplication);
        assertEquals("application was not updated", ClientResponse.Status.ACCEPTED.getStatusCode(), response.getStatus());

        response = service.get(ClientResponse.class);
        assertEquals("response was not '200 ok'", ClientResponse.Status.OK.getStatusCode(), response.getStatus());

        UserApplication updatedApplication = response.getEntity(UserApplication.class);
        assertEquals("updated applications do not match", returnedApplication.getUser().getUsername(), updatedApplication.getUser().getUsername());

        response = resource().path("/users/verification").path("jndoe").queryParam("pwd", "secret").get(ClientResponse.class);
        assertEquals("response was not '401 unauthorized' (for pending application)", ClientResponse.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());

        response = resource().path("/users/verification").queryParam("uuid", appUUID).post(ClientResponse.class);
        assertEquals("response was not '201 created'", ClientResponse.Status.CREATED.getStatusCode(), response.getStatus());

        response = resource().path("/users/verification").path("jndoe").queryParam("pwd", "secret").get(ClientResponse.class);
        assertEquals("response was not '200 ok'", ClientResponse.Status.OK.getStatusCode(), response.getStatus());

        response = resource().path("/users/verification").path("notExisting").queryParam("pwd", "secret").get(ClientResponse.class);
        assertEquals("response was not '404 not found'", ClientResponse.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        response = resource().path("/users/verification").path("jndoe").queryParam("pwd", "wrongPass").get(ClientResponse.class);
        assertEquals("response was not '401 unauthorized'", ClientResponse.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testLoginOk() throws Exception {
        String username = "jdoe";

        ClientResponse response = resource().path(HOST).path("users").path(username).path("authorization").queryParam("mock", "force-ok").get(ClientResponse.class);
        assertEquals("response was not '200 ok'", ClientResponse.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testLoginNok() throws Exception {
        String username = "jdoe";

        ClientResponse response = resource().path(HOST).path("users").path(username).path("authorization").queryParam("mock", "force-nok").get(ClientResponse.class);
        assertEquals("response was not '401 unauthorized'", ClientResponse.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }
}
