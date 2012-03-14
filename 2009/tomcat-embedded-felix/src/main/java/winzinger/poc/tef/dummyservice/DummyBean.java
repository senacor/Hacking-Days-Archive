package winzinger.poc.tef.dummyservice;

import java.util.logging.Logger;

import senacor.hd.GreeterService;

public class DummyBean {
	private Logger log = Logger.getLogger("DummyBean");
	private DummyService dummyService = null;
	private GreeterService greeterService = null;

	public DummyService getDummyService() {
		return dummyService;
	}

	public void setDummyService(DummyService dummyService) {
		this.dummyService = dummyService;
		
		log.info("dummy-service was injected ... sayHello('ralph'): "+dummyService.sayHello("ralph"));
	}

	public GreeterService getGreeterService() {
		return greeterService;
	}

	public void setGreeterService(GreeterService greeterService) {
		this.greeterService = greeterService;
	
		log.info("greeter-service was injected ... sayHello('ralph'): "+greeterService.sayHello("ralph"));
	}
	
	
}
