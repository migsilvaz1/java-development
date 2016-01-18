package es.chg.portal.requests.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

public class DBConnectionFactory {

	public static void configure(String url, String user, String pass,
			String driverName) throws ClassNotFoundException, SQLException {
		Class.forName(driverName);
		ObjectPool connectionPool = new GenericObjectPool(null);
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				url, user, pass);
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
				connectionFactory, connectionPool, null, null, false, true);
		Class.forName("org.apache.commons.dbcp.PoolingDriver");
		PoolingDriver driver = (PoolingDriver) DriverManager
				.getDriver("jdbc:apache:commons:dbcp:");
		driver.registerPool("chg-requests", connectionPool);
	}

	public synchronized static Connection getDBConnection() throws SQLException {
		return DriverManager
				.getConnection("jdbc:apache:commons:dbcp:chg-requests");
	}

	public static void printDriverStats() throws SQLException {
		PoolingDriver driver = (PoolingDriver) DriverManager
				.getDriver("jdbc:apache:commons:dbcp:");
		ObjectPool connectionPool = driver.getConnectionPool("chg-requests");
		System.out.println("NumActive: " + connectionPool.getNumActive());
		System.out.println("NumIdle: " + connectionPool.getNumIdle());
	}

	public static void shutdownDriver() throws SQLException {
		PoolingDriver driver = (PoolingDriver) DriverManager
				.getDriver("jdbc:apache:commons:dbcp:");
		driver.closePool("chg-requests");
	}
	
}