package senacor.hd.rest.orders;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import senacor.hd.poc.dummyapp.model.Customer;
import senacor.hd.poc.dummyapp.model.Order;
import senacor.hd.poc.dummyapp.model.Order.OrderState;
import senacor.hd.rest.dao.CustomerDao;

@Path("/customer/{id}/orders")
@Component
@Scope("request")
public class OrdersResource {
	private static Logger LOG = Logger.getLogger("OrdersResource");
	
	CustomerDao customerDao = null;
	
	public CustomerDao getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	@POST
	@Consumes("application/json")
	public Response addCustomer(@Context UriInfo uriInfo, @PathParam("id") Long id) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		String requestUri  = uriInfo.getAbsolutePath().toString();
		
		Customer customer = customerDao.loadCustomer(id);
		
		if (customer == null) {
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).
					entity("customer '"+id+"' not found").
					type("application/json").build());
		}
		
		Order order = new Order();
		order.setEingang(new Date());
		order.setOrderState(OrderState.ACTIVE);
		Integer orderNo = customer.getOrders().size()+1;
		order.setOrderNo(orderNo);
		
		customer.getOrders().add(order);
		customer = customerDao.updateCustomer(customer);
		
		LOG.info("order persisted: "+order.getId());
		
		List<Order> orderList = customer.getOrders();
		Long newId = null;
		if (orderList.size() > 0) {
			newId = orderList.get(orderList.size()-1).getId();
		}
		
		try {
			return Response.created(new URI(requestUri+"/"+orderNo)).build();
		} catch (URISyntaxException e) {
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR).
					entity("could not create URI form '"+requestUri+"' and '"+orderNo+"': "+e.getMessage()).
					type("application/json").build());
		}
	}
	
	@GET
	@Produces("application/json")
	public List<Order> getFilteredOrder(@PathParam("id") Long id, @QueryParam("state") Order.OrderState filterState) {
		List<Order> orderList = new ArrayList<Order>();
		
		Customer customer = customerDao.loadCustomer(id);
		
		if (customer == null) {
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).
					entity("customer '"+id+"' not found").
					type("application/json").build());
		}
		
		for (Order order : customer.getOrders()) {
			if (order.getOrderState() == filterState) {
				orderList.add(order);
			}
		}

		return orderList;
	}
}
