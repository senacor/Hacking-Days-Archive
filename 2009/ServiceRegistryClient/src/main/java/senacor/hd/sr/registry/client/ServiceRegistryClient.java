package senacor.hd.sr.registry.client;

import java.util.List;

import senacor.hd.sr.registry.client.model.Service;

public interface ServiceRegistryClient {
	public void registerService(Service service);
	public void updateService(Service service);
	public void deleteService(String serviceName);
	public Service getService(String serviceName);
	public List<Service> getServices();
}
