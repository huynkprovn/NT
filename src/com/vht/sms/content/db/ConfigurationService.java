package com.vht.sms.content.db;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * 
 * <p> Class: ConfigurationService </p>
 * <p> Package: com.vht.sms.db </p> 
 * <p> Author: Nguyen Van Doi </p>
 * <p> Update by: Nguyen Van Doi </p>
 * <p> Version: $1.0 </p>
 * <p> Create date: Oct 28, 2010 4:18:36 PM </p>
 * <p> Update date: Oct 28, 2010 4:18:36 PM </p>
 *
 */

public class ConfigurationService extends PropertiesConfiguration { 
	   
   	/** The logger. */
   	private static Log logger = LogFactory.getLog(ConfigurationService.class);
	   
   	/** The Constant PROPS_RESOURCE. */
   	private static final String PROPS_RESOURCE = "vhtcms.properties";
	   
   	/** The instance. */
   	private static ConfigurationService instance = null;
	   
   	/** The props file. */
   	private String propsFile = PROPS_RESOURCE;
	   
   	/**
   	 * Gets the single instance of ConfigurationService.
   	 *
   	 * @return single instance of ConfigurationService
   	 */
   	public static final ConfigurationService getInstance() {
		      if (instance == null) {
		         instance = new ConfigurationService();
		         instance.loadProperties();
		      }
		      return instance;
		   }
	   
   	/**
   	 * Load properties.
   	 */
   	private void loadProperties() {
		      try {
		         File is = null;
		        // Class c = getClass();
                 is = new File(propsFile);
		         if (is != null) {
		            loadProperties(is);
		         }
		         else {
		            logger.warn("Could not find API properties to load");
		         }

		      }
		      catch (Exception ioe) {
		         logger.warn("Could not load engine properties", ioe);
		      }
		   }

	   /**
   	 * Load properties.
   	 *
   	 * @param is the is
   	 * @throws IOException Signals that an I/O exception has occurred.
   	 */
   	private void loadProperties(File is)throws IOException {
	      try {
	         instance.setFile(is);
	         instance.setReloadingStrategy(new FileChangedReloadingStrategy());
	         instance.setAutoSave(true);
	         instance.load();
	         logger.info("Loaded engine properties from " + propsFile);
	      }
	      catch (Exception e) {
	         throw new IOException("Failed to load properties from " + propsFile);
	      }
	   }
}
