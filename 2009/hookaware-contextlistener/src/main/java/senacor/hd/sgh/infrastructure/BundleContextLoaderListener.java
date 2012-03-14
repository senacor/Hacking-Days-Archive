package senacor.hd.sgh.infrastructure;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import senacor.hd.sgh.HookService;

/**
 * Bootstrap listener to start up Spring's root {@link WebApplicationContext}.
 * Simply delegates to {@link ContextLoader}.
 */
public class BundleContextLoaderListener implements ServletContextListener {
	Logger logger = Logger.getLogger("BundleContextLoaderListener");

	private HookService hookService;
		
	@Resource
	private void setHookService(HookService hookService) {
		logger.info("setting hookService to "+hookService);
		this.hookService = hookService;
		
		if (hookService != null) {
			logger.info("hookService is set to "+hookService);
			BundleContextHolder.createInstance(hookService.getBundleContext());
		} else {
			logger.severe("hookService is not valid - maybe bundle Spring-Glassfish-Hook is not acive");
		}
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("context destroyed");
	}

	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("context initializing ... hookService is currently "+hookService);

		if (hookService == null) {
			String jndiName = "java:comp/env/"+HookService.class.getName();
			logger.warning("hookService is still null - trying JNDI lookup '"+jndiName+"'");
			
			try {
				Context ctx = new InitialContext();
				HookService hs = (HookService) ctx.lookup(jndiName);
				logger.info("hookService via JNDI: "+hs);
				setHookService(hs);
			} catch (NamingException e) {
				throw new RuntimeException("hookService could not be initialized via resource injection or JNDI-lookup", e);
			}
		}
	}
}
