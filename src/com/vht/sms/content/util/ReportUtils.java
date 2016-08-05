package com.vht.sms.content.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vht.sms.content.db.LocalDatabasePooling;

// TODO: Auto-generated Javadoc
/**
 * The Class ReportUtils.
 * 
 * @author doi.nv
 */

public class ReportUtils {

	/** The log. */
	static Log log = LogFactory.getLog(ReportUtils.class);

	/**
	 * Save db.
	 * 
	 * @param messageId
	 *            the message id
	 * @param commandCode
	 *            the command code
	 * @param varchar_param1
	 *            the varchar_param1
	 * @param varchar_param2
	 *            the varchar_param2
	 * @param varchar_param3
	 *            the varchar_param3
	 * @param varchar_param4
	 *            the varchar_param4
	 * @param varchar_param5
	 *            the varchar_param5
	 * @param num_param1
	 *            the num_param1
	 * @param num_param2
	 *            the num_param2
	 * @param num_param3
	 *            the num_param3
	 * @param num_param4
	 *            the num_param4
	 * @param num_param5
	 *            the num_param5
	 * @return the int
	 */
	public static int saveDB(long messageId, String commandCode,
			String varchar_param1, String varchar_param2,
			String varchar_param3, String varchar_param4,
			String varchar_param5, int num_param1, int num_param2,
			int num_param3, int num_param4, int num_param5) {

		int result = 0;
		log.info("Start insert data..");
		CallableStatement callableStatement = null;
		Connection conn = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			callableStatement = conn
					.prepareCall("{CALL PKG_PROCESS_SERVER.PRO_INSERT_DATA_SERVICE_DETAIL(?,?,?,?,?,?,?,?,?,?,?,?)}");
			callableStatement.setLong(1, messageId);
			callableStatement.setString(2, commandCode);
			callableStatement.setString(3, varchar_param1);
			callableStatement.setString(4, varchar_param2);
			callableStatement.setString(5, varchar_param3);
			callableStatement.setString(6, varchar_param4);
			callableStatement.setString(7, varchar_param5);
			callableStatement.setInt(8, num_param1);
			callableStatement.setInt(9, num_param2);
			callableStatement.setInt(10, num_param3);
			callableStatement.setInt(11, num_param4);
			callableStatement.setInt(12, num_param5);

			result = callableStatement.executeUpdate();
		} catch (Exception e) {
			log.error("CALL PKG_PROCESS_SERVER.PRO_INSERT_DATA_SERVICE_DETAIL("
					+ messageId + "," + commandCode + "," + varchar_param1
					+ "," + varchar_param2 + "," + varchar_param3 + ","
					+ varchar_param4 + "," + varchar_param5 + "," + num_param1
					+ "," + num_param2 + "," + num_param3 + "," + num_param4
					+ "," + num_param5 + ")");
			log.error(e);
		} finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.debug(e.toString());
			}
		}

		return result;

	}

	public synchronized static Map<Long, String> getListPhone() {
		Map<Long, String> list = new HashMap<Long, String>();
		PreparedStatement callableStatement = null;
		Connection conn = null;
		PreparedStatement callableUpdate = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			callableStatement = conn
					.prepareStatement("select id,phone from spam_casino where sent!=1  and rownum<=5");
			callableUpdate = conn
					.prepareStatement("update spam_casino set sent=1 where id=?");
			ResultSet rs = callableStatement.executeQuery();
			while (rs.next()) {
				String phone = rs.getString("phone");
				long id = rs.getLong("id");
				list.put(id, phone);
				callableUpdate.setLong(1, id);
				callableUpdate.addBatch();
			}
			callableUpdate.executeBatch();
		} catch (Exception e) {
		} finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
				if (callableUpdate != null) {
					callableUpdate.close();
				}
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.debug(e.toString());
			}
		}
		return list;

	}

	public static void updateSpamCasinoPhone(long id, String message,
			String serviceNum, String commandCode, int status) {
		PreparedStatement callableStatement = null;
		Connection conn = null;
		try {
			conn = LocalDatabasePooling.getInstance().getConnection();
			callableStatement = conn
					.prepareStatement("update spam_casino set status=?,message=?,service_num=?,command_code=?,date_sent=? where id=? ");
			callableStatement.setInt(1, status);
			callableStatement.setString(2, message);
			callableStatement.setString(3, serviceNum);
			callableStatement.setString(4, commandCode);
			callableStatement.setTimestamp(5,
					new Timestamp(new Date().getTime()));
			callableStatement.setLong(6, id);
			callableStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.debug(e.toString());
			}
		}
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		// System.out.println(DateUtils.createTimestamp());
	}
}
