package com.vht.sms.content.dmvt;

import com.telix.sms.jms.DeliverMessage;
import com.vht.sms.content.ContentAbstract;
import com.vht.sms.content.util.MessageUtils;
import com.vht.sms.content.util.MyConst;
import com.vht.sms.content.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DmvtService extends ContentAbstract {
	static Log logger = LogFactory.getLog(DmvtService.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static DmvtProps props = DmvtProps.getInstance();

	@Override
	protected Collection getMessages(DeliverMessage dm) throws Exception {
		if (dm == null) {
			return new ArrayList<String>();
		}
		try {
			messageType = MyConst.CHARGE;
			contentType = MyConst.TEXT;
			retriesNum = MyConst.SUCCESS;
			String[] tokens = StringUtils.getObjectMessageUpperCase(dm.getMessage());
			if(tokens.length > 1){
				String msg = callApiMsg(dm, tokens[1]);
				logger.info("response :   " + msg);
				JSONObject jsonObject = new JSONObject(msg);
				if(jsonObject.has("message")){ 
					msg = jsonObject.getString("message");
					return MessageUtils.getTextMessages(msg);
				} else {
					return MessageUtils.getTextMessages(props.getString("message.timeout"));
				}
			} else {
				return MessageUtils.getTextMessages(props.getString("message.fail"));
			}
		} catch (Exception e) {
			logger.error(">>>Message timeout!!! DMVT " + e.getMessage());
			messageType = MyConst.CHARGE;
			contentType = MyConst.TEXT;
			retriesNum = MyConst.TIMEOUT;
			return MessageUtils.getTextMessages(props.getString("message.timeout"));
		}
	}
	
	public static String callApiMsg(DeliverMessage dm, String code) {
		String response = "";
		String link = props.getString("link");
		String user = props.getString("user");
		String pass = props.getString("pass");
		String telcos = dm.getMobileOperator();
		if (telcos.equalsIgnoreCase("VMS")) {
			telcos = "Mobifone";
		} else if(telcos.equalsIgnoreCase("GPC")) {
			telcos ="Vinaphone";
		} else if(telcos.equalsIgnoreCase("BEELINE")) {
			telcos ="Gmoblie";
		} 
		
		try { 
			URL urlAgr = new URL(link);
			List<HttpHeader> requestList = new ArrayList<HttpHeader>();
			requestList.add(new HttpHeader("phone", dm.getUserId(), false));
			requestList.add(new HttpHeader("code", code, false));
			requestList.add(new HttpHeader("shortcode", dm.getServiceId(), false));
			requestList.add(new HttpHeader("telco", telcos, false));
			requestList.add(new HttpHeader("date_time", dm.getReceiveDate()+"", false));
			requestList.add(new HttpHeader("mo_id", dm.getId()+"", false));
			requestList.add(new HttpHeader("user_name", user, false));
			requestList.add(new HttpHeader("password", pass, false));
			List<String> responseList = HTTPGateway.HttpPost(urlAgr, requestList);
			for(String item : responseList){
				if(!item.isEmpty()){		
						return item;
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
			return props.getString("message.timeout");
		}
		return response;
	}
	public static void main(String[] args) {
		DmvtService dmvt = new DmvtService();
		DeliverMessage dm = new DeliverMessage();
		dm.setCommandCode("DMVT");
		dm.setMessage("DMVT 1");
		dm.setMobileOperator("VMS");
		dm.setServiceId("6289");
		dm.setUserId("84937145822");
		try {
			dmvt.getMessages(dm);
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
}
