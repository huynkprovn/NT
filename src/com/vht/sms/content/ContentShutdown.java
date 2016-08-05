package com.vht.sms.content;

import org.apache.commons.logging.*;

// TODO: Auto-generated Javadoc
/**
 * The Class ContentShutdown.
 *
 * @author Hoang Phi Long
 * @version 2.0.1, 17 Aug 2006
 */
public class ContentShutdown extends Thread {
   
   /** The logger. */
   static Log logger = LogFactory.getLog(ContentShutdown.class);

   /**
    * run.
    */
   public void run() {
      logger.info("Shutdown!");
      ContentListener content = ContentListener.getInstance();
      content.stop();
   }
}
