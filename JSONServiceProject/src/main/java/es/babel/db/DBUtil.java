package es.babel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBUtil {
	public static boolean checkDataTable(){
		Boolean res = true;
		try {
			Connection connection = DBConnection.createConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("select * from datos");
			preparedStatement.executeQuery();
			connection.close();
		} catch (SQLException e) {
			res = false;
		}
		return res;
	}
	public static void createDataTable(){
		try {
			Connection connection = DBConnection.createConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `datos` (`id` int(11) NOT NULL AUTO_INCREMENT, `userId` bigint(20) DEFAULT NULL, `groupId` bigint(20) DEFAULT NULL, `privateLayout` tinyint(1) DEFAULT NULL, `parentLayoutId` bigint(20) DEFAULT NULL, `name` varchar(255) COLLATE utf8_bin DEFAULT NULL, `title` varchar(255) COLLATE utf8_bin DEFAULT NULL, `description` varchar(255) COLLATE utf8_bin DEFAULT NULL, `keywords` varchar(255) COLLATE utf8_bin DEFAULT NULL, `robots` varchar(255) COLLATE utf8_bin DEFAULT NULL, `type` varchar(255) COLLATE utf8_bin DEFAULT NULL, `typeSettings` varchar(255) COLLATE utf8_bin DEFAULT NULL, `hidden` tinyint(1) DEFAULT NULL, `friendlyURL` varchar(255) COLLATE utf8_bin DEFAULT NULL, PRIMARY KEY (`id`) ) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin");
			preparedStatement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void dropDataTable(){
		try {
			Connection connection = DBConnection.createConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS `datos`");
			preparedStatement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
