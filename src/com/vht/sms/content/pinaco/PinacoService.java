package com.vht.sms.content.pinaco;


import com.telix.sms.jms.DeliverMessage;
import com.vht.sms.content.ContentAbstract;
import com.vht.sms.content.util.MessageUtils;
import com.vht.sms.content.util.MyConst;
import com.vht.sms.content.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PinacoService extends ContentAbstract {
	static Log logger = LogFactory.getLog(PinacoService.class);
	private static PinacoProps props = PinacoProps.getInstance();
	
	@Override
	protected Collection getMessages(DeliverMessage dm) throws Exception {
		messageType = MyConst.CHARGE;
		contentType = MyConst.TEXT;
		retriesNum = MyConst.SUCCESS;
		if (dm == null) {
			return new ArrayList<String>();
		}
		try {
			messageType = MyConst.CHARGE;
			contentType = MyConst.TEXT;
			retriesNum = MyConst.SUCCESS;
			String[] tokens = StringUtils.getObjectMessageUpperCase(dm.getMessage());
			String response = null;
			if(tokens.length > 1){
				response = callWs(dm);
				logger.info("response :   " + response);
				JSONObject jsonObject = new JSONObject(response);
				if(jsonObject.has("message")){ 
					return MessageUtils.getTextMessages(jsonObject.getString("message"));
				} else {
					return MessageUtils.getTextMessages(props.getString("message.timeout"));
				}
			} else {
				return MessageUtils.getTextMessages(props.getString("message.timeout"));
			}
		} catch (Exception e) {
			logger.error(">>>Message timeout!!! Sharp:   " + e.getMessage());
			messageType = MyConst.CHARGE;
			contentType = MyConst.TEXT;
			retriesNum = MyConst.TIMEOUT;
			return MessageUtils.getTextMessages(props.getString("message.timeout"));
		}
	}
	public synchronized String callWs(DeliverMessage dm) {
		String link = props.getString("link");
		try { 
			URL urlAgr = new URL(link);
			List<HttpHeader> requestList = new ArrayList<HttpHeader>();
			requestList.add(new HttpHeader("phone", dm.getUserId(), false));
			requestList.add(new HttpHeader("telco", dm.getMobileOperator(), false));
			requestList.add(new HttpHeader("mo_content", dm.getMessage(), false));
			requestList.add(new HttpHeader("service", dm.getCommandCode(), false));
			requestList.add(new HttpHeader("service_number", dm.getServiceId(), false));
			
			List<String> responseList = HTTPGateway.HttpPost(urlAgr, requestList);
			
			if (responseList.size()>0) {
				StringBuilder builder = new StringBuilder("");
				for(String item : responseList){
					builder.append(item);
				}	
				return builder.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return props.getString("message.timeout");
		}
		return props.getString("message.timeout");
	}

	public static void main(String[] args) {
		PinacoService asv = new PinacoService();
		DeliverMessage dm = new DeliverMessage();
		dm.setCommandCode("PAC");
		dm.setMessage("PAC 02598203");
		dm.setMobileOperator("VIETTEL ");
		dm.setServiceId("6089");
		dm.setUserId("84937145822");
		try {
			asv.getMessages(dm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
