package senacor.hd.poc.dummyapp.services;

import java.util.List;

import senacor.hd.poc.dummyapp.model.Order;

public interface OrderService {
	public List<Order> getOrders();
	public List<Order> getOrdersForCustomer(String customerId);
}
