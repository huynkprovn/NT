package com.vht.sms.content.util;

import java.io.PrintStream;
import java.util.Random;
import java.util.StringTokenizer;

// TODO: Auto-generated Javadoc
/**
 * 
 * <p> Class: StringUtils </p>
 * <p> Package: com.vht.sms.content.util </p> 
 * <p> Author: Nguyen Van Doi </p>
 * <p> Update by: Nguyen Van Doi </p>
 * <p> Version: $1.0 </p>
 * <p> Create date: Oct 28, 2010 2:30:33 PM </p>
 * <p> Update date: Oct 28, 2010 2:30:33 PM </p>
 *
 */

public class StringUtils {

	/**
	 * Instantiates a new string utils.
	 */
	public StringUtils() {
	}

	/**
	 * Bytes to hex string.
	 *
	 * @param theData the the data
	 * @return the string
	 */
	public static String bytesToHexString(byte theData[]) {
		StringBuffer hexStrBuff = new StringBuffer(theData.length * 2);
		for (int i = 0; i < theData.length; i++) {
			String hexByteStr = Integer.toHexString(theData[i] & 0xff)
					.toUpperCase();
			if (hexByteStr.length() == 1)
				hexStrBuff.append("0");
			hexStrBuff.append(hexByteStr);
		}

		return hexStrBuff.toString();
	}

	/**
	 * Gets the object message.
	 *
	 * @param Message the message
	 * @return the object message
	 */
	public static String[] getObjectMessage(String Message) {
		StringTokenizer tokenizer = new StringTokenizer(Message.toUpperCase(),
				" ");
		String returns[] = new String[tokenizer.countTokens()];
		int i = 0;
		while (tokenizer.hasMoreElements())
			returns[i++] = tokenizer.nextToken();
		return returns;
	}

	/**
	 * Gets the object message.
	 *
	 * @param Message the message
	 * @param pre the pre
	 * @return the object message
	 */
	public static String[] getObjectMessage(String Message, String pre) {
		StringTokenizer tokenizer = new StringTokenizer(Message.toUpperCase(),
				pre);
		String returns[] = new String[tokenizer.countTokens()];
		int i = 0;
		while (tokenizer.hasMoreElements())
			returns[i++] = tokenizer.nextToken();
		return returns;
	}

	/**
	 * Gets the day hour minute.
	 *
	 * @param dayhourminutesecond the dayhourminutesecond
	 * @return the day hour minute
	 */
	public static String getDayHourMinute(String dayhourminutesecond) {
		int temp = Integer.parseInt(dayhourminutesecond);
		int day = temp / 1440;
		int hour = 0;
		int minutes = 0;
		String returns = "";
		if (day > 0) {
			returns = returns + day + " ngay ";
			int modday = temp % 1440;
			if (modday > 0) {
				hour = modday / 60;
				if (hour > 0) {
					minutes = modday % 60;
					returns = returns + "," + hour + " gio";
					if (minutes > 0)
						returns = returns + ", " + minutes + " phut ";
				} else {
					minutes = modday;
					returns = returns + ", " + minutes + " phut ";
				}
			}
		} else {
			hour = temp / 60;
			if (hour > 0) {
				returns = returns + hour + " gio ";
				int modminutes = temp % 60;
				if (modminutes > 0) {
					minutes = modminutes;
					returns = returns + ", " + minutes + " phut ";
				}
			} else {
				minutes = temp;
				returns = returns + minutes + " phut ";
			}
		}
		return returns;
	}

	/**
	 * Gets the gateway id.
	 *
	 * @param phoneNumber the phone number
	 * @return the gateway id
	 */
	public static String getGatewayID(String phoneNumber) {
		if (phoneNumber.startsWith("+"))
			phoneNumber = phoneNumber.replace("+", "");
		if (phoneNumber.startsWith("8498") || phoneNumber.startsWith("8497")
				|| phoneNumber.startsWith("84168"))
			return "vtl-8x04";
		if (phoneNumber.startsWith("8490") || phoneNumber.startsWith("8493")
				|| phoneNumber.startsWith("84122"))
			return "vms-8x04";
		if (phoneNumber.startsWith("8491") || phoneNumber.startsWith("8494"))
			return "vnp-8x04";
		else
			return "vms-8x04";
	}

	/**
	 * Byte to hex string.
	 *
	 * @param theByte the the byte
	 * @return the string
	 */
	public static String byteToHexString(byte theByte) {
		StringBuffer hexStrBuff = new StringBuffer(2);
		String hexByteStr = Integer.toHexString(theByte & 0xff).toUpperCase();
		if (hexByteStr.length() == 1)
			hexStrBuff.append("0");
		hexStrBuff.append(hexByteStr);
		return hexStrBuff.toString();
	}

	/**
	 * Hex string to bytes.
	 *
	 * @param theHexString the the hex string
	 * @return the byte[]
	 */
	public static byte[] hexStringToBytes(String theHexString) {
		byte data[] = new byte[theHexString.length() / 2];
		for (int i = 0; i < data.length; i++) {
			String a = theHexString.substring(i * 2, i * 2 + 2);
			data[i] = (byte) Integer.parseInt(a, 16);
		}

		return data;
	}

	/**
	 * Replace white letter.
	 *
	 * @param sInput the s input
	 * @return the string
	 */
	public static String replaceWhiteLetter(String sInput) {
		String strTmp = sInput;
		String sReturn = "";
		boolean flag = true;
		int i;
		for (i = 0; i < sInput.length() && flag; i++) {
			char ch = sInput.charAt(i);
			if (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'Z' || ch >= 'a'
					&& ch <= 'z')
				flag = false;
			else
				strTmp = sInput.substring(i + 1);
		}

		i = strTmp.length() - 1;
		flag = true;
		sReturn = strTmp;
		for (; i >= 0 && flag; i--) {
			char ch = strTmp.charAt(i);
			if (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'Z' || ch >= 'a'
					&& ch <= 'z')
				flag = false;
			else
				sReturn = strTmp.substring(0, i);
		}

		return sReturn;
	}

	/**
	 * Gets the object string.
	 *
	 * @param msg the msg
	 * @param index the index
	 * @return the object string
	 */
	public static String getObjectString(String msg, int index) {
		String sReturn = "";
		String sTmp = replaceWhiteLetter(msg);
		int i = getIndexOfSeparate(sTmp, 0);
		if (i < 0)
			return null;
		sTmp = sTmp.substring(i + 1);
		sTmp = replaceWhiteLetter(sTmp);
		i = getIndexOfSeparate(sTmp, 0);
		switch (index) {
		default:
			break;

		case 1: // '\001'
			if (i < 0)
				sReturn = sTmp.toUpperCase();
			else
				sReturn = sTmp.substring(0, i).toUpperCase();
			break;

		case 2: // '\002'
			if (i < 0)
				return null;
			sTmp = sTmp.substring(i + 1);
			sTmp = replaceWhiteLetter(sTmp);
			i = getIndexOfSeparate(sTmp, 0);
			if (i < 0)
				sReturn = sTmp.toUpperCase();
			else
				sReturn = sTmp.substring(0, i).toUpperCase();
			break;

		case 3: // '\003'
			if (i < 0)
				return null;
			sTmp = sTmp.substring(i + 1);
			sTmp = replaceWhiteLetter(sTmp);
			i = getIndexOfSeparate(sTmp, 0);
			if (i < 0)
				return null;
			sTmp = sTmp.substring(i + 1);
			sTmp = replaceWhiteLetter(sTmp);
			i = getIndexOfSeparate(sTmp, 0);
			if (i < 0)
				sReturn = sTmp.toUpperCase();
			else
				sReturn = sTmp.substring(0, i).toUpperCase();
			break;

		case 4: // '\004'
			if (i < 0)
				return null;
			sTmp = sTmp.substring(i + 1);
			sTmp = replaceWhiteLetter(sTmp);
			i = getIndexOfSeparate(sTmp, 0);
			if (i < 0)
				return null;
			sTmp = sTmp.substring(i + 1);
			sTmp = replaceWhiteLetter(sTmp);
			i = getIndexOfSeparate(sTmp, 0);
			if (i < 0)
				return null;
			sTmp = sTmp.substring(i + 1);
			sTmp = replaceWhiteLetter(sTmp);
			i = getIndexOfSeparate(sTmp, 0);
			if (i < 0)
				sReturn = sTmp.toUpperCase();
			else
				sReturn = sTmp.substring(0, i).toUpperCase();
			break;
		}
		return sReturn;
	}

	/**
	 * Gets the index of separate.
	 *
	 * @param strInput the str input
	 * @param type the type
	 * @return the index of separate
	 */
	public static int getIndexOfSeparate(String strInput, int type) {
		if (type == 1) {
			for (int i = strInput.length() - 1; i >= 0; i--) {
				char ch = strInput.charAt(i);
				if (ch < '0' || ch > '9' && ch < 'A' || ch > 'Z' && ch < 'a'
						|| ch > 'z')
					return i;
			}

		} else {
			for (int i = 0; i < strInput.length(); i++) {
				char ch = strInput.charAt(i);
				if (ch < '0' || ch > '9' && ch < 'A' || ch > 'Z' && ch < 'a'
						|| ch > 'z')
					return i;
			}

		}
		return -1;
	}

	/**
	 * Generate user id.
	 *
	 * @param id the id
	 * @return the string
	 */
	public static String generateUserID(long id) {
		int k = (int) id / 10000;
		int m = (int) id % 10000;
		int n = 0;
		int j = 0;
		String s = null;
		if (id >= 0x672640L) {
			n = (k / 26) % 26;
			j = k / 26 / 26 - 1;
			System.out.println("<" + n);
			System.out.println("<<" + j);
			k %= 26;
			s = String.valueOf((char) (65 + j))
					+ String.valueOf((char) (65 + n))
					+ String.valueOf((char) (65 + k)) + m;
		} else if (id >= 0x3f7a0L) {
			n = k / 26 - 1;
			k %= 26;
			s = String.valueOf((char) (65 + n))
					+ String.valueOf((char) (65 + k)) + m;
		} else {
			s = String.valueOf((char) (65 + k)) + m;
		}
		return s;
	}

	/**
	 * Generate random.
	 *
	 * @param input the input
	 * @return the int
	 */
	public int generateRandom(int input) {
		Random generator = new Random();
		return generator.nextInt(input) + 1;
	}

	/**
	 * Check number.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public static boolean checkNumber(String message) {
		if (message == null)
			return false;
		for (int i = message.length() - 1; i >= 0; i--) {
			char ch = message.charAt(i);
			if (ch < '0' || ch > '9')
				return false;
		}

		return true;
	}

	/**
	 * Check number charater.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public static boolean checkNumberCharater(String message) {
		if (message == null)
			return false;
		for (int i = message.length() - 1; i >= 0; i--) {
			char ch = message.charAt(i);
			if (ch < '0' || ch > '9' && ch < 'A' || ch > 'Z' && ch < 'a'
					|| ch > 'z')
				return false;
		}

		return true;
	}
	
	/**
	 * Gets the object message upper case.
	 *
	 * @param Message the message
	 * @return the object message upper case
	 */
	public static String[] getObjectMessageUpperCase(String Message){
		StringTokenizer tokenizer = new StringTokenizer(Message.toUpperCase(), " ");
		String returns[] = new String[tokenizer.countTokens()];
		int i = 0;
		while(tokenizer.hasMoreElements()){
			returns[i++] = tokenizer.nextToken();
		}
		return returns;
	}
	
	/**
	 * Gets the object message not upper case.
	 *
	 * @param Message the message
	 * @return the object message not upper case
	 */
	public static String[] getObjectMessageNotUpperCase(String Message){
		StringTokenizer tokenizer = new StringTokenizer(Message.toUpperCase(), " ");
		String returns[] = new String[tokenizer.countTokens()];
		int i = 0;
		while(tokenizer.hasMoreElements()){
			returns[i++] = tokenizer.nextToken();
		}
		return returns;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String args[]) {
		StringUtils t = new StringUtils();
		System.out.println(t.checkNumberCharater("dfasAFSDFDSFSDasdf56745"));
	}
}