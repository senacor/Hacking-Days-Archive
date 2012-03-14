package senacor.hd.poc.dummyapp.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import senacor.hd.poc.dummyapp.model.Customer;
import senacor.hd.poc.dummyapp.services.CustomerService;

public class CustomerServiceImpl implements CustomerService {
	private static Logger LOG = Logger.getLogger("CustomerService");
	
	private static CustomerService instance = null;
	
	private List<Customer> customers = new ArrayList<Customer>();
	private int idx = 0;

	private CustomerServiceImpl() {
	}
	
	public static CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerServiceImpl();
        }
        return instance;
    }
	
	public List<Customer> getNewCustomers() {
		if (Math.random() > 0.3) {
			Customer c = new Customer();

			c.setId(new Long(++idx));
			c.setVorname("Wolfgang_" + idx);
			c.setNachname("Wunderlich_" + idx);
			c.setKundeSeit(new Date());

			customers.add(c);
		}
		
		List<Customer> retval = new ArrayList<Customer>();
		retval.addAll(customers);
		Collections.sort(retval, new Comparator<Customer>() {
			public int compare(Customer c1, Customer c2) {
				return c2.getKundeSeit().compareTo(c1.getKundeSeit());
			};
		});
		while (retval.size() > 10) {
			retval.remove(10);
		}

		return retval;
	}
	
	public Customer getCustomer(String id) {
		for (Customer customer : customers) {
			LOG.info("checking customer with id '"+customer.getId()+"'  - looking for '"+id+"'");
			if (customer.getId().equals(id)) {
				return customer;
			}
		}
		
		return null;
	}
}
