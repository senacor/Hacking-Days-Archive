package senacor.hd.poc.dummyapp.services;

import java.util.List;

import senacor.hd.poc.dummyapp.model.Customer;

public interface CustomerService {
	public List<Customer> getNewCustomers();
	public Customer getCustomer(String id);
}
