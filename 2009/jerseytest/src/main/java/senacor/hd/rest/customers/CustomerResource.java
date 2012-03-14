package senacor.hd.rest.customers;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import senacor.hd.poc.dummyapp.model.Customer;
import senacor.hd.rest.dao.CustomerDao;

import com.sun.jersey.api.NotFoundException;

@Path("/customers/{id}")
@Component
@Scope("request")
public class CustomerResource {
	private static Logger LOG = Logger.getLogger("CustomerResource");
	
	CustomerDao customerDao = null;
	
	public CustomerDao getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	@GET
	@Produces("application/json")
	public Customer getCustomerJSON(@PathParam("id") Long id) {
		LOG.info("getCustomer '"+id+"' - application/json");
		
		Customer customer = customerDao.loadCustomer(id);
		if (customer == null) {
			LOG.severe("customer not found");
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).
					entity("customer '"+id+"' not found").
					type("application/json").build());
		}
		
		return customer;
	}
	
	@GET
	@Produces("text/html")
	public String getCustomerHTML(@PathParam("id") Long id) {
		LOG.info("getCustomer '"+id+"' - text/html");
		
		Customer customer = customerDao.loadCustomer(id);
		if (customer == null) {
			LOG.severe("customer not found");
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).
					entity("customer '"+id+"' not found").
					type(MediaType.TEXT_PLAIN).build());
		}
		
		StringBuffer retval = new StringBuffer();
		retval.append("<html>");
		retval.append("<head>");
		retval.append("<title>REST-Test</title>");
		retval.append("</head>");
		retval.append("<body>");
		retval.append(customer.getVorname()).append(" ").append(customer.getNachname());
		retval.append("</body>");

		return retval.toString();
	}	
}
