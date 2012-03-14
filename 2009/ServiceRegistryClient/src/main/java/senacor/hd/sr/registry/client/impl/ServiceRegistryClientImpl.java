package senacor.hd.sr.registry.client.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import senacor.hd.sr.registry.client.ServiceRegistryClient;
import senacor.hd.sr.registry.client.model.Service;
import senacor.hd.sr.registry.client.model.ServiceList;

public class ServiceRegistryClientImpl implements ServiceRegistryClient {
	// private final static String BASE_URI = "http://podcast.senacor.com/ServiceRegistry/registry/services";
	private final static String BASE_URI = "http://192.168.100.68:8080/ServiceRegistry/registry/services";
	
	public void registerService(Service service) {
		System.out.println("register service: "+service);
		
		Client client = Client.create();	
		WebResource wr = client.resource(BASE_URI);
		WebResource.Builder b = wr.accept("application/json");
	    b = b.entity(service, MediaType.APPLICATION_JSON);

	    System.out.println("posting service(JSON) to "+BASE_URI);
	    ClientResponse cr = b.post(ClientResponse.class);	    
	    if (cr.getClientResponseStatus() == ClientResponse.Status.CREATED) {
	    	System.out.println("ok. service registered");
	    } else if (cr.getClientResponseStatus() == ClientResponse.Status.CONFLICT) {
	    	System.out.println("service registration failed - "+cr.getClientResponseStatus().getReasonPhrase()+": "+cr.getEntity(String.class));
	    	updateService(service);
	    } else {
	    	System.out.println("service registration failed - "+cr.getClientResponseStatus().getReasonPhrase());
	    	System.out.println(cr.getEntity(String.class));
	    }
	}

	public void updateService(Service service) {
		System.out.println("update service: "+service);
		
		String encodedName = null;
		try {
			encodedName = URLEncoder.encode(service.getName(), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
		Client client = Client.create();	
		WebResource wr = client.resource(BASE_URI+"/"+encodedName);
		WebResource.Builder b = wr.accept("application/json");
	    b = b.entity(service, MediaType.APPLICATION_JSON);

	    System.out.println("putting service(JSON) to "+BASE_URI+"/"+encodedName);
	    ClientResponse cr = b.put(ClientResponse.class);	    
	    if (cr.getClientResponseStatus() == ClientResponse.Status.OK) {
	    	System.out.println("service updated");
	    	System.out.println(cr.getEntity(String.class));
	    } else {
	    	System.out.println("service update failed - "+cr.getClientResponseStatus().getReasonPhrase()+": "+cr.getEntity(String.class));
	    }
	}

	public void deleteService(String serviceName) {
		String encodedName = null;
		try {
			encodedName = URLEncoder.encode(serviceName, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		Client client = Client.create();	
		WebResource wr = client.resource(BASE_URI+"/"+encodedName);
		WebResource.Builder b = wr.accept("application/json");

	    ClientResponse cr = b.delete(ClientResponse.class);	    
	    if (cr.getClientResponseStatus() == ClientResponse.Status.OK) {
	    	System.out.println("service deleted");
	    } else {
	    	System.out.println("service deletion failed - "+cr.getClientResponseStatus().getReasonPhrase());
	    	System.out.println(cr.getEntity(String.class));
	    }
	}

	public Service getService(String serviceName) {
		Service service = null;
		String encodedName = null;
		try {
			encodedName = URLEncoder.encode(serviceName, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		Client client = Client.create();	
		WebResource wr = client.resource(BASE_URI+"/"+encodedName);
		WebResource.Builder b = wr.accept("application/json");

	    ClientResponse cr = b.get(ClientResponse.class);	    
	    if (cr.getClientResponseStatus() == ClientResponse.Status.OK) {
	    	service = cr.getEntity(Service.class);
	    	System.out.println("got service: "+service.getName()+"/"+service.getUri());
	    } else {
	    	System.out.println("service deletion failed - "+cr.getClientResponseStatus().getReasonPhrase());
	    	System.out.println(cr.getEntity(String.class));
	    }
	    
	    return service;
	}

	public List<Service> getServices() {
		ServiceList serviceList = null;

		Client client = Client.create();	
		WebResource wr = client.resource(BASE_URI);
		WebResource.Builder b = wr.accept("application/json");

	    ClientResponse cr = b.get(ClientResponse.class);	    
	    if (cr.getClientResponseStatus() == ClientResponse.Status.OK) {
	    	serviceList = cr.getEntity(ServiceList.class);
	    	System.out.println("got services: #"+serviceList.getServices().size());
	    } else {
	    	System.out.println("service list failed - "+cr.getClientResponseStatus().getReasonPhrase());
	    	System.out.println(cr.getEntity(String.class));
	    }
	    
	    return serviceList.getServices();
	}
}
