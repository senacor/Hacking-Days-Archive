package senacor.hd.rest.customers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.logging.Logger;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import senacor.hd.poc.dummyapp.model.Customer;
import senacor.hd.rest.dao.CustomerDao;

@Path("/customers")
@Component
@Scope("request")
public class CustomersResource {
	private static Logger LOG = Logger.getLogger("CustomersResource");

	CustomerDao customerDao = null;
	
	public CustomerDao getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	// curl -i -X POST -H "Content-Type: application/json" -d '{"name":"test","uri":"http://test.de/doit"}' http://localhost:8080/ServiceRegistry/registry/services
	@POST
	@Consumes("application/json")
	public Response addCustomer(@Context UriInfo uriInfo, Customer customer) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		String requestUri  = uriInfo.getAbsolutePath().toString();
		
		if (customer.getId() != null) {
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).
					entity("'id' must not be set").
					type("application/json").build());
		}
		if ((customer.getVorname() == null) || (customer.getNachname() == null)) {
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).
					entity("'vorname' and 'nachname' are obligatory").
					type("application/json").build());
		}
		if (customer.getKundeSeit() == null) {
			customer.setKundeSeit(new Date());
		}
		
		customerDao.saveCustomer(customer);
		
		try {
			return Response.created(new URI(requestUri+"/"+customer.getId())).build();
		} catch (URISyntaxException e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR).
					entity("could not create URI form '"+requestUri+"' and '"+customer.getId()+"': "+e.getMessage()).
					type("application/json").build());
		}
	}
}
