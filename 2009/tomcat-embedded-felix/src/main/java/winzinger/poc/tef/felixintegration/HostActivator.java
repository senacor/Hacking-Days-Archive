package winzinger.poc.tef.felixintegration;

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class HostActivator implements BundleActivator {
	private Logger log = Logger.getLogger("HostActivator");
	
	public void start(BundleContext bundleContext) throws Exception {
		log.info("host-activator starting ...");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		log.info("host-activator stopping ...");
	}
}
