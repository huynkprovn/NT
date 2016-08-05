package com.vht.sms.content.util;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;
import org.apache.axis2.transport.http.HttpTransportProperties.Authenticator;
import org.apache.axis2.util.Base64;

// TODO: Auto-generated Javadoc
/**
 * The Class CallWebServiceUtils.
 */
public class CallWebServiceUtils {

	/**
	 * Call.
	 *
	 * @param link the link
	 * @param targetNameSpace the target name space
	 * @param function the function
	 * @param userName the user name
	 * @param password the password
	 * @param objects the objects
	 * @return the string
	 */
	public static String call(String link, String targetNameSpace,
			String function, String userName, String password, Object[] objects) {
		String re = null;
		try {
			ServiceClient serviceClient = new ServiceClient();
			Options options = serviceClient.getOptions();

			if (userName != null && !userName.equals("") && password != null) {
				Authenticator auth = new Authenticator();
				auth.setUsername(userName);
				auth.setPassword(password);
				auth.setPreemptiveAuthentication(true);
				serviceClient.setOptions(options);
				serviceClient
						.getOptions()
						.setProperty(
								org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE,
								auth);
			}
			EndpointReference linkws = new EndpointReference(link);
			options.setTo(linkws);
			QName qName = new QName(targetNameSpace, function);
			OMElement request = BeanUtil.getOMElement(qName, objects, null,
					false, null);
			OMElement response = serviceClient.sendReceive(request);

			Class[] returnTypes = new Class[] { String.class };

			Object[] result = BeanUtil.deserialize(response, returnTypes,
					new DefaultObjectSupplier());
			if (result != null) {
				re = (String) result[0];
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		return re;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// System.out.println(CallWebServiceUtils.call(
		// "http://112.78.7.137:8686/vht/services/sms?wsdl",
		// "http://core.brand.ws.dnv.com", "send", null, null,
		// new Object[] { "d015ca", "phankhang", "84947579583",
		// "Phan Khang", "Test from VHT", "Test", "12/16/2010",
		// "Test" });
		long a = System.currentTimeMillis();
		System.out
				.println(CallWebServiceUtils.call(
						"http://203.162.71.82/wsicom/services/Receiver",
						"http://203.162.71.82/wsicom/services/Receiver",
						"mtReceiver",
						"vht8x51",
						"jhj09uuii6ll",
						new Object[] {
								"84947579583",
								Base64.encode("Ban da cung VHT bau chon.."
										.getBytes()), "8051", "CMO", "1",
								"12791", "1", "1", "0", "0", "GPC" }));
		long b = System.currentTimeMillis();
		System.out.println((a - b));

	}
}
