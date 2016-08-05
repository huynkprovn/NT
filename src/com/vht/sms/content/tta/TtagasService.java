package com.vht.sms.content.tta;

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

public class TtagasService extends ContentAbstract {
    static Log logger = LogFactory.getLog(TtagasService.class);
    static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static TtagasProps props = TtagasProps.getInstance();

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
            if (tokens.length > 1) {
                response = callWs(dm);
                logger.info("response :   " + response);
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("message")) {
                    return MessageUtils.getTextMessages(jsonObject.getString("message"));
                } else {
                    return MessageUtils.getTextMessages(props.getString("message.timeout"));
                }
            } else {
                return MessageUtils.getTextMessages(props.getString("message.timeout"));
            }
        } catch (Exception e) {
            logger.error(">>>Message timeout!!! TTAGAS " + e.getMessage());
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
            requestList.add(new HttpHeader("User_ID", dm.getUserId(), false));
            requestList.add(new HttpHeader("Telco", dm.getMobileOperator(), false));
            requestList.add(new HttpHeader("Message", dm.getMessage(), false));
            requestList.add(new HttpHeader("Command_Code", dm.getCommandCode(), false));
            requestList.add(new HttpHeader("Service_ID", dm.getServiceId(), false));
            requestList.add(new HttpHeader("Request_ID", "0", false));

            List<String> responseList = HTTPGateway.HttpPost(urlAgr, requestList);

            if (responseList.size() > 0) {
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
        try {
            TtagasService ws = new TtagasService();
            DeliverMessage dm = new DeliverMessage();
            dm.setId(123456);
            dm.setCommandCode("TTA");
            dm.setMessage("TTA VHT");
            dm.setMobileOperator("VMS");
            dm.setServiceId("6089");
            dm.setUserId("84938882601");

            ws.getMessages(dm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
