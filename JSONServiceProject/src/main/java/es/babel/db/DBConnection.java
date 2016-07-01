package es.babel.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBConnection {
	
	public static Connection createConnection() {
		Connection connection = null;
		try {
			ResourceBundle configDB = ResourceBundle.getBundle("db-connection");
			String url = configDB.getString("db.url");
			String driver = configDB.getString("db.driver");
			String user = configDB.getString("db.user");
			String pass = configDB.getString("db.password");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static void closeConnection(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
