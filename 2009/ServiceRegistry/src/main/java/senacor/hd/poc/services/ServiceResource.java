package senacor.hd.poc.services;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import senacor.hd.poc.registry.model.Service;
import senacor.hd.poc.services.registry.ServiceExistsException;
import senacor.hd.poc.services.registry.impl.ServiceRegistryImpl;

@Path("/services/{name}")
public class ServiceResource {
	private static Logger LOG = Logger.getLogger("ServiceResource");

	@GET
	@Produces("application/json")
	public Service getServiceListJSON(@PathParam("name") String name)
			throws UnsupportedEncodingException {
		LOG.info("getService("+name+") - application/json");

		String decodedName = URLDecoder.decode(name, "ISO-8859-1");

		Service service = null;
		List<Service> services = ServiceRegistryImpl.getInstance()
				.getServiceList().getServices();
		for (Service s : services) {
			if (s.getName().equalsIgnoreCase(decodedName)) {
				service = s;
				break;
			}
		}

		if (service == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("service '" + decodedName + "' is not registered.")
					.type(MediaType.TEXT_PLAIN).build());
		}

		return service;
	}

	@GET
	@Produces("text/html")
	public String getServiceListHTML(@PathParam("name") String name)
			throws UnsupportedEncodingException {
		LOG.info("getService - application/json");

		String decodedName = URLDecoder.decode(name, "ISO-8859-1");

		Service service = null;
		List<Service> services = ServiceRegistryImpl.getInstance()
				.getServiceList().getServices();
		for (Service s : services) {
			if (s.getName().equalsIgnoreCase(decodedName)) {
				service = s;
				break;
			}
		}

		if (service == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("service '" + decodedName + "' is not registered.")
					.type(MediaType.TEXT_PLAIN).build());
		}

		StringBuffer retval = new StringBuffer();
		retval.append("<html>");
		retval.append("<head>");
		retval.append("<title>Hacking Days Service Registry</title>");
		retval.append("</head>");
		retval.append("<body>");

		retval.append("<table border=1>");
		retval.append("<tr>");
		retval.append("<th>").append("Service Name").append("</th>");
		retval.append("<th>").append("URI").append("</th>");
		retval.append("</tr>");
		retval.append("<tr>");
		retval.append("<td>").append(service.getName()).append("</td>");
		retval.append("<td>").append(service.getUri()).append("</td>");
		retval.append("</tr>");

		retval.append("</table>");
		retval.append("</body>");

		return retval.toString();
	}

	// curl -i -X PUT -H "Content-Type: application/json" -d
	// '{"name":"test","uri":"http://test.de/doit"}'
	// http://localhost:8080/ServiceRegistry/registry/services
	@PUT
	@Consumes("application/json")
	public Response updateService(@PathParam("name") String name,
			Service newServiceData) throws UnsupportedEncodingException {
		LOG.info("updateService (via PUT)");

		LOG.info("Name             : " + name);
		String encodedName = URLEncoder.encode(newServiceData.getName(),
				"ISO-8859-1");
		LOG.info("ServiceName (enc): " + encodedName);

		LOG.info("ServiceName      : " + newServiceData.getName());
		LOG.info("Service-URI      : " + newServiceData.getUri());

		if (!name.equalsIgnoreCase(encodedName)) {
			throw new WebApplicationException(Response.status(
					Status.BAD_REQUEST).entity(
					"encoded serviceName '" + encodedName
							+ "' does not match URI '" + name + "'.").type(
					MediaType.TEXT_PLAIN).build());
		}

		Service service = null;
		List<Service> services = ServiceRegistryImpl.getInstance()
				.getServiceList().getServices();
		
		Iterator<Service> servicesIt = services.iterator();
		while (servicesIt.hasNext()) {
			Service serviceElem = servicesIt.next();
			if (serviceElem.getName().equalsIgnoreCase(newServiceData.getName())) {
				servicesIt.remove();	
				break;
			}
		}
		try {
			ServiceRegistryImpl.getInstance().addService(newServiceData);
		} catch (ServiceExistsException e) {
			// humbug, wir haben ja gerade gelšscht
		}

		return Response.ok().build();
	}

	@DELETE
	public Response deleteService(@PathParam("name") String name)
			throws UnsupportedEncodingException {
		LOG.info("deleteService (via DELETE)");

		LOG.info("Name             : " + name);
		String decodedName = URLDecoder.decode(name, "ISO-8859-1");
		LOG.info("ServiceName (dec): " + decodedName);

		ServiceRegistryImpl.getInstance().removeService(decodedName);

		return Response.status(Status.OK).build();
	}
}
