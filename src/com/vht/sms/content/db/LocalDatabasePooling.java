package com.vht.sms.content.db;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleConnectionCacheManager;
import oracle.jdbc.pool.OracleDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


// TODO: Auto-generated Javadoc
/**
 * <p> Class: LocalDatabasePooling </p>
 * <p> Package: com.cth.smpp.brand.db </p> 
 * <p> Author: Nguyen Van Doi </p>
 * <p> Update by: Nguyen Van Doi </p>
 * <p> Version: $1.0 </p>
 * <p> Create date: Dec 28, 2008 15:10:44 PM </p>
 * <p> Update date: Dec 28, 2008 15:10:44 PM </p>
 **/

public class LocalDatabasePooling {
   
   /** The logger. */
   static Log logger = LogFactory.getLog(LocalDatabasePooling.class);
   
   /** The dp. */
   static ConfigurationService dp = ConfigurationService.getInstance();

   /** The instance. */
   static LocalDatabasePooling instance;
   
   /** The ods. */
   static OracleDataSource ods;
   
   /** The conn. */
   static Connection conn;

   /** The Constant USER. */
   static final String USER = "db1.database.user";
   
   /** The Constant PASSWORD. */
   static final String PASSWORD = "db1.database.password";
   
   /** The Constant HOST. */
   static final String HOST = "db1.database.host";
   
   /** The Constant PORT. */
   static final String PORT = "db1.database.port";
   
   /** The Constant NAME. */
   static final String NAME = "db1.database.name";
   
   /** The Constant DRIVER_TYPE. */
   static final String DRIVER_TYPE = "db1.database.drive-type";
   
   /** The Constant CACHE_SHEME. */
   static final String CACHE_SHEME = "db1.database.cache-sheme";
   
   /** The Constant MAX_CACHE_SIZE. */
   static final String MAX_CACHE_SIZE = "db1.database.max-cache-size";
   
   /** The Constant MIN_CACHE_SIZE. */
   static final String MIN_CACHE_SIZE = "db1.database.min-cache-size";
   
   /** The Constant CACHE_NAME. */
   static final String CACHE_NAME = "db1.database.cache-name";
   
   /**
    * DatabasePooling.
    *
    * @throws SQLException the sQL exception
    */
   private LocalDatabasePooling() throws SQLException {
      logger.info("Initializes the connection pooling");

      try {
         ods = new OracleDataSource();

         // set cache properties
         java.util.Properties props = new java.util.Properties();
         props.setProperty("MinLimit", dp.getString(MIN_CACHE_SIZE));
         props.setProperty("MaxLimit", dp.getString(MAX_CACHE_SIZE));
         props.setProperty("InitialLimit", "1"); 
         props.setProperty("ConnectionWaitTimeout", "5");
         props.setProperty("ValidateConnection", "true");

         ods.setServerName(dp.getString(HOST));
         ods.setDatabaseName(dp.getString(NAME));
         ods.setPortNumber(dp.getInt(PORT));
         ods.setDriverType(dp.getString(DRIVER_TYPE));
         ods.setUser(dp.getString(USER));
         ods.setPassword(dp.getString(PASSWORD));

         ods.setConnectionCachingEnabled(true); // be sure set to true
         ods.setConnectionCacheProperties(props);
         ods.setConnectionCacheName(CACHE_NAME); // this cache's name
      }
      catch (SQLException sqle) {
         throw sqle;
      }

   }


   /**
    * getInstance.
    *
    * @return DatabasePooling
    * @throws SQLException the sQL exception
    */
   public static LocalDatabasePooling getInstance() throws SQLException {
      if (instance == null) {
         instance = new LocalDatabasePooling();
      }
      return instance;
   }


   /**
    * getConnection.
    *
    * @return Connection
    * @throws SQLException the sQL exception
    */
   public static Connection getConnection() throws SQLException {
		Connection conn = ods.getConnection();
		// Turn off auto-commit for better performance
		// conn.setAutoCommit(false);
		if (conn == null) {
			throw new SQLException(
					"Maximum number of connection in pool exceeded");
		}
		return conn;
	}
   
   /**
    * List cache infos.
    *
    * @throws SQLException the sQL exception
    */
   public static void listCacheInfos() throws SQLException{
	      OracleConnectionCacheManager occm =
	          OracleConnectionCacheManager.getConnectionCacheManagerInstance();
	      System.out.println
	          (occm.getNumberOfAvailableConnections(CACHE_NAME)
	              + " connections are available in cache " + CACHE_NAME);
	      System.out.println
	          (occm.getNumberOfActiveConnections(CACHE_NAME)
	              + " connections are active");
	 
	    }


   /**
    * close.
    *
    * @throws SQLException the sQL exception
    */
   public void close() throws SQLException {
      ods.close();
   }
}
