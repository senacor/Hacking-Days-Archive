package senacor.hd.sgh;

import org.osgi.framework.BundleContext;

public class HookServiceImpl implements HookService {
	private BundleContext bundleContext;
	
	public HookServiceImpl(BundleContext bundleContext) {
		super();
		this.bundleContext = bundleContext;
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

}
