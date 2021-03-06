package com.vht.sms.content.htr;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HtrProps extends PropertiesConfiguration
{
	private static Log log = LogFactory.getLog(HtrProps.class);

	/**
	 * paths to search for API properties file. There always end in the "/"
	 * character and end entity is empty string
	 */
	private static final String[] SEARCH_PATH = {"/com/vht/sms/content/htr/"};

	/**
	 * Name of properties file to get resource
	 */
	private static final String PROPS_RESOURCE = "htr.properties";

	/**
	 * The singleton instance of the engine configuration.
	 */
	private static HtrProps instance = null;

	/**
	 * The file the properties got loaded from (including path info).
	 */
	private String propsFile = "";

	/**
	 * Get the singleton <code>PropsConfiguration</code> instance.
	 * 
	 * @return PropertiesConfiguration instance such as : AtmProps
	 */
	public static final HtrProps getInstance() {
		if (instance == null) {
			instance = new HtrProps();
			instance.loadProperties();
		}
		return instance;
	}

	/**
	 * Load the engine properties. This method searches for the properties
	 * resource in a number of places and uses the
	 * <code>Class.getResourceAsStream</code> and the
	 * <code>Properties.load</code> method to load them.
	 */
	private void loadProperties() {
		try {

			File fileProperties = null;
			Class c = getClass();

			for (int i = 0; i < SEARCH_PATH.length && fileProperties == null; i++) {
				propsFile = SEARCH_PATH[i] + PROPS_RESOURCE;

				URL url = c.getResource(propsFile);

				if (url == null)
					continue;

				fileProperties = new File(url.getFile());

			}

			if (fileProperties != null) {
				loadProperties(fileProperties);
			} else {
				log.warn("Could not find API properties to load");
			}

		} catch (IOException ioe) {
			log.warn("Could not load engine properties", ioe);
		}
	}
	/**
	 * Load the properties from a stream. This method actually just calls
	 * <code>Properties.load</code> but includes some useful debugging output
	 * too.
	 * 
	 * @param is
	 *            File
	 * @throws IOException
	 * @throws IOException
	 */
	private void loadProperties(File fileProperties) throws IOException {
		try {
			instance.setFile(fileProperties);
			instance.setReloadingStrategy(new FileChangedReloadingStrategy());
			instance.setAutoSave(true);

			instance.load();

			log.info("Loaded engine properties from " + propsFile);
		} catch (Exception e) {
			throw new IOException("Failed to load properties from " + propsFile);

		}
	}

	
}
