package com.vht.sms.content.svt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.telix.sms.jms.DeliverMessage;
import com.vht.sms.content.ContentAbstract;
import com.vht.sms.content.svt.wsclient.MdsWebServicesServiceStub;
import com.vht.sms.content.util.MessageUtils;
import com.vht.sms.content.util.MyConst;

public class SvtService extends ContentAbstract {

    static Log logger = LogFactory.getLog(SvtService.class);
    private static SvtProps props = SvtProps.getInstance();

    @Override
    protected Collection getMessages(DeliverMessage dm) throws Exception {
        try {
            if (dm == null) {
                return new ArrayList<String>();
            } else {
                messageType = MyConst.CHARGE;
                contentType = MyConst.TEXT;
                retriesNum = MyConst.SUCCESS;
                StringTokenizer token = new StringTokenizer(dm.getMessage());
                if (token.countTokens() == 2) {
                    token.nextToken();
                    String tagVst = token.nextToken();
                    String rs = getMessageFromVst(dm.getUserId(), tagVst);
                    //System.out.println(rs);
                    return MessageUtils.getTextMessages(rs);
                } else {
                    return MessageUtils.getTextMessages(props.getString("message.error"));
                }
            }
        } catch (Exception e) {
            logger.error(">>>Message timeout!!! Lien he Ky ben Medason. | " + e.getMessage());
            messageType = MyConst.CHARGE;
            contentType = MyConst.TEXT;
            retriesNum = MyConst.TIMEOUT;
            return MessageUtils.getTextMessages(props.getString("message.timeout"));
        }
    }

    public String getMessageFromVst(String userId, String message)
            throws Exception {
        String rs = props.getString("message.timeout");

        MdsWebServicesServiceStub.CheckSVT request = new MdsWebServicesServiceStub.CheckSVT();
        request.setPhone_no(userId);
        request.setSvt(message);

        MdsWebServicesServiceStub stub = new MdsWebServicesServiceStub();
        MdsWebServicesServiceStub.CheckSVTResponse response = stub
                .checkSVT(request);
        rs = response.getResult();
        return rs;
    }

    public static void main(String[] args) {
        SvtService service = new SvtService();
        DeliverMessage dm = new DeliverMessage();
        dm.setCommandCode("SVT");
        dm.setMessage("svt 28HBJP1");
        dm.setUserId("84907915813");
        // dm.setServiceId("8051");
        // dm.setId(123461);
        // dm.setRequestId("0");
        // dm.setMobileOperator("VIETTEL");
        // dm.setReceiveDate(new Timestamp(new Date().getTime()));
        // System.out.println(props.getString("message.timeout"));

        try {
            // String rs = service.getMessageFromVst(dm.getUserId(),
            // dm.getMessage());
            // System.out.println(rs);
            // service.getMessages(dm);
            // System.out.println(date.compareTo(date2));
            service.getMessages(dm);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
