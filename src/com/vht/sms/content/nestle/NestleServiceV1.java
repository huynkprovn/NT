package com.vht.sms.content.nestle;


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

public class NestleServiceV1 extends ContentAbstract {
	static Log logger = LogFactory.getLog(NestleServiceV1.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static NestleProps props = NestleProps.getInstance();

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

			if (tokens.length<1) {
                return MessageUtils.getTextMessages(props.getString("message.timeout"));
            }

            if(tokens.length==3)
            {
                String question = tokens[1];
                String answer = tokens[2];

                boolean flagQuestion = checkQuestion(question);
                boolean flagAnswer = checkAnswer(answer);

                if (!flagQuestion && !flagAnswer) {
                    return MessageUtils.getTextMessages(props.getString("message.fail.final"));
                }
                else if (!flagQuestion) {
                    return MessageUtils.getTextMessages(props.getString("message.fail.question"));
                }
                else if (!flagAnswer) {
                    return MessageUtils.getTextMessages(props.getString("message.fail.answer"));
                }
                else {
                    String[] finalArray = props.getStringArray("nestle.final");
                    for (String element:finalArray) {
                        if ( element.split("::")[0].equals(question) && element.split("::")[1].equals( answer )) {
                            saveDB(dm, question, answer);
                            return MessageUtils.getTextMessages(props.getString("message.success"));
                        }
                    }
                    return MessageUtils.getTextMessages(props.getString("message.fail.answer"));
                }

            } else {
                return MessageUtils.getTextMessages(props.getString("message.fail"));
            }

			
		} catch (Exception e) {
			logger.error(">>>Message timeout!!! NESTLE " + e.getMessage());
			messageType = MyConst.CHARGE;
			contentType = MyConst.TEXT;
			retriesNum = MyConst.TIMEOUT; 
			return MessageUtils.getTextMessages(props.getString("message.timeout"));
		}
	}

    public boolean checkQuestion(String question)
    {
        try {
            boolean found = false;
            String[] questionArray = props.getStringArray("nestle.question");
            for (String element:questionArray) {
                if ( element.equals( question )) {
                    found = true;
                    break;
                }
            }
            return found;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkAnswer(String answer)
    {
        try {
            boolean found = false;
            String[] answerArray = props.getStringArray("nestle.answer");
            for (String element:answerArray) {
                if ( element.equals( answer )) {
                    found = true;
                    break;
                }
            }
            return found;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

	public void saveDB(DeliverMessage dm,String question,String answer) {
		int result = 0; 
		result = ReportUtils.saveDB(dm.getId(), dm.getCommandCode(), dm.getMessage(), dm.getServiceId(), dm.getUserId(), question, answer,
				0, 0, 0, 0, 1);
		if (result == 1) {
			logger.info(">>>Message insert success!!!");
		} else {
			logger.info(">>>Message insert error!!!");
		}
	}
	
	
	public static void main(String[] args) {

		try {
			DeliverMessage dm = new DeliverMessage();
			dm.setCommandCode("NESTLE");
			dm.setMessage("NESTLE 1 A");
			dm.setMobileOperator("VMS");
			dm.setServiceId("6089");
			dm.setUserId("84938882601");

			NestleServiceV1 service = new NestleServiceV1();
			service.getMessages(dm);

			System.out.print("Hello World");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
