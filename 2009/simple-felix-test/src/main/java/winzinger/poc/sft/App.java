package winzinger.poc.sft;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.felix.framework.Felix;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

/**
 * Hello world!
 * 
 */
public class App {
	private Logger log = Logger.getLogger("FelixLaunchListener");
	private Felix felix = null;
	private static Framework m_fwk = null;

	public static void main(String[] args) {
		App app = new App();
		app.doItSimple();
	}

	private void doItComplex() {
		Main.loadSystemProperties();
		Properties configProps = Main.loadConfigProperties();
		if (configProps == null) {
			System.err.println("No properties found.");
			configProps = new Properties();
		}

		// Copy framework properties from the system properties.
		Main.copySystemProperties(configProps);

		try {
			// Create an instance of the framework.
			FrameworkFactory factory = getFrameworkFactory();
			m_fwk = factory.newFramework(configProps);
			// Initialize the framework, but don't start it yet.
			m_fwk.init();
			// Use the system bundle context to process the auto-deploy
			// and auto-install/auto-start properties.
			AutoProcessor.process(configProps, m_fwk.getBundleContext());
			// Start the framework.
			m_fwk.start();
			// Wait for framework to stop to exit the VM.
			m_fwk.waitForStop(0);
			System.exit(0);
		} catch (Exception ex) {
			System.err.println("Could not create framework: " + ex);
			ex.printStackTrace();
			System.exit(-1);
		}
	}

	private void doItSimple() {
		Main.loadSystemProperties();
		Properties configProps = Main.loadConfigProperties();
		if (configProps == null) {
			System.err.println("No properties found.");
			configProps = new Properties();
		}
		// Copy framework properties from the system properties.
	    Main.copySystemProperties(configProps);

		// Create a configuration property map.
		Map configMap = new HashMap();

		try {
			// Now create an instance of the framework with
			// our configuration properties.
			felix = new Felix(configMap);
			// Now start Felix instance.
			log.info("starting up felix ...");
			felix.init();
			felix.start();
			// BundleContextHolder.createInstance(felix.getBundleContext());
			AutoProcessor.process(configProps, felix.getBundleContext());
		} catch (Exception ex) {
			log.severe("Could not create framework: " + ex);
		}
	}

	private static FrameworkFactory getFrameworkFactory() throws Exception {
		URL url = Main.class.getClassLoader().getResource(
				"META-INF/services/org.osgi.framework.launch.FrameworkFactory");
		if (url != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(url
					.openStream()));
			try {
				for (String s = br.readLine(); s != null; s = br.readLine()) {
					s = s.trim();
					// Try to load first non-empty, non-commented line.
					if ((s.length() > 0) && (s.charAt(0) != '#')) {
						return (FrameworkFactory) Class.forName(s)
								.newInstance();
					}
				}
			} finally {
				if (br != null)
					br.close();
			}
		}

		throw new Exception("Could not find framework factory.");
	}

	/*
	private void loadSystemProperties() {
		// The system properties file is either specified by a system
		// property or it is in the same directory as the Felix JAR file.
		// Try to load it from one of these places.

		// See if the property URL was specified as a property.
		URL propURL = null;
		String custom = System.getProperty(SYSTEM_PROPERTIES_PROP);
		if (custom != null) {
			try {
				propURL = new URL(custom);
			} catch (MalformedURLException ex) {
				System.err.print("Main: " + ex);
				return;
			}
		} else {
			// Determine where the configuration directory is by figuring
			// out where felix.jar is located on the system class path.
			File confDir = null;
			String classpath = System.getProperty("java.class.path");
			int index = classpath.toLowerCase().indexOf("felix.jar");
			int start = classpath.lastIndexOf(File.pathSeparator, index) + 1;
			if (index >= start) {
				// Get the path of the felix.jar file.
				String jarLocation = classpath.substring(start, index);
				// Calculate the conf directory based on the parent
				// directory of the felix.jar directory.
				confDir = new File(new File(new File(jarLocation)
						.getAbsolutePath()).getParent(), CONFIG_DIRECTORY);
			} else {
				// Can't figure it out so use the current directory as default.
				confDir = new File(System.getProperty("user.dir"),
						CONFIG_DIRECTORY);
			}

			try {
				propURL = new File(confDir, SYSTEM_PROPERTIES_FILE_VALUE)
						.toURL();
			} catch (MalformedURLException ex) {
				System.err.print("Main: " + ex);
				return;
			}
		}

		// Read the properties file.
		Properties props = new Properties();
		InputStream is = null;
		try {
			is = propURL.openConnection().getInputStream();
			props.load(is);
			is.close();
		} catch (FileNotFoundException ex) {
			// Ignore file not found.
		} catch (Exception ex) {
			System.err.println("Main: Error loading system properties from "
					+ propURL);
			System.err.println("Main: " + ex);
			try {
				if (is != null)
					is.close();
			} catch (IOException ex2) {
				// Nothing we can do.
			}
			return;
		}

		// Perform variable substitution on specified properties.
		for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {
			String name = (String) e.nextElement();
			System.setProperty(name, Util.substVars(props.getProperty(name),
					name, null, null));
		}
	}
	*/
}
