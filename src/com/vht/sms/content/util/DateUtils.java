package com.vht.sms.content.util;

// TODO: Auto-generated Javadoc
/**
 * The Class DateUtils.
 */
public class DateUtils {
	
	/**
	 * Creates the timestamp.
	 *
	 * @return the java.sql. timestamp
	 */
	public static java.sql.Timestamp createTimestamp() {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		return new java.sql.Timestamp((calendar.getTime()).getTime());
	}
}
