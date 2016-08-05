package com.vht.sms.content;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telix.sms.jms.DeliverMessage;
import com.telix.sms.jms.ServiceLocator;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving content events.
 * The class that is interested in processing a content
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addContentListener<code> method. When
 * the content event occurs, that object's appropriate
 * method is invoked.
 *
 * @author Hoang Phi Long
 * @version 2.0.1, 17 Aug 2006
 */
public class ContentListener implements MessageListener {
   
   /** The logger. */
   static Log logger = LogFactory.getLog(ContentListener.class);
   
   /** The instance. */
   static ContentListener instance = null;

   /** The conn. */
   private QueueConnection conn = null;
   
   /** The session. */
   private QueueSession session = null;
   
   /** The receiver. */
   private QueueReceiver receiver = null;
   
   /**
    * constructor.
    */
   public ContentListener() {
      try {
         // lock up queue and create receiver for deliver messages
         QueueConnectionFactory queueFactory = ServiceLocator.getInstance()
            .getQueueConnectionFactory(ServiceLocator.CONNECTION_FACTORY_JNDI);
         Queue queue = ServiceLocator.getInstance().getQueue("queue/deliverws");

         conn = queueFactory.createQueueConnection();
         session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
         receiver = session.createReceiver(queue);
   }
      catch (Exception e) {
         logger.error(e.getMessage(), e);
      }
   }


   /**
    * getInstance.
    *
    * @return ContentFactory
    */
   public static ContentListener getInstance() {
      if (instance == null) {
         instance = new ContentListener();
      }
      return instance;
   }


   /**
    * start.
    */
   public void start() {
      try {
         receiver.setMessageListener(this);
         conn.start();
      }
      catch (Exception e) {
         logger.error(e.getMessage(), e);
      }
   }


   /**
    * stop.
    */
   public void stop() {
      logger.info("Close the deliver message listener!");
      if (conn != null) {
         try {
            conn.close();
         }
         catch (JMSException jmse) {
         }
      }
   }

   /**
    * Handle messages from deliver queue.
    *
    * @param message Message
    */
   public void onMessage(Message message) {
      DeliverMessage dm = null;
      try {
         // routing message
         if (message instanceof ObjectMessage) {
            dm = (DeliverMessage) ((ObjectMessage) message).getObject();

            if (dm.getServiceId().startsWith("04")) {
               dm.setServiceId(dm.getServiceId().substring(2));
            }
            else {
               dm.setServiceId(dm.getServiceId());
            }

            logger.info("Receive message:\n" + dm);
            ContentFactory factory = new ContentFactory(dm);
            new Thread(factory).start();
         }
      }
      catch (Exception e) {
         logger.error(e.getMessage(), e);
      }
   }

   /**
    * The main method.
    *
    * @param args the arguments
    */
   public static void main(String[] args) {
      Runtime.getRuntime().addShutdownHook(new ContentShutdown());
      ContentListener content = ContentListener.getInstance();
      content.start();
   }
}
