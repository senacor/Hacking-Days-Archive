package winzinger.poc.tef.felixintegration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.framework.util.Util;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.osgi.framework.BundleException;

import winzinger.poc.tef.dummyservice.DummyActivator;

public class FelixLaunchListener implements ServletContextListener {
	private Logger log = Logger.getLogger("FelixLaunchListener");
	private Felix felix = null;

	@SuppressWarnings("unchecked")
	public void contextInitialized(ServletContextEvent arg0) {
		log.info("intialializing servlet context");

		Main.loadSystemProperties();
		Properties configProps = loadConfigProperties();
		if (configProps == null) {
			System.err.println("No properties found.");
			configProps = new Properties();
		}
		// Copy framework properties from the system properties.
	    Main.copySystemProperties(configProps);

		// Create a dummy activator;
		List list = new ArrayList();
		list.add(new DummyActivator());
		configProps.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, list);

		try {
			// Now create an instance of the framework with
			// our configuration properties.
			felix = new Felix(configProps);
			// Now start Felix instance.
			log.info("starting up felix ...");
			felix.init();
			felix.start();
			AutoProcessor.process(configProps, felix.getBundleContext());
			BundleContextHolder.createInstance(felix.getBundleContext());
		} catch (Exception ex) {
			log.severe("Could not create framework: " + ex);
		}
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			felix.stop();
			felix.waitForStop(5000L);
		} catch (BundleException be) {
			log.severe("failed to stop felix: "+be.getMessage());
		} catch (InterruptedException ie) {
			log.severe("failed to stop felix (timeout): "+ie.getMessage());
		}
	}
	
	
    public Properties loadConfigProperties()
    {
        // The config properties file is either specified by a system
        // property or it is in the conf/ directory of the Felix
        // installation directory.  Try to load it from one of these
        // places.

        // See if the property URL was specified as a property.
        URL propURL = null;
        String custom = System.getProperty("felix.config.properties");
        if (custom != null)
        {
            try
            {
                propURL = new URL(custom);
            }
            catch (MalformedURLException ex)
            {
                System.err.print("Main: " + ex);
                return null;
            }
        }
        else
        {
            // Determine where the configuration directory is by figuring
            // out where felix.jar is located on the system class path.
            File confDir = null;
            String classpath = System.getProperty("java.class.path");
            log.info("class-path: "+classpath);
            int index = classpath.toLowerCase().indexOf("felix.jar");
            int start = classpath.lastIndexOf(File.pathSeparator, index) + 1;
            if (index >= start)
            {
                // Get the path of the felix.jar file.
                String jarLocation = classpath.substring(start, index);
                // Calculate the conf directory based on the parent
                // directory of the felix.jar directory.
                confDir = new File(
                    new File(new File(jarLocation).getAbsolutePath()).getParent(),
                    "conf");
            }
            else
            {
                // Can't figure it out so use the current directory as default.
                confDir = new File(System.getProperty("user.dir"), "conf");
            	log.info("felix.jar not found. switching to "+confDir);
            }

            try
            {
                propURL = new File(confDir, "config.properties").toURL();
                log.info("ok, file found");
            }
            catch (MalformedURLException ex)
            {
                System.err.print("Main: " + ex);
                return null;
            }
        }

        // Read the properties file.
        Properties props = new Properties();
        InputStream is = null;
        try
        {
            // Try to load config.properties.
            is = propURL.openConnection().getInputStream();
            props.load(is);
            log.info("PROPERTIES: "+props.toString());
            is.close();
        }
        catch (Exception ex)
        {
            // Try to close input stream if we have one.
            try
            {
                if (is != null) is.close();
            }
            catch (IOException ex2)
            {
                // Nothing we can do.
            }

            return null;
        }

        // Perform variable substitution for system properties.
        for (Enumeration e = props.propertyNames(); e.hasMoreElements(); )
        {
            String name = (String) e.nextElement();
            log.info("property: "+name+" / "+props.getProperty(name));
            props.setProperty(name,
                Util.substVars(props.getProperty(name), name, null, props));
        }

        return props;
    }

}
