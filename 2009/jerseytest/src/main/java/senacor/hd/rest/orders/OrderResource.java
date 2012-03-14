package senacor.hd.rest.orders;

import java.util.logging.Logger;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import senacor.hd.poc.dummyapp.model.Customer;
import senacor.hd.poc.dummyapp.model.Order;
import senacor.hd.rest.dao.CustomerDao;

@Path("/customer/{id}/order/{orderNo}")
@Component
@Scope("request")
public class OrderResource {
	private static Logger LOG = Logger.getLogger("OrderResource");
	
	CustomerDao customerDao = null;
	
	public CustomerDao getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	@PUT
	@Consumes("application/json")
	public Response updateOrder(@PathParam("id") Long id,  @PathParam("orderNo") Integer orderNo, Order newOrder) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		Customer customer = customerDao.loadCustomer(id);
		
		if (customer == null) {
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).
					entity("customer '"+id+"' not found").
					type("application/json").build());
		}
		
		for (Order order : customer.getOrders()) {
			if (order.getOrderNo().equals(orderNo)) {
				if (newOrder.getEingang() != null) {
					order.setEingang(newOrder.getEingang());
				}
				if (newOrder.getOrderState() != null) {
					order.setOrderState(newOrder.getOrderState());
				}
				customerDao.updateCustomer(customer);
				return Response.status(Status.ACCEPTED).build();
			}
		}
		

		throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).
				entity("order '"+orderNo+"' not found").
				type("application/json").build());
	}
	
	@DELETE
	public Response deleteOrder(@PathParam("id") Long id,  @PathParam("orderNo") Integer orderNo) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		LOG.info("deleting order: "+id+"/"+orderNo);
		 
		Customer customer = customerDao.loadCustomer(id);
		
		if (customer == null) {
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).
					entity("customer '"+id+"' not found").
					type("application/json").build());
		}
		
		for (Order order : customer.getOrders()) {
			if (order.getOrderNo().equals(orderNo)) {
				order.setOrderState(Order.OrderState.INACTIVE);
				customerDao.updateCustomer(customer);
				return Response.status(Status.OK).build();
			}
		}
		

		throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).
				entity("order '"+orderNo+"' not found").
				type("application/json").build());
	}
}
