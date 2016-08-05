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
import java.util.Date;

public class NestleService extends ContentAbstract {
	static Log logger = LogFactory.getLog(NestleService.class);
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

            if(tokens.length==2)
            {
                String answer = tokens[1];
                if (checkQuestionAnswer(answer)) {
                    saveDB(dm, answer);
                    return MessageUtils.getTextMessages(props.getString("message.success"));
                } else {
                    return MessageUtils.getTextMessages(props.getString("message.fail"));
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

    public boolean checkQuestionAnswer(String message)
    {
        try {

            String[] splitArray = message.split(",");
            if (splitArray.length == 5 || splitArray.length == 7) {
                for(int i=0; i < splitArray.length; i++) {

                    if (splitArray[i].length() == 2) {

                        String question = splitArray[i].substring(0, 1);
                        String answer = splitArray[i].substring(1, 2);

                        String index = question.valueOf(i+1);
                        boolean flagAnswer = checkAnswer(answer);

                        if ( !question.equals(index) || !flagAnswer) {
                            //System.out.println(flagAnswer);
                            return false;
                        }

                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

	public void saveDB(DeliverMessage dm,String answer) {
		int result = 0; 
		result = ReportUtils.saveDB(dm.getId(), dm.getCommandCode(), dm.getMessage(), dm.getServiceId(), dm.getUserId(), answer, null,
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
			dm.setMessage("NESTLE 1A,2B,3E,4D,5E");
			dm.setMobileOperator("VMS");
			dm.setServiceId("6089");
			dm.setUserId("84938882601");

			NestleService service = new NestleService();
			//service.getMessages(dm);

			System.out.print("Hello World");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
