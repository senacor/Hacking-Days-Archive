package senacor.hd.poc.services;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import senacor.hd.poc.registry.model.Service;
import senacor.hd.poc.registry.model.ServiceList;
import senacor.hd.poc.services.registry.ServiceExistsException;
import senacor.hd.poc.services.registry.ServiceRegistry;
import senacor.hd.poc.services.registry.impl.ServiceRegistryImpl;

@Path("/services")
public class ServicelistResource {
	private static Logger LOG = Logger.getLogger("ServicelistResource");

	@GET
	@Produces("application/json")
	public ServiceList getServiceListJSON() {
		LOG.info("getServiceList - application/json");

		ServiceRegistry sr = ServiceRegistryImpl.getInstance();
		ServiceList sl = sr.getServiceList();
		LOG.info("#items: "+sl.getServices().size());
		
		return sr.getServiceList();
	}

	@GET
	@Produces("text/html")
	public String getServiceListHTML() {
		LOG.info("getServiceList - text/html");

		ServiceRegistry sr = ServiceRegistryImpl.getInstance();

		StringBuffer retval = new StringBuffer();
		retval.append("<html>");
		retval.append("<head>");
		retval.append("<title>Hacking Days Service Registry</title>");
		retval.append("</head>");
		retval.append("<body>");

		List<Service> services = sr.getServiceList().getServices();
		if (services.size() == 0) {
			retval.append("- sorry, tiger ... currently no services -");
		} else {
			retval.append("<table border=1>");
			retval.append("<tr>");
			retval.append("<th>").append("Service Name").append("</th>");
			retval.append("<th>").append("URI").append("</th>");
			retval.append("</tr>");

			for (Service service : services) {
				retval.append("<tr>");
				retval.append("<td>").append(service.getName()).append("</td>");
				retval.append("<td>").append(service.getUri()).append("</td>");
				retval.append("</tr>");
			}

			retval.append("</table>");
		}
		retval.append("</body>");

		return retval.toString();
	}

	// curl -i -X POST -H "Content-Type: application/json" -d '{"name":"test","uri":"http://test.de/doit"}' http://localhost:8080/ServiceRegistry/registry/services
	@POST
    @Consumes("application/json")
	public Response addService(@Context UriInfo uriInfo, Service service) throws URISyntaxException {
		LOG.info("addService (via POST)");
		
		LOG.info("ServiceName: "+service.getName());
		LOG.info("Serive-URI : "+service.getUri());

		String requestUri  = uriInfo.getAbsolutePath().toString();
		String encodedName = null;
		try {
			encodedName = URLEncoder.encode(service.getName(), "ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		LOG.info("reqUri: "+requestUri);
		LOG.info("encName: "+encodedName);

		try {
			ServiceRegistryImpl.getInstance().addService(service);
		} catch (ServiceExistsException e) {
			throw new WebApplicationException(
					Response.status(Status.CONFLICT).
					entity("service '"+service.getName()+"' is already registered and could not be added - try to update.").
					type(MediaType.TEXT_PLAIN).build());
		}
		
		return Response.created(new URI(requestUri+"/"+encodedName)).build();
	}
}
