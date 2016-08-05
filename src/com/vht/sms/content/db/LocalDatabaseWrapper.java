package com.vht.sms.content.db;

import java.sql.*;
import org.apache.commons.logging.*;

// TODO: Auto-generated Javadoc
/**
 * <p> Class: LocalDatabasePooling </p>
 * <p> Package: com.cth.smpp.brand.db </p> 
 * <p> Author: Nguyen Van Doi </p>
 * <p> Update by: Nguyen Van Doi </p>
 * <p> Version: $1.0 </p>
 * <p> Create date: Dec 28, 2008 15:20:44 PM </p>
 * <p> Update date: Dec 28, 2008 15:20:44 PM </p>
 **/
public class LocalDatabaseWrapper {
   
   /** The logger. */
   static Log logger = LogFactory.getLog(LocalDatabaseWrapper.class);

   /**
    * Get the submit sequence number.
    *
    * @return int
    */
   public static int getSequenceNum() {
      int sequenceNum = 0;

      Connection cn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
         LocalDatabasePooling pool = LocalDatabasePooling.getInstance();
         cn = pool.getConnection();

         pstmt = cn.prepareStatement(
            "SELECT MESSAGE_SEQ.NEXTVAL FROM DUAL");

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


   /**
    * The main method.
    *
    * @param args the arguments
    * @throws Exception the exception
    */
   public static void main(String[] args) throws Exception {
      System.out.println(getSequenceNum());
   }

}
