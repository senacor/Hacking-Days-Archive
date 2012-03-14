package senacor.hd.poc.services.registry.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import senacor.hd.poc.registry.model.Service;
import senacor.hd.poc.registry.model.ServiceList;
import senacor.hd.poc.services.registry.ServiceExistsException;
import senacor.hd.poc.services.registry.ServiceRegistry;

public class ServiceRegistryImpl implements ServiceRegistry {
	private static Logger LOG = Logger.getLogger("ServiceRegistry");

	private static ServiceRegistry instance = null;
	private Map<String, Service> services = new HashMap<String, Service>();

	private ServiceRegistryImpl() {
	}

	public static ServiceRegistry getInstance() {
		if (instance == null) {
			instance = new ServiceRegistryImpl();
		}

		return instance;
	}

	public void addService(Service service) throws ServiceExistsException {
		Service oldService = services.get(service.getName());

		if (oldService != null) {
			throw new ServiceExistsException(service, oldService);
		} else {
			services.put(service.getName(), service);
		}
	}

	public ServiceList getServiceList() {
		ServiceList serviceList = new ServiceList();
		serviceList.getServices().addAll(services.values());

		return serviceList;
	}

	public void removeService(String serviceName) {
		services.remove(serviceName);
	}

	public void updateService(Service service) {
		services.put(service.getName(), service);
	}
}
