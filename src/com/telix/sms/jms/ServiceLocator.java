package com.telix.sms.jms;

import java.util.Hashtable;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * 
 * <p> Class: ServiceLocator </p>
 * <p> Package: com.telix.sms.jms </p> 
 * <p> Author: Nguyen Van Doi </p>
 * <p> Update by: Nguyen Van Doi </p>
 * <p> Version: $1.0 </p>
 * <p> Create date: Oct 28, 2010 2:21:42 PM </p>
 * <p> Update date: Oct 28, 2010 2:21:42 PM </p>
 *
 */

public class ServiceLocator {
	
	/** The logger. */
	static Log logger = LogFactory.getLog(ServiceLocator.class);
	
	/** The Constant CONNECTION_FACTORY_JNDI. */
	public static final String CONNECTION_FACTORY_JNDI = "UIL2ConnectionFactory";
	
	/** The instance. */
	private static ServiceLocator instance;
	
	/** The ictx. */
	private InitialContext ictx;
	
	/**
	 * Instantiates a new service locator.
	 *
	 * @throws Exception the exception
	 */
	private ServiceLocator() throws Exception {
		try {
			Hashtable props = new Hashtable();
			props.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
			props.put("java.naming.provider.url", "localhost");
			props.put("java.naming.factory.url.pkgs", "org.jboss.naming");
			ictx = new InitialContext(props);
		} catch (NamingException ne) {
			throw new Exception(ne);
		}
	}

	/**
	 * Gets the single instance of ServiceLocator.
	 *
	 * @return single instance of ServiceLocator
	 */
	public static ServiceLocator getInstance() {
		if (instance == null)
			try {
				instance = new ServiceLocator();
			} catch (Exception e) {
				logger.error(e.toString());
			}
		return instance;
	}

	/**
	 * Gets the queue connection factory.
	 *
	 * @param queueFactoryName the queue factory name
	 * @return the queue connection factory
	 * @throws Exception the exception
	 */
	public QueueConnectionFactory getQueueConnectionFactory(
			String queueFactoryName) throws Exception {
		logger.info("Look up the connection factory from JNDI context");
		QueueConnectionFactory queueFactory = null;
		try {
			queueFactory = (QueueConnectionFactory) ictx
					.lookup(queueFactoryName);
		} catch (NamingException ne) {
			throw new Exception(ne.getMessage(), ne);
		}
		return queueFactory;
	}

	/**
	 * Gets the queue.
	 *
	 * @param queueName the queue name
	 * @return the queue
	 * @throws Exception the exception
	 */
	public Queue getQueue(String queueName) throws Exception {
		logger.info("Look up the queue from JNDI context");
		Queue queue = null;
		try {
			queue = (Queue) ictx.lookup(queueName);
		} catch (NamingException ne) {
			throw new Exception(ne.getMessage(), ne);
		}
		return queue;
	}
}