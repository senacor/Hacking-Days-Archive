package senacor.hd.rest.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import senacor.hd.poc.dummyapp.model.Customer;

@Transactional
public class CustomerDaoImpl implements CustomerDao {
	private EntityManager entityManager;
	
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this. entityManager = entityManager;
    }
    
    public Customer saveCustomer(Customer customer) {
    	entityManager.persist(customer);
    	return customer;
    }

    public Customer updateCustomer(Customer customer) {
    	entityManager.merge(customer);
    	return customer;
    }

	public Customer loadCustomer(Long id) {
		return entityManager.find(Customer.class, id);
	}
}
