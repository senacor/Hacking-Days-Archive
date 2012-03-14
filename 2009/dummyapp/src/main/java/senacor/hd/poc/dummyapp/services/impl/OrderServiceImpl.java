package senacor.hd.poc.dummyapp.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import senacor.hd.poc.dummyapp.model.Order;
import senacor.hd.poc.dummyapp.services.OrderService;

public class OrderServiceImpl implements OrderService {
	private static Logger LOG = Logger.getLogger("OrderServiceImpl");
	private static OrderService instance = null;
	
	private OrderServiceImpl() {
	}

	public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderServiceImpl();
        }
        return instance;
    }

	public List<Order> getOrders() {
		LOG.info("returning orders for all clients");
		return new ArrayList<Order>();
	}

	public List<Order> getOrdersForCustomer(String customerId) {
		LOG.info("returning orders for client '"+customerId+"'");
		return new ArrayList<Order>();
	}
}
