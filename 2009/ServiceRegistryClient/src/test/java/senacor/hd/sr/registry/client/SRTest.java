package senacor.hd.sr.registry.client;

import java.util.List;

import senacor.hd.sr.registry.client.impl.ServiceRegistryClientImpl;
import senacor.hd.sr.registry.client.model.Service;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class SRTest extends TestCase {
	private ServiceRegistryClient src = new ServiceRegistryClientImpl();
	
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public SRTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SRTest.class);
	}
	

	public void testRegister() {
		src.deleteService("testservice1");
		
		List<Service> serviceList = src.getServices();
		System.out.println("####");
		for (Service service: serviceList) {
			System.out.println("s: "+service.getName()+" - "+service.getUri());
		}
		src.registerService(new Service("testservice1", "test://dies.ist.ein.test"));
		System.out.println("####");
		serviceList = src.getServices();
		for (Service service: serviceList) {
			System.out.println("s: "+service.getName()+" - "+service.getUri());
		}
		System.out.println("####");
		src.registerService(new Service("testservice1", "test://dies.ist.ein.test"));
		System.out.println("####");
		serviceList = src.getServices();
		for (Service service: serviceList) {
			System.out.println("s: "+service.getName()+" - "+service.getUri());
		}

		src.updateService(new Service("testservice", "test://dies.ist.ein.test"));
		src.deleteService("testservice");

		src.registerService(new Service("testservice1", "test://dies.ist.ein.test"));
		src.getService("testservice1");
		src.updateService(new Service("testservice1", "test://dies.ist.ein.test.1"));
		src.registerService(new Service("testservice2", "test://dies.ist.ein.test.2"));
		serviceList = src.getServices();
		for (Service service: serviceList) {
			System.out.println("s: "+service.getName()+" - "+service.getUri());
		}
		src.deleteService("testservice1");
		src.deleteService("testservice2");
	}
}
