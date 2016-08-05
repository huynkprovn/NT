package com.telix.sms.jms;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Gateway.
 */
public class Gateway implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1;
	
	/** The ID. */
	private long ID;
	
	/** The code. */
	private String code;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The ws link. */
	private String wsLink;
	
	/** The ws target namespace. */
	private String wsTargetNamespace;
	
	/** The ws username. */
	private String wsUsername;
	
	/** The ws password. */
	private String wsPassword;
	
	/** The function. */
	private String function;
	
	/** The ws params. */
	private String wsParams;
	
	/**
	 * Gets the iD.
	 *
	 * @return the iD
	 */
	public long getID() {
		return ID;
	}
	
	/**
	 * Sets the iD.
	 *
	 * @param id the new iD
	 */
	public void setID(long id) {
		ID = id;
	}
	
	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the ws link.
	 *
	 * @return the ws link
	 */
	public String getWsLink() {
		return wsLink;
	}
	
	/**
	 * Sets the ws link.
	 *
	 * @param wsLink the new ws link
	 */
	public void setWsLink(String wsLink) {
		this.wsLink = wsLink;
	}
	
	/**
	 * Gets the ws target namespace.
	 *
	 * @return the ws target namespace
	 */
	public String getWsTargetNamespace() {
		return wsTargetNamespace;
	}
	
	/**
	 * Sets the ws target namespace.
	 *
	 * @param wsTargetNamespace the new ws target namespace
	 */
	public void setWsTargetNamespace(String wsTargetNamespace) {
		this.wsTargetNamespace = wsTargetNamespace;
	}
	
	/**
	 * Gets the ws username.
	 *
	 * @return the ws username
	 */
	public String getWsUsername() {
		return wsUsername;
	}
	
	/**
	 * Sets the ws username.
	 *
	 * @param wsUsername the new ws username
	 */
	public void setWsUsername(String wsUsername) {
		this.wsUsername = wsUsername;
	}
	
	/**
	 * Gets the ws password.
	 *
	 * @return the ws password
	 */
	public String getWsPassword() {
		return wsPassword;
	}
	
	/**
	 * Sets the ws password.
	 *
	 * @param wsPassword the new ws password
	 */
	public void setWsPassword(String wsPassword) {
		this.wsPassword = wsPassword;
	}
	
	/**
	 * Gets the function.
	 *
	 * @return the function
	 */
	public String getFunction() {
		return function;
	}
	
	/**
	 * Sets the function.
	 *
	 * @param function the new function
	 */
	public void setFunction(String function) {
		this.function = function;
	}
	
	/**
	 * Gets the ws params.
	 *
	 * @return the ws params
	 */
	public String getWsParams() {
		return wsParams;
	}
	
	/**
	 * Sets the ws params.
	 *
	 * @param wsParams the new ws params
	 */
	public void setWsParams(String wsParams) {
		this.wsParams = wsParams;
	}
	
	
}
