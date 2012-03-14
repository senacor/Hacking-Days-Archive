package winzinger.poc.tef.dummyservice;

import java.util.Hashtable;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

public class DummyActivator implements BundleActivator {
	private Logger log = Logger.getLogger("DummyActivator");

	private DummyService service = null;
	private ServiceTracker serviceTracker;
   
	public void start(BundleContext context) throws Exception {
		log.info("dummy-activator starting ...");
		service = new DummyServiceImpl();
		
		// register the service
		ServiceRegistration sr = context.registerService(DummyService.class.getName(), service, new Hashtable());
		log.info("dummy service registered");
		
		// create a tracker and track the service
		serviceTracker = new ServiceTracker(context, DummyService.class.getName(), null);
		serviceTracker.open();
		
		// grab the service
		service = (DummyService) serviceTracker.getService();
	}

	public void stop(BundleContext context) throws Exception {
		log.info("dummy-activator stopping ...");
	}
}
