package com.vht.sms.content.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.*;

import com.telix.pdu.sms.SmsPdu;
import com.telix.sms.jms.SubmitMessage;
import com.vht.sms.content.db.LocalDatabasePooling;

import org.apache.commons.lang.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageUtils.
 *
 * @author Hoang Phi Long
 * @version 1.0.1, 15 Aug 2006
 */
public class MessageUtils {
	
	/** The log. */
	static Log log = LogFactory.getLog(MessageUtils.class);

	/**
	 * getMessages.
	 *
	 * @param message String
	 * @param maxMessageLen boolean
	 * @return Collection
	 */

	public static Collection getTextMessages(String message, int maxMessageLen) {
		Collection msgs = new ArrayList();

		if (message.length() <= maxMessageLen) {
			msgs.add(message.getBytes());
			return msgs;
		}

		String wrap = WordUtils.wrap(message, maxMessageLen - 4, "{sep}", true);
		String[] lines = wrap.split("\\{sep\\}");

		String msg = "";
		for (int i = 0; i < lines.length; i++) {
			msg = lines[i].trim() + "\n" + (i + 1) + "/" + lines.length;
			msgs.add(msg.getBytes());
		}

		return msgs;
	}

	/**
	 * Gets the text messages ln.
	 *
	 * @param message the message
	 * @param maxMessageLen the max message len
	 * @return the text messages ln
	 */
	@Deprecated
	public static Collection getTextMessagesLn(String message, int maxMessageLen) {
		Collection msgs = new ArrayList();
		String msg = message.replaceAll("\n", "");
		if (msg.length() <= maxMessageLen) {
			msgs.add(msg.getBytes());
			return msgs;
		}
		// spit to message with max len - 4
		Collection items = new ArrayList();
		while (msg.length() > maxMessageLen) {
			items.add(msg.substring(0, maxMessageLen));
			msg = msg.substring(maxMessageLen, msg.length());
		}
		if (!"".equals(msg)) {
			items.add(msg.trim());
		}
		// add the pager
		Iterator iter = items.iterator();
		for (int i = 1; iter.hasNext(); i++) {
			String item = (String) iter.next();
			msgs.add(item.getBytes());
		}

		return msgs;
	}

	/**
	 * getTextMessagesLn.
	 *
	 * @param message String
	 * @param maxMessageLen boolean
	 * @return Collection
	 */
	@Deprecated
	public static Collection getTextMessagesLnTemp(String message,
			int maxMessageLen) {
		Collection msgs = new ArrayList();
		if (message.length() <= maxMessageLen) {
			msgs.add(message.getBytes());
			return msgs;
		}
		// spit to message with max len - 4
		String msg = "";
		Collection items = new ArrayList();
		String[] lines = message.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (msg.length() + lines[i].length() < maxMessageLen - 4) {
				msg += "\n" + lines[i];
			} else {
				items.add(msg.trim());
				msg = lines[i];
			}
		}

		if (!"".equals(msg)) {
			items.add(msg.trim());
		}

		// add the pager
		Iterator iter = items.iterator();
		for (int i = 1; iter.hasNext(); i++) {
			String item = ((String) iter.next()) + "\n" + i + "/"
					+ items.size();
			msgs.add(item.getBytes());
		}

		return msgs;
	}

	/**
	 * Gets the text messages.
	 *
	 * @param message the message
	 * @return the text messages
	 */
	public static Collection getTextMessages(String message) {
		System.out.println("message :  " + message);
		Collection msgs = new ArrayList();
		msgs.add(message.getBytes());
		return msgs;
	}

	
	/**
	 * Gets the submit message.
	 *
	 * @param commandCode the command code
	 * @param contentType the content type
	 * @param cpId the cp id
	 * @param hasProcess the has process
	 * @param isMore the is more
	 * @param message the message
	 * @param messageIndex the message index
	 * @param messageType the message type
	 * @param mobileOperator the mobile operator
	 * @param requestId the request id
	 * @param retriesNum the retries num
	 * @param serviceId the service id
	 * @param totalMessage the total message
	 * @param userId the user id
	 * @param messageId the message id
	 * @return the submit message
	 */
	public static SubmitMessage getSubmitMessage(String commandCode, 
												 int contentType, 
												 long cpId,
												 int hasProcess,
												 int isMore,
												 byte[] message,
												 int messageIndex,
												 int messageType,
												 String mobileOperator,
												 String requestId,
												 int retriesNum,
												 String serviceId,
												 int totalMessage,
												 String userId,
												 String messageId) {

		SubmitMessage sm = new SubmitMessage();

		sm.setCommandCode(commandCode);
		sm.setContentType(contentType);
		sm.setCpId(cpId);
		sm.setHasProcess(hasProcess);
		sm.setIsMore(isMore);
		sm.setMessage(message);
		sm.setMessageIndex(messageIndex);
		sm.setMessageType(messageType);
		sm.setMobileOperator(mobileOperator);
		sm.setRequestId(requestId);
		sm.setRetriesNum(retriesNum);
		sm.setServiceId(serviceId);
		sm.setTotalMessage(totalMessage);
		sm.setUserId(userId);
		sm.setMessageId(messageId);

		return sm;
	}

	/**
	 * getByteArray.
	 *
	 * @param pdu SmsPdu
	 * @return byte[] of the given PDU
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] getByteArray(SmsPdu pdu) throws IOException {
		byte[] data = null;

		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			byte[] udh = pdu.getUserDataHeaders();

			if (udh != null)
				baos.write(udh);
			baos.write(pdu.getUserData().getData());

			data = baos.toByteArray();
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException ioe) {
				}
			}
		}
		return data;
	}

	/**
	 * Method to check if given message is a number.
	 *
	 * @param message the message
	 * @return true, if is numeric
	 */
	public static boolean isNumeric(String message) {
		if (message == null)
			return false;

		return message.matches("[0-9]+");
	}
	
	/**
	 * Gets the operator.
	 *
	 * @param userId the user id
	 * @return the operator
	 */
	public static String getOperator(String userId) {
		if (userId == null || userId.trim().length() == 0)
			return null;
		Connection conn = null;
		CallableStatement stmt = null;
		String operator = null;

		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			stmt = conn.prepareCall("{CALL ? := PKG_PROCESS_SERVER.FT_GET_OPERATOR(?)}");
			stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.VARCHAR);
			stmt.setString(2, userId);
			stmt.execute();
			operator = (String) stmt.getObject(1);
		} catch (Exception e) {
			log.error("************************ Error in calling PKG_PROCESS_SERVER.FT_GET_OPERATOR: ****************************************\n");
			log.error(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();

				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.error(e);
			}
		}
		return operator;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		for (int i = 0; i < 1; i++) {
			Collection col = MessageUtils.getTextMessages("Chuc qui vi may man se duoc hawdawdawdawd awde thong chon tham gia tra loi truc tiep cau hoi cua c/trinh CHOI CHU tren kenh HN1. Acecook Viet Nam han hanh tai tro chuong trinh");
			int size = col.size();
			if(col.size() == 2){
				System.out.println("size is 2");
				break;
			}else{
				System.out.println(col.size());
			}
		}		
	}
}
