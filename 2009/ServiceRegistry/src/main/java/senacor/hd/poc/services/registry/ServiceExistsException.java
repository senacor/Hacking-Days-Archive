package senacor.hd.poc.services.registry;

import senacor.hd.poc.registry.model.Service;

@SuppressWarnings("serial")
public class ServiceExistsException extends Exception {
	private Service newService;
	private Service oldService;

	public ServiceExistsException(Service newService, Service oldService) {
		super();
		this.newService = newService;
		this.oldService = oldService;
	}

	public Service getNewService() {
		return newService;
	}

	public Service getOldService() {
		return oldService;
	}
}
