package senacor.hd.poc.services.registry;

import senacor.hd.poc.registry.model.Service;
import senacor.hd.poc.registry.model.ServiceList;

public interface ServiceRegistry {
	public ServiceList getServiceList();
	public void addService(Service service) throws ServiceExistsException;
	public void updateService(Service service);
	public void removeService(String serviceName);
}
