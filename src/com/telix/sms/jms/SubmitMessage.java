package com.telix.sms.jms;

import java.io.Serializable;
import java.sql.Timestamp;

// TODO: Auto-generated Javadoc
/**
 * The Class SubmitMessage.
 */
public class SubmitMessage implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1;
	
	/** The id. */
	long id;
	
	/** The user id. */
	String userId;
	
	/** The service id. */
	String serviceId;
	
	/** The mobile operator. */
	String mobileOperator;
	
	/** The command code. */
	String commandCode;
	
	/** The message. */
	byte message[];
	
	/** The receive date. */
	Timestamp receiveDate;
	
	/** The request id. */
	String requestId;
	
	/** The cp id. */
	long cpId;
	
	/** The message type. */
	int messageType;
	
	/** The retries num. */
	int retriesNum;
	
	/** The has process. */
	int hasProcess;
	
	/** The content type. */
	int contentType;
	
	/** The total message. */
	int totalMessage;
	
	/** The message index. */
	int messageIndex;
	
	/** The is more. */
	int isMore;
	
	/** The message id. */
	String messageId;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(id + "|");
		sb.append(userId + "|");
		sb.append(serviceId + "|");
		sb.append(mobileOperator + "|");
		sb.append(commandCode + "|");
		sb.append(receiveDate + "|");
		sb.append(requestId + "|");
		sb.append(cpId + "|");
		sb.append(messageType + "|");
		sb.append(retriesNum + "|");
		sb.append(hasProcess + "|");
		sb.append(contentType + "|");
		sb.append(totalMessage + "|");
		sb.append(messageIndex + "|");
		sb.append(isMore + "|");
		sb.append(messageId + "|");
		sb.append("\n" + new String(message));
		
		return sb.toString();
	}


	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public byte[] getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(byte[] message) {
		this.message = message;
	}

	/**
	 * Gets the message id.
	 *
	 * @return the message id
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * Sets the message id.
	 *
	 * @param messageId the new message id
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the service id.
	 *
	 * @return the service id
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * Sets the service id.
	 *
	 * @param serviceId the new service id
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * Gets the mobile operator.
	 *
	 * @return the mobile operator
	 */
	public String getMobileOperator() {
		return mobileOperator;
	}

	/**
	 * Sets the mobile operator.
	 *
	 * @param mobileOperator the new mobile operator
	 */
	public void setMobileOperator(String mobileOperator) {
		this.mobileOperator = mobileOperator;
	}

	/**
	 * Gets the command code.
	 *
	 * @return the command code
	 */
	public String getCommandCode() {
		return commandCode;
	}

	/**
	 * Sets the command code.
	 *
	 * @param commandCode the new command code
	 */
	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}
	
	/**
	 * Gets the receive date.
	 *
	 * @return the receive date
	 */
	public Timestamp getReceiveDate() {
		return receiveDate;
	}

	/**
	 * Sets the receive date.
	 *
	 * @param receiveDate the new receive date
	 */
	public void setReceiveDate(Timestamp receiveDate) {
		this.receiveDate = receiveDate;
	}

	/**
	 * Gets the request id.
	 *
	 * @return the request id
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * Sets the request id.
	 *
	 * @param requestId the new request id
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * Gets the cp id.
	 *
	 * @return the cp id
	 */
	public long getCpId() {
		return cpId;
	}

	/**
	 * Sets the cp id.
	 *
	 * @param cpId the new cp id
	 */
	public void setCpId(long cpId) {
		this.cpId = cpId;
	}

	/**
	 * Gets the message type.
	 *
	 * @return the message type
	 */
	public int getMessageType() {
		return messageType;
	}

	/**
	 * Sets the message type.
	 *
	 * @param messageType the new message type
	 */
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	/**
	 * Gets the retries num.
	 *
	 * @return the retries num
	 */
	public int getRetriesNum() {
		return retriesNum;
	}

	/**
	 * Sets the retries num.
	 *
	 * @param retriesNum the new retries num
	 */
	public void setRetriesNum(int retriesNum) {
		this.retriesNum = retriesNum;
	}

	/**
	 * Gets the checks for process.
	 *
	 * @return the checks for process
	 */
	public int getHasProcess() {
		return hasProcess;
	}

	/**
	 * Sets the checks for process.
	 *
	 * @param hasProcess the new checks for process
	 */
	public void setHasProcess(int hasProcess) {
		this.hasProcess = hasProcess;
	}

	/**
	 * Gets the content type.
	 *
	 * @return the content type
	 */
	public int getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 *
	 * @param contentType the new content type
	 */
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	/**
	 * Gets the total message.
	 *
	 * @return the total message
	 */
	public int getTotalMessage() {
		return totalMessage;
	}

	/**
	 * Sets the total message.
	 *
	 * @param totalMessage the new total message
	 */
	public void setTotalMessage(int totalMessage) {
		this.totalMessage = totalMessage;
	}

	/**
	 * Gets the message index.
	 *
	 * @return the message index
	 */
	public int getMessageIndex() {
		return messageIndex;
	}

	/**
	 * Sets the message index.
	 *
	 * @param messageIndex the new message index
	 */
	public void setMessageIndex(int messageIndex) {
		this.messageIndex = messageIndex;
	}

	/**
	 * Gets the checks if is more.
	 *
	 * @return the checks if is more
	 */
	public int getIsMore() {
		return isMore;
	}

	/**
	 * Sets the checks if is more.
	 *
	 * @param isMore the new checks if is more
	 */
	public void setIsMore(int isMore) {
		this.isMore = isMore;
	}

}
