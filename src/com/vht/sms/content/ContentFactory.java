package com.vht.sms.content;

import java.util.Collection;
import java.util.Iterator;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telix.sms.jms.DeliverMessage;
import com.telix.sms.jms.ServiceLocator;
import com.telix.sms.jms.SubmitMessage;
import com.vht.sms.content.db.DBInteractive;
import com.vht.sms.content.util.AlertUtils;
import com.vht.sms.content.util.MessageUtils;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Content objects.
 *
 * @author Hoang Phi Long
 * @version 2.0.1, 17 Aug 2006
 */
public class ContentFactory implements Runnable {
   
   /** The logger. */
   static Log logger = LogFactory.getLog(ContentFactory.class);
   static ContentProps props = ContentProps.getInstance();
   
   /** The dm. */
   private DeliverMessage dm = null; 

   /**
    * constructor.
    *
    * @param dm DeliverMessage
    */
   public ContentFactory(DeliverMessage dm) {
      this.dm = dm;
   }

   /**
    * run.
    */
	public void run() {
		QueueConnection conn = null;
		QueueSession session = null;
		QueueSender sender = null;
		
		logger.info("Start process DM...");
		
		try {
			// process the delive message and send response messages
			Collection sms = getSubmitMessages(dm);
			if (sms != null) {
				String operator = "";
				String queueIsSubmited = "queue/submitws"; // default queue

				Iterator iter = sms.iterator();
				while (iter.hasNext()) {
					SubmitMessage sm = (SubmitMessage) iter.next();
					operator = sm.getMobileOperator();
					break;
				}
				logger.info(operator + "is submited to queue:"
						+ queueIsSubmited);

				QueueConnectionFactory queueFactory = ServiceLocator
						.getInstance().getQueueConnectionFactory(
								ServiceLocator.CONNECTION_FACTORY_JNDI);

				Queue queue = ServiceLocator.getInstance().getQueue(
						queueIsSubmited);

				// connect to submit queue
				conn = queueFactory.createQueueConnection();
				session = conn.createQueueSession(false,
						QueueSession.AUTO_ACKNOWLEDGE);
				sender = session.createSender(queue);

				Iterator iter1 = sms.iterator();
				while (iter1.hasNext()) {
					SubmitMessage sm = (SubmitMessage) iter1.next();

					logger.info("ContentType: " + sm.getContentType());
					logger.info("Send message: " + sm);
					ObjectMessage obj = session.createObjectMessage(sm);
					sender.send(obj);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (JMSException jmse) {
				}
			}
		}
	}

   /**
    * getSubmitMessages.
    *
    * @param dm DeliverMessage
    * @return Collection
    */
   public static Collection getSubmitMessages(DeliverMessage dm) {
      Collection sms = null;
      SubmitMessage sm = null;
	  ContentAbstract delegate = null;
	  String className = null;
		
      try {
    	  	//className = props.getString(dm.getCommandCode()+ "." + dm.getServiceId() + "." + "content.class",null);
			
    	  	className = DBInteractive.getInstance().getClassName(dm.getCommandCode(), dm.getServiceId());
    	  	
			if(className == null){
				className = "com.vht.sms.content.help.Help";
			}
			
			Class delegateClass = Class.forName(className);
			Object delegateObject = delegateClass.newInstance();
			delegate = (ContentAbstract) delegateObject;

			sms = delegate.getSubmitMessages(dm);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(">>>Error getSM : Can't found Class " + className);
			logger.error(e.getMessage());

		}
      
      return sms;
   }   
}
