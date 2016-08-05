package com.vht.sms.content.util;

import com.vht.sms.content.db.DBInteractive;



// TODO: Auto-generated Javadoc
/**
 * The Class AlertUtils.
 */
public class AlertUtils {

	/**
	 * Save alert.
	 *
	 * @param code the code
	 * @param domain the domain
	 * @param issue the issue
	 * @param alertMsg the alert msg
	 * @param alertStatus the alert status
	 */
	public static void saveAlert(String code,
								 String domain,
								 String issue,
								 String alertMsg,
								 String alertStatus){
		DBInteractive.getInstance().saveAlert(code, domain, issue, alertMsg, alertStatus);
	}	
}
