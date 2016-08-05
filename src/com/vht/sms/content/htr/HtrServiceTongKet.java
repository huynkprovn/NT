package com.vht.sms.content.htr;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.telix.sms.jms.DeliverMessage;
import com.vht.sms.content.ContentAbstract;
import com.vht.sms.content.util.DateUtils;
import com.vht.sms.content.util.MessageUtils;
import com.vht.sms.content.util.MyConst;

public class HtrServiceTongKet extends ContentAbstract {
	static Log logger = LogFactory.getLog(HtrServiceTongKet.class);
	private static HtrProps props = HtrProps.getInstance();

	@Override
	protected Collection getMessages(DeliverMessage dm) throws Exception {
		messageType = MyConst.CHARGE;
		contentType = MyConst.TEXT;
		retriesNum = MyConst.SUCCESS;
		return MessageUtils.getTextMessages(props.getString("message.tk.lan12013"));		
	}
	
	
	public static void main(String[] args) {
		DeliverMessage dm = new DeliverMessage();
		dm.setId(new Date().getTime());
		dm.setUserId("84985855954");
		dm.setCommandCode("HTR");
		dm.setServiceId("8051");
		dm.setMessage("Htr D7RKMNA");
		dm.setMobileOperator("VMS");
		dm.setReceiveDate(DateUtils.createTimestamp());
		HtrServiceTongKet htr = new HtrServiceTongKet();
		try {
			htr.getMessages(dm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
