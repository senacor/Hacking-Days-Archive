package com.senacor.hd11;

import com.senacor.hd11.model.Message;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerException;
import org.junit.Before;
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
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class MessageServiceTest extends JerseyTest {
    private final static String HOST = "";

    public MessageServiceTest() throws TestContainerException {
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
    public void testBadCreateMessage() throws Exception {
        String username = "jdoe";
        String text = "this is my message bomb";

        ClientResponse response = resource().path(HOST).path("/messages").path(username).post(ClientResponse.class, text);
        assertEquals("message was not created", ClientResponse.Status.CREATED.getStatusCode(), response.getStatus());

        Message message = response.getEntity(Message.class);

        assertEquals("wrong username returned", username, message.getUsername());
        assertEquals("wrong text returned", "Wusstet Ihr schon ... der Meister ist groß, der Meister ist gütig", message.getText());



        text = "this is my message sex";

        response = resource().path(HOST).path("/messages").path(username).post(ClientResponse.class, text);
        assertEquals("message was not created", ClientResponse.Status.CREATED.getStatusCode(), response.getStatus());

        message = response.getEntity(Message.class);

        assertEquals("wrong username returned", username, message.getUsername());
        assertEquals("wrong text returned", "Wusstet Ihr schon ... der Meister ist groß, der Meister ist gütig", message.getText());



        text = "this is my message terror";

        response = resource().path(HOST).path("/messages").path(username).post(ClientResponse.class, text);
        assertEquals("message was not created", ClientResponse.Status.CREATED.getStatusCode(), response.getStatus());

        message = response.getEntity(Message.class);

        assertEquals("wrong username returned", username, message.getUsername());
        assertEquals("wrong text returned", "Wusstet Ihr schon ... der Meister ist groß, der Meister ist gütig", message.getText());



        text = "look, a sexbomb!";

        response = resource().path(HOST).path("/messages").path(username).post(ClientResponse.class, text);
        assertEquals("message was not created", ClientResponse.Status.CREATED.getStatusCode(), response.getStatus());

        message = response.getEntity(Message.class);

        assertEquals("wrong username returned", username, message.getUsername());
        assertEquals("wrong text returned", "<sorry, user was muted>", message.getText());
    }
}
