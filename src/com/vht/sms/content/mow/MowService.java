package com.vht.sms.content.mow;


import com.telix.sms.jms.DeliverMessage;
import com.vht.sms.content.ContentAbstract;
import com.vht.sms.content.util.MessageUtils;
import com.vht.sms.content.util.MyConst;
import com.vht.sms.content.util.ReportUtils;
import com.vht.sms.content.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

public class MowService extends ContentAbstract {
	static Log logger = LogFactory.getLog(MowService.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static MowProps props = MowProps.getInstance();
 
	@Override
	protected Collection getMessages(DeliverMessage dm) throws Exception {
		if (dm == null) {
			return new ArrayList<String>();
		}
		try {
			messageType = MyConst.CHARGE;
			contentType = MyConst.TEXT;
			retriesNum = MyConst.SUCCESS;
			
			String [] tokens = StringUtils.getObjectMessageUpperCase(dm.getMessage());
			
			if (this.checkDateBefor()) {
				return MessageUtils.getTextMessages(props.getString("message.befor"));
			}
			if (this.checkDateAfter()) {
				return MessageUtils.getTextMessages(props.getString("message.end"));
			}
			if (tokens.length==1) {
				String ma = randomCharacter(2)+randomNumber(2);
				String msg = props.getString("message.ok");
				msg = msg.replaceAll("<ma>", ma);
				saveDB(dm,1,ma);
				return MessageUtils.getTextMessages(msg);
			} else {
				return MessageUtils.getTextMessages(props.getString("message.timeout"));
			}
			
		} catch (Exception e) {
			logger.error(">>>Message timeout!!! MOW " + e.getMessage());
			messageType = MyConst.CHARGE;
			contentType = MyConst.TEXT;
			retriesNum = MyConst.TIMEOUT;
			return MessageUtils.getTextMessages(props.getString("message.timeout"));
		}
	}
	public static String randomCharacter(int length) {
		String validCharsUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int maxIndex = validCharsUpper.length();
		String resultId = "";
		Random rnd = new Random(System.currentTimeMillis()
				* (new Random().nextInt()));
		for (int i = 0; i < length; i++) {
			int rndPos = Math.abs(rnd.nextInt() % maxIndex);
			resultId += validCharsUpper.charAt(rndPos);
		}

		return resultId;
	}
	public static String randomNumber(int length) {
		String validCharsUpper = "123456789";
		int maxIndex = validCharsUpper.length();
		String resultId = "";
		 Random rnd = new Random(System.currentTimeMillis()
				* (new Random().nextInt()));
		for (int i = 0; i < length; i++) {
			int rndPos = Math.abs(rnd.nextInt() % maxIndex);
			resultId += validCharsUpper.charAt(rndPos);
		}
		return resultId;
	}
	
	public boolean checkDateAfter(){
		Date currentDate = new Date();
		try {
			Date endDate = sdf.parse(props.getString("mow.date.end"));
			if(currentDate.compareTo(endDate) > 0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	public boolean checkDateBefor(){
		Date currentDate = new Date();
		try {
			Date endDate = sdf.parse(props.getString("mow.date.start"));
			if(currentDate.compareTo(endDate) < 0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	public boolean checkTime(){
		try {
			Date currentDate = new Date();
			Date startDate = sdf.parse(props.getString("mow.date.start"));
			Date endDate = sdf.parse(props.getString("mow.date.end"));
			
			if(currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) <= 0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public void saveDB(DeliverMessage dm, int status, String ma) {
		int result = 0; 
		result = ReportUtils.saveDB(dm.getId(), dm.getCommandCode(), dm.getMessage(), dm.getServiceId(), dm.getUserId(), ma, null,
				0, 0, 0, 0, 1);
		if (result == 1) {
			logger.info(">>>Message insert success!!!");
		} else {
			logger.info(">>>Message insert error!!!");
		}
	}
	
	
	public static void main(String[] args) {
		MowService asv = new MowService();
		DeliverMessage dm = new DeliverMessage();
		dm.setCommandCode("MOW");
		dm.setMessage("MOW");
		dm.setMobileOperator("VMS");
		dm.setServiceId("6689");
		dm.setUserId("84937145822");
		try {
			asv.getMessages(dm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
