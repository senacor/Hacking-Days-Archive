package senacor.hd.sgh;

import org.osgi.framework.BundleContext;

/**
 * This services provides a link between the OSGi Core and the JEE Container 
 * by fetching the BundleContext and exposing it to the Container.
 * Implementation Details: This service is injected into a managed object (in our case the GlassfishContextLoaderListener), 
 * and by magic and the MyBeanPostProcessor, each spring bean gets this BundleContext set. 
 */
public interface HookService {
	public BundleContext getBundleContext();
}
