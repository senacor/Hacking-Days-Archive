package com.senacor.hd11;

import com.senacor.hd11.model.User;
import com.senacor.hd11.model.UserApplication;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Ralph Winzinger, Senacor
 */
public class ApplicationServiceTest extends JerseyTest {
    private final static String HOST = "";

    public ApplicationServiceTest() {
        super(new WebAppDescriptor.Builder("com.senacor.hd11")
                .contextParam("contextConfigLocation", "classpath:applicationContext.xml")
                .initParam("com.sun.jersey.spi.container.ContainerRequestFilters", "com.sun.jersey.api.container.filter.LoggingFilter")
                .initParam("com.sun.jersey.spi.container.ContainerResponseFilters", "com.sun.jersey.api.container.filter.LoggingFilter")
                .initParam("com.sun.jersey.config.feature.logging.DisableEntitylogging", "false")
                .servletClass(SpringServlet.class)
                .contextListenerClass(ContextLoaderListener.class)
                .build());
    }

    @Before
    public void checkServices() throws Exception {
        ClientResponse response = resource().path(HOST).path("/messages/ping").get(ClientResponse.class);
        assertEquals("message service is not online", ClientResponse.Status.OK.getStatusCode(), response.getStatus());

        response = resource().path(HOST).path("/users/ping").get(ClientResponse.class);
        assertEquals("user service is not online", ClientResponse.Status.OK.getStatusCode(), response.getStatus());

        response = resource().path(HOST).path("/applications/ping").get(ClientResponse.class);
        assertEquals("application service is not online", ClientResponse.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUserApplication() {
        UserApplication userApplication = new UserApplication();
        User user = new User();
        user.setUsername("jdoe-1");
        user.setPassword("secret");
        user.setFirstname("John");
        user.setLastname("Doe");
        userApplication.setUser(user);

        ClientResponse response = resource().path(HOST).path("/applications").entity(userApplication).post(ClientResponse.class, userApplication);
        assertEquals("response was not '201 created'", ClientResponse.Status.CREATED.getStatusCode(), response.getStatus());
        String newLocation = response.getHeaders().getFirst("location");

        response = resource().path(HOST).path("/applications").entity(userApplication).post(ClientResponse.class, userApplication);
        assertEquals("response was not '409 conflict'", ClientResponse.Status.CONFLICT.getStatusCode(), response.getStatus());

        ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(newLocation);
        response = service.get(ClientResponse.class);
        assertEquals("response was not '200 ok'", ClientResponse.Status.OK.getStatusCode(), response.getStatus());

        UserApplication returnedApplication = response.getEntity(UserApplication.class);
        assertEquals("applications do not match", userApplication.getUser().getUsername(), returnedApplication.getUser().getUsername());

        response = resource().path(HOST).path("/applications").queryParam("owner", "Fix").get(ClientResponse.class);
        List<UserApplication> userApplications = response.getEntity((new GenericType<List<UserApplication>>() {}));
        returnedApplication = userApplications.get(userApplications.size()-1);

        returnedApplication.setState(UserApplication.ApplicationState.ACCEPTED);
        response = service.queryParam("owner", "Fix").put(ClientResponse.class, returnedApplication);
        assertEquals("application was not updated", ClientResponse.Status.ACCEPTED.getStatusCode(), response.getStatus());

        response = service.get(ClientResponse.class);
        assertEquals("response was not '200 ok'", ClientResponse.Status.OK.getStatusCode(), response.getStatus());

        UserApplication updatedApplication = response.getEntity(UserApplication.class);
        assertEquals("updated applications do not match", UserApplication.ApplicationState.ACCEPTED, updatedApplication.getState());
    }

    @Test
    public void testGetAllApplications() throws Exception {
        UserApplication userApplication = new UserApplication();
        User user = new User();
        user.setUsername("jdoe-2");
        user.setPassword("secret");
        user.setFirstname("John");
        user.setLastname("Doe");
        userApplication.setUser(user);

        ClientResponse response = resource().path(HOST).path("/applications").entity(userApplication).post(ClientResponse.class, userApplication);
        assertEquals("response was not '201 created'", ClientResponse.Status.CREATED.getStatusCode(), response.getStatus());

        userApplication = new UserApplication();
        user = new User();
        user.setUsername("jappleseed-2");
        user.setPassword("secret");
        user.setFirstname("John");
        user.setLastname("Appleseed");
        userApplication.setUser(user);

        response = resource().path(HOST).path("/applications").entity(userApplication).post(ClientResponse.class, userApplication);
        assertEquals("response was not '201 created'", ClientResponse.Status.CREATED.getStatusCode(), response.getStatus());

        response = resource().path(HOST).path("/applications").queryParam("owner", "Foxy").get(ClientResponse.class);
        List<UserApplication> userApplications = response.getEntity((new GenericType<List<UserApplication>>() {}));

        assertTrue("should have returned more than one entry", userApplications.size() > 1);
    }

    @Test
    public void testApplicationProcess() throws Exception {
        UserApplication userApplication = new UserApplication();
        User user = new User();
        user.setUsername("pprocess-22");
        user.setPassword("secret");
        user.setFirstname("Patricia");
        user.setLastname("Process");
        userApplication.setUser(user);

        ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		ClientResponse response = client.resource("http://localhost:18080/rest-1.0/rest").path("/applications").post(ClientResponse.class, userApplication);

        System.out.println("process started!");
    }

    @Test
    public void testGetPendingApplications() throws Exception {
        ClientResponse response = resource().path(HOST).path("/applications").queryParam("state", "PENDING").get(ClientResponse.class);
    }
}
