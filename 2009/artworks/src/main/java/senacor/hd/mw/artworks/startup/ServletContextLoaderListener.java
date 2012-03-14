package senacor.hd.mw.artworks.startup;

import senacor.hd.sr.registry.client.ServiceRegistryClient;
import senacor.hd.sr.registry.client.impl.ServiceRegistryClientImpl;
import senacor.hd.sr.registry.client.model.Service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * User: hannes
 * Date: Nov 28, 2009
 * Time: 12:15:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServletContextLoaderListener implements ServletContextListener {
    private static final String ARTWORK_MANAGER = "artwork-manager";

    private String getCompleteContextUrl() {
        try {
            InetAddress ownAddress = InetAddress.getLocalHost();
            return "http://" + ownAddress.getHostAddress() + ":8080/artwork-manager";
        } catch (UnknownHostException e) {
            throw new RuntimeException("Bloody shit!", e);
        }
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Service service = new Service(ARTWORK_MANAGER, getCompleteContextUrl());

        ServiceRegistryClient serviceRegistryClient = new ServiceRegistryClientImpl();
        serviceRegistryClient.registerService(service);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServiceRegistryClient serviceRegistryClient = new ServiceRegistryClientImpl();
        serviceRegistryClient.deleteService(ARTWORK_MANAGER);
    }
}
