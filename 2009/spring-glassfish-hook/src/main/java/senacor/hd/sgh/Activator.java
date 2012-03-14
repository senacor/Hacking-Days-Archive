package senacor.hd.sgh;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	private HookService service;
	private ServiceTracker hookServiceTracker;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		service = new HookServiceImpl(context);
		
		// register the service
		context.registerService(HookService.class.getName(), service, new Hashtable());
		
		// create a tracker and track the service
		hookServiceTracker = new ServiceTracker(context, HookService.class.getName(), null);
		hookServiceTracker.open();
		
		// grab the service
		service = (HookService) hookServiceTracker.getService();
		System.err.println(service.getBundleContext().toString());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		// close the service tracker
		hookServiceTracker.close();
		hookServiceTracker = null;
		
		service = null;
	}
}
