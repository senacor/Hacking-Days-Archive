package winzinger.poc.tef.felixintegration;

import org.osgi.framework.BundleContext;

public class BundleContextHolder {
	private static BundleContextHolder instance = null;
	private BundleContext bundleContext = null;
	
	private BundleContextHolder() {
		// dont use this one
	}
	
	private BundleContextHolder(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public static void createInstance(BundleContext bundleContext) {
		instance = new BundleContextHolder(bundleContext);
	}
	
	public static BundleContextHolder getInstance() {
		if (instance == null) {
			throw new RuntimeException("SGH: BundleContextHolder was not created yet");
		}
 		return instance;
	}
	
	public BundleContext getBundleContext() {
		return bundleContext;
	}
}
