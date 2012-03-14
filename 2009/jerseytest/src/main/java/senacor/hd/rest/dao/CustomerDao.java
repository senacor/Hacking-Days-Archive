package senacor.hd.rest.dao;

import senacor.hd.poc.dummyapp.model.Customer;

public interface CustomerDao {
	public Customer saveCustomer(Customer customer);
    public Customer updateCustomer(Customer customer);
	public Customer loadCustomer(Long id);
}
