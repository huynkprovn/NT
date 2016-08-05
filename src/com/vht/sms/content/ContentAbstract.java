package com.vht.sms.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telix.sms.jms.DeliverMessage;
import com.telix.sms.jms.SubmitMessage;
import com.vht.sms.content.db.DBInteractive;
import com.vht.sms.content.util.MessageUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ContentAbstract.
 */
public abstract class ContentAbstract {
	
	
	/** The logger. */
	static Log logger = LogFactory.getLog(ContentAbstract.class);
	
	/** The content type. */
	protected int contentType = 0; // default: text message
	
	/** 0: SUCCESS, 1: ERROR, 2: TIMEOUT */
	protected int retriesNum = 0;
	
	/** The is more. */
	protected int isMore = 0;
	
	/** The message index. */
	protected int messageIndex = 1;
	
	/** The message type. */
	protected int messageType = 1; 
	
	/** 1: Show report, 0: not show report. */
	protected int hasProcess = 1;
	
	protected long cpId = 198;
	/** The message to send. */
	protected ArrayList messageToSend = new ArrayList();
	
	
	
	/**
	 * Gets the submit messages.
	 *
	 * @param dm the dm
	 * @return the submit messages
	 * @throws Exception the exception
	 */
	public Collection getSubmitMessages(DeliverMessage dm) throws Exception {
		Collection sms = new ArrayList();
		SubmitMessage sm = null;

		Collection messages = getMessages(dm);
		if (messages != null) {
			Iterator iter = messages.iterator();
			
			
			for (int i = 1; iter.hasNext(); i++) {
				byte[] message = (byte[]) iter.next();

				sm = MessageUtils.getSubmitMessage(dm.getCommandCode(),
												   contentType,
												   cpId, 
												   hasProcess,
												   (i==messages.size())?0:1, 
												   message, 
												   i, 
												   (i==1)?1:0, 
												   dm.getMobileOperator(), 
												   dm.getRequestId(),
												   retriesNum, 
												   dm.getServiceId(), 
												   messages.size(), 
												   dm.getUserId(), 
												   dm.getId()+"");

				sms.add(sm);
			}
		}
		sms.addAll(messageToSend);
		return sms;
	}
	
	/**
	 * Gets the messages.
	 *
	 * @param dm the dm
	 * @return the messages
	 * @throws Exception the exception
	 */
	protected abstract Collection getMessages(DeliverMessage dm)
			throws Exception;
	
}
