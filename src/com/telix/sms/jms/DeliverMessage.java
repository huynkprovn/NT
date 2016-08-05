package com.telix.sms.jms;

import java.io.Serializable;
import java.sql.Timestamp;



// TODO: Auto-generated Javadoc
/**
 * The Class DeliverMessage.
 */
public class DeliverMessage implements Serializable{

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
	String message;
	
	/** The request id. */
	String requestId;
	
	/** The receive date. */
	Timestamp receiveDate;
	
	/** The retries num. */
	int retriesNum;
	
	/** The has process. */
	int hasProcess;
	
	/** The command prefix. */
	String commandPrefix;
	
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
		sb.append(requestId + "|");
		sb.append(receiveDate + "|");
		sb.append(retriesNum + "|");
		sb.append(hasProcess + "|");
		sb.append(commandPrefix + "|");
		sb.append("\n" + message);
		
		return sb.toString();
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
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
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
	 * Gets the command prefix.
	 *
	 * @return the command prefix
	 */
	public String getCommandPrefix() {
		return commandPrefix;
	}

	/**
	 * Sets the command prefix.
	 *
	 * @param commandPrefix the new command prefix
	 */
	public void setCommandPrefix(String commandPrefix) {
		this.commandPrefix = commandPrefix;
	}

}
