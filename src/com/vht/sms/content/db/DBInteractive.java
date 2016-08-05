package com.vht.sms.content.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telix.sms.jms.CP;
import com.telix.sms.jms.DeliverMessage;
import com.telix.sms.jms.Gateway;
import com.telix.sms.jms.SubmitMessage;
import com.vht.sms.content.util.DateUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class DBInteractive.
 */
public class DBInteractive {

	/** The logger. */
	static Log logger = LogFactory.getLog(DBInteractive.class);

	/** The instance. */
	public static DBInteractive instance;

	/**
	 * Gets the single instance of DBInteractive.
	 *
	 * @return single instance of DBInteractive
	 */
	public static DBInteractive getInstance() {
		if (instance == null) {
			instance = new DBInteractive();

		}
		return instance;
	}
	
	
	/**
	 * Insert sm.
	 *
	 * @param sm the sm
	 * @return the int
	 */
	public static int insertSM(SubmitMessage sm) {
		// TODO: save submit messages
		int result = 0;
		logger.info("insertSM :"+sm.getUserId()+"|"+sm.getServiceId()+"|"+sm.getMobileOperator()+"|"+
				sm.getCommandCode()+"|"+sm.getMessage()+"|"+DateUtils.createTimestamp()+"|"+sm.getRequestId()+"|"+sm.getCpId()+
				"|"+sm.getMessageType()+"|"+sm.getRetriesNum()+"|"+sm.getHasProcess()+"|"+sm.getContentType()+"|"+sm.getTotalMessage()+
				"|"+sm.getMessageIndex()+"|"+sm.getIsMore()+"|"+sm.getMessageId());
		CallableStatement callableStatement = null;
		Connection conn = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			callableStatement = conn
					.prepareCall("{CALL PKG_PROCESS_SERVER.PR_INSERT_SM(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			callableStatement.setString(1, sm.getUserId());
			callableStatement.setString(2, sm.getServiceId());
			callableStatement.setString(3, sm.getMobileOperator());
			callableStatement.setString(4, sm.getCommandCode());
			callableStatement.setString(5, new String(sm.getMessage()));
			callableStatement.setTimestamp(6, DateUtils.createTimestamp());
			callableStatement.setString(7, sm.getRequestId());
			callableStatement.setLong(8, sm.getCpId());
			callableStatement.setInt(9, sm.getMessageType());
			callableStatement.setInt(10, sm.getRetriesNum());
			callableStatement.setInt(11, sm.getHasProcess());
			callableStatement.setInt(12, sm.getContentType());
			callableStatement.setInt(13, sm.getTotalMessage());
			callableStatement.setInt(14, sm.getMessageIndex());
			callableStatement.setInt(15, sm.getIsMore());
			callableStatement.setString(16, sm.getMessageId());

			result =  callableStatement.executeUpdate();

		} catch (SQLException e) {
			logger.error(">>>> PKG_PROCESS_SERVER.PR_INSERT_SM("
					+ sm.getUserId() + ", " + sm.getServiceId() + ", "
					+ sm.getMobileOperator() + ", " + sm.getCommandCode()
					+ ", " + sm.getMessage() + ", "
					+ DateUtils.createTimestamp() + ", " + sm.getRequestId()
					+ ", " + sm.getCpId() + ", " + sm.getMessageType() + ", "
					+ sm.getRetriesNum() + ", " + sm.getHasProcess() + ", "
					+ sm.getContentType() + ", " + sm.getTotalMessage() + ", "
					+ sm.getMessageIndex() + ", " + sm.getIsMore() + ")", e);		
		}finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				logger.debug(e.toString());
			}
		}		
		return result;
	}

	/**
	 * Gets the class name.
	 *
	 * @param commandCode the command code
	 * @param serviceId the service id
	 * @return the class name
	 */
	public static String getClassName(String commandCode, String serviceId) {
		String className = null;

		// TODO: get class name from database
		CallableStatement callableStatement = null;
		Connection conn = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			callableStatement = conn
					.prepareCall("{CALL ? :=PKG_PROCESS_SERVER.FT_GET_CLASS_NAME(?,?)}");
			callableStatement.registerOutParameter(1,
					oracle.jdbc.OracleTypes.VARCHAR);
			callableStatement.setString(2, commandCode);
			callableStatement.setString(3, serviceId);

			callableStatement.execute();
			className = callableStatement.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(">>>> PKG_PROCESS_SERVER.FT_GET_CLASS_NAME("
					+ commandCode + " ," + serviceId + ")", e);
		}finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				logger.debug(e.toString());
			}
		}		
		return className;
	}

	/**
	 * Gets the cp id.
	 *
	 * @param commandCode the command code
	 * @return the cp id
	 */
	public static long getCpId(String commandCode) {
		long cpId = 0;
		// TODO get CP_ID from COMMAND_CODE
		CallableStatement callableStatement = null;
		Connection conn = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			callableStatement = conn
					.prepareCall("{CALL ?:= PKG_PROCESS_SERVER.FT_GET_CP_ID(?)}");
			callableStatement.registerOutParameter(1,
					oracle.jdbc.OracleTypes.VARCHAR);
			callableStatement.setString(2, commandCode);
			callableStatement.execute();
			cpId = callableStatement.getLong(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(">>>> PKG_PROCESS_SERVER.FT_GET_CP_ID(" + commandCode
					+ ")", e);
		}finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				logger.debug(e.toString());
			}
		}		

		return cpId;
	}

	/**
	 * Gets the operator.
	 *
	 * @param phoneNumber the phone number
	 * @return the operator
	 */
	public static String getOperator(String phoneNumber) {
		String cpId = "";
		// TODO get CP_ID from COMMAND_CODE
		CallableStatement callableStatement = null;
		Connection conn = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			callableStatement = conn
					.prepareCall("{CALL ?:= PKG_WS_VHTCMS.FT_GET_OPERATOR(?)}");
			callableStatement.registerOutParameter(1,
					oracle.jdbc.OracleTypes.VARCHAR);
			callableStatement.setString(2, phoneNumber);
			callableStatement.execute();
			cpId = callableStatement.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(">>>> PKG_WS_VHTCMS.FT_GET_OPERATOR(" + phoneNumber
					+ ")", e);
		}finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				logger.debug(e.toString());
			}
		}		

		return cpId;
	}

	/**
	 * Save alert.
	 *
	 * @param code the code
	 * @param domain the domain
	 * @param issue the issue
	 * @param alertMsg the alert msg
	 * @param alertStatus the alert status
	 */
	public static void saveAlert(String code, 
								 String domain, 
								 String issue,
								 String alertMsg, 
								 String alertStatus) {

		logger.error(">>>>ERROR : ALERT: " + code+", "+domain+", "+issue+", "+alertMsg+", "+DateUtils.createTimestamp()+", "+alertStatus);
		CallableStatement callableStatement = null;
		Connection conn = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			callableStatement = conn.prepareCall("{CALL PKG_PROCESS_SERVER.PR_SAVE_ALERT(?,?,?,?,?,?)}");
			callableStatement.setString(1, code);
			callableStatement.setString(2, domain);
			callableStatement.setString(3, issue);
			callableStatement.setString(4, alertMsg);
			callableStatement.setTimestamp(5, DateUtils.createTimestamp());
			callableStatement.setString(6, alertStatus);
			callableStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(">>>> PKG_WS_VHTCMS.PR_SAVE_ALERT(" + code+", "+domain+", "+issue+", "+alertMsg+", "+DateUtils.createTimestamp()+", "+alertStatus+ ")", e);
		}finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				logger.debug(e.toString());
			}
		}		
	}
	
	/**
	 * Gets the cP.
	 *
	 * @param cpId the cp id
	 * @return the cP
	 */
	public static CP getCP(long cpId){
		CP rs = new CP();
		ResultSet resultSet = null;
		CallableStatement callStatement = null;
		Connection conn = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			callStatement = conn.prepareCall("{CALL ?:= PKG_PROCESS_SERVER.FT_GET_CP(?)}");
			callStatement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
			callStatement.setLong(2, cpId);
			callStatement.execute();
			
			resultSet = (ResultSet) callStatement.getObject(1);
			while(resultSet.next()){
				long id = resultSet.getLong("ID");
				String name = resultSet.getString("NAME");
				String phone = resultSet.getString("PHONE");
				String address = resultSet.getString("ADDRESS");
				String email = resultSet.getString("EMAIL");
				String description = resultSet.getString("DESCRIPTION");
				String wsLink = resultSet.getString("WS_LINK");
				String wsTargetNamespace = resultSet.getString("WS_TARGET_NAMESPACE");
				String wsUsername = resultSet.getString("WS_USERNAME");
				String wsPassword = resultSet.getString("WS_PASSWORD");
				String function = resultSet.getString("FUNCTION");
				String wsParams = resultSet.getString("WS_PARAMS");
				String active = resultSet.getString("ACTIVE");
				
				rs.setId(id);
				rs.setName(name);
				rs.setPhone(phone);
				rs.setAddress(address);
				rs.setEmail(email);
				rs.setDescription(description);
				rs.setWsLink(wsLink);
				rs.setWsTargetNamespace(wsTargetNamespace);
				rs.setWsUsername(wsUsername);
				rs.setWsPassword(wsPassword);
				rs.setFunction(function);
				rs.setWsParams(wsParams);
				rs.setActive(active);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(">>>> PKG_PROCESS_SERVER.FT_GET_CP(" + cpId+ ")", e);
		}finally {
			try {
				if (callStatement != null)
					callStatement.close();
				if(resultSet != null)
					resultSet.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				logger.debug(e.toString());
			}
		}	
		
		return rs;
	}
	
	/**
	 * Gets the gateway.
	 *
	 * @param serviceId the service id
	 * @return the gateway
	 */
	public static Gateway getgateway(String serviceId){
		Gateway rs = new Gateway();
		ResultSet resultSet = null;
		CallableStatement callStatement = null;
		Connection conn = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			callStatement = conn.prepareCall("{CALL ?:= PKG_PROCESS_SERVER.FT_GET_GATEWAY(?)}");
			callStatement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
			callStatement.setString(2, serviceId);
			callStatement.execute();
			
			resultSet = (ResultSet) callStatement.getObject(1);
			while(resultSet.next()){
				long id = resultSet.getLong("ID");
				String code = resultSet.getString("CODE");
				String name = resultSet.getString("NAME");
				String description = resultSet.getString("DESCRIPTION");
				String wsLink = resultSet.getString("WS_LINK");
				String wsTargetNamespace = resultSet.getString("WS_TARGET_NAMESPACE");
				String wsUsername = resultSet.getString("WS_USERNAME");
				String wsPassword = resultSet.getString("WS_PASSWORD");
				String function = resultSet.getString("FUNCTION");
				String wsParams = resultSet.getString("WS_PARAMS");
				
				rs.setID(id);
				rs.setCode(code);
				rs.setName(name);
				rs.setDescription(description);
				rs.setWsLink(wsLink);
				rs.setWsTargetNamespace(wsTargetNamespace);
				rs.setWsUsername(wsUsername);
				rs.setWsPassword(wsPassword);
				rs.setFunction(function);
				rs.setWsParams(wsParams);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(">>>> PKG_PROCESS_SERVER.FT_GET_GATEWAY(" + serviceId+ ")", e);
		}finally {
			try {
				if (callStatement != null)
					callStatement.close();
				if(resultSet != null)
					resultSet.close(); 
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				logger.debug(e.toString());
			}
		}		
		
		return rs;
	}
	
	/**
	 * Gets the request id.
	 *
	 * @param messageId the message id
	 * @return the request id
	 */
	public String getRequestId(String messageId) {

		String requestId = "";
		// TODO get CP_ID from COMMAND_CODE
		CallableStatement callableStatement = null;
		Connection conn = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			callableStatement = conn
					.prepareCall("{CALL ?:= PKG_WS_VHTCMS.FT_GET_REQUEST_ID(?)}");
			callableStatement.registerOutParameter(1,
					oracle.jdbc.OracleTypes.VARCHAR);
			callableStatement.setString(2, messageId);
			callableStatement.execute();
			requestId = callableStatement.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("CALL PKG_WS_VHTCMS.FT_GET_REQUEST_ID(" + messageId
					+ ")", e);
		}finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				logger.debug(e.toString());
			}
		}	
		return requestId;
	}
	
	/**
	 * Gets the message ok.
	 *
	 * @param commandCode the command code
	 * @param serviceId the service id
	 * @return the message ok
	 */
	public static String getMessageOk(String commandCode, String serviceId) {
		String messageOk = null;

		// TODO: get class name from database
		CallableStatement callableStatement = null;
		Connection conn = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			callableStatement = conn
					.prepareCall("{CALL ? :=PKG_PROCESS_SERVER.FT_GET_MESSAGE_OK(?,?)}");
			callableStatement.registerOutParameter(1,
					oracle.jdbc.OracleTypes.VARCHAR);
			callableStatement.setString(2, commandCode);
			callableStatement.setString(3, serviceId);

			callableStatement.execute();
			messageOk = callableStatement.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(">>>> PKG_PROCESS_SERVER.FT_GET_MESSAGE_OK("
					+ commandCode + " ," + serviceId + ")", e);
		}finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				logger.debug(e.toString());
			}
		}		
		return messageOk;
	}
	
	public static List<DeliverMessage> getListDM(int numDM) {
		List<DeliverMessage> listMsg = new ArrayList<DeliverMessage>();
		CallableStatement callableStatement = null;
		Connection conn = null;
		ResultSet resultSet = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			callableStatement = conn
					.prepareCall("{CALL ? := PKG_PROCESS_SERVER.FT_GET_LIST_DM(?)}");
			callableStatement.registerOutParameter(1,
					oracle.jdbc.OracleTypes.CURSOR);
			callableStatement.setInt(2, numDM);
			callableStatement.execute();
			resultSet = (ResultSet) callableStatement.getObject(1);

			while (resultSet.next()) {
				// TODO: Get DM
				long id = resultSet.getLong("ID");
				String userId = resultSet.getString("USER_ID");
				String serviceId = resultSet.getString("SERVICE_ID");
				String mobileOperator = resultSet.getString("MOBILE_OPERATOR");
				
				String commandCode = resultSet.getString("COMMAND_CODE");
				String message = resultSet.getString("MESSAGE");
				String requestId = resultSet.getString("REQUEST_ID");
				Timestamp receiveDate = resultSet.getTimestamp("RECEIVE_DATE");
				int retriesNum = resultSet.getInt("RETRIES_NUM");
				
				int hasProcess = resultSet.getInt("HAS_PROCESS");
				
				String commandPrefix = resultSet.getString("COMMAND_PREFIX");//COMMAND_PREFIX
				DeliverMessage dm = new DeliverMessage();
				dm.setId(id);
				dm.setUserId(userId);
				dm.setServiceId(serviceId);
				dm.setMobileOperator(mobileOperator);
				dm.setCommandCode(commandCode);
				dm.setMessage(message);
				dm.setRequestId(requestId);
				dm.setReceiveDate(receiveDate);
				dm.setRetriesNum(retriesNum);
				dm.setHasProcess(hasProcess);
				dm.setCommandPrefix(commandPrefix);

				listMsg.add(dm);
			}
		} catch (SQLException e) {
			logger.error(">>>> PKG_DM.FT_GET_LIST_DM(" + numDM + ")", e);
			// Send mail Alert
			// TODO

			try {
				throw new Exception(e);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				logger.error(">>>> PKG_DM.FT_GET_LIST_DM(" + numDM + ")", e);

			}
		} finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
				if (resultSet != null) {
					resultSet.close();
				}
				if(conn != null)
					conn.close();
			} catch (Exception e) {
				logger.error(">>>> callableStatement " + e.getMessage(), e);
				// Send mail Alert
				// TODO

			}
		}

		return listMsg;
	}
	
	 public static int getSequenceNumHTV() {
	      int sequenceNum = 0;

	      Connection cn = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      try {
	         LocalDatabasePooling pool = LocalDatabasePooling.getInstance();
	         cn = pool.getConnection();

	         pstmt = cn.prepareStatement(
	            "SELECT HTV_SEQ.NEXTVAL FROM DUAL");

	         rs = pstmt.executeQuery();
	         if (rs.next()) {
	            sequenceNum = rs.getInt(1);
	         }
	      }
	      catch (SQLException sqle) {
	         logger.error("Failed to get submit sequence number.", sqle);
	      }
	      finally {
	         if (rs != null) {
	            try {
	               rs.close();
	            }
	            catch (SQLException sqle) {
	            }
	         }
	         if (pstmt != null) {
	            try {
	               pstmt.close();
	            }
	            catch (SQLException sqle) {
	            }
	         }
	         if (cn != null) {
	            try {
	               cn.close();
	            }
	            catch (SQLException sqle) {
	            }
	         }
	      }

	      return sequenceNum;
	   }
	 
	public static int insertHtvTblData(String phoneNumber, String personelID,
			String info, String programID,Timestamp votingTime ,String note){
		Connection conn = null;
		PreparedStatement  preparedStatement = null;
		int DONE = 1;
		int FAILD = 0;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			int id = getSequenceNumHTV();
			logger.info("insert into htv_tbl_Data values("
					+ id
					+ "|"
					+ phoneNumber
					+ "|"
					+ personelID
					+ "|"
					+ info
					+ "|"
					+ programID + "|" + votingTime + "|"+ note + ")");
			
			String sql = "Insert into htv_tbl_Data(id,PhoneNumber,PersonelID,Info,ProgramID,VotingTime,Note) values(?,?,?,?,?,?,?)";
			preparedStatement = conn.prepareStatement(sql);
			
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, phoneNumber);
			preparedStatement.setString(3, personelID);
			preparedStatement.setString(4, info);
			preparedStatement.setString(5, programID);
			preparedStatement.setTimestamp(6, votingTime);
			preparedStatement.setString(7, note);
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.error(new Date()+" >>> Xay ra loi khi insert HTV_TBL_DATA :"+e.getMessage());
			return FAILD;
		}
		 finally {
				try {				
					if (preparedStatement != null)
						preparedStatement.close();
					if(conn != null){
						conn.close();
					}

				} catch (Exception e) {
					logger.error(">>>> prepareStatement " + e.getMessage(), e);
					return FAILD;
				}
			}
		return DONE;
	}

}
