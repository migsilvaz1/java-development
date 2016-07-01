package es.babel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.babel.model.DataModel;

public class DataCRUD {
	public static void createData(DataModel data) {
		try {
			Connection c = DBConnection.createConnection();
			PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO datos(userId, groupId, privateLayout, parentLayoutId, name, title, description, keywords, robots, type, typeSettings, hidden, friendlyURL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			preparedStatement.setInt(1, data.getUserId());
			preparedStatement.setInt(2, data.getGroupId());
			if(data.getPrivateLayout()){
				preparedStatement.setInt(3, 1);
			}else{
				preparedStatement.setInt(3, 0);
			}
			preparedStatement.setInt(4, data.getParentLayoutId());
			preparedStatement.setString(5, data.getName());
			preparedStatement.setString(6, data.getTitle());
			preparedStatement.setString(7, data.getDescription());
			preparedStatement.setString(8, data.getKeywords());
			preparedStatement.setString(9, data.getRobots());
			preparedStatement.setString(10, data.getType());
			preparedStatement.setString(11, data.getTypeSettings());
			if(data.getHidden()){
				preparedStatement.setInt(12, 1);
			}else{
				preparedStatement.setInt(12, 0);
			}
			preparedStatement.setString(13, data.getFriendlyURL());
			preparedStatement.executeUpdate();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static DataModel readData(Integer id) {
		DataModel data = null;
		try {
			Connection c = DBConnection.createConnection();
			PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM datos WHERE id = ?");
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			data = resultSetToDataModel(resultSet);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static List<DataModel> readAllData() {
		List<DataModel> dataList = null;
		try {
			Connection c = DBConnection.createConnection();
			PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM datos");
			ResultSet resultSet = preparedStatement.executeQuery();
			dataList = new ArrayList<DataModel>();
			while(resultSet.next()){
				dataList.add(resultSetToDataModel(resultSet));
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataList;
	}

	public static void updateData(DataModel data) {
		try {
			Connection c = DBConnection.createConnection();
			PreparedStatement preparedStatement = c.prepareStatement("UPDATE datos SET userId = ?, groupId = ?, privateLayout = ?, parentLayoutId = ?, name = ?, title = ?, description = ?, keywords = ?, robots = ?, type = ?, typeSettings = ?, hidden = ?, friendlyURL = ? WHERE id = ?");
			preparedStatement.setInt(1, data.getUserId());
			preparedStatement.setInt(2, data.getGroupId());
			if(data.getPrivateLayout()){
				preparedStatement.setInt(3, 1);
			}else{
				preparedStatement.setInt(3, 0);
			}
			preparedStatement.setInt(4, data.getParentLayoutId());
			preparedStatement.setString(5, data.getName());
			preparedStatement.setString(6, data.getTitle());
			preparedStatement.setString(7, data.getDescription());
			preparedStatement.setString(8, data.getKeywords());
			preparedStatement.setString(9, data.getRobots());
			preparedStatement.setString(10, data.getType());
			preparedStatement.setString(11, data.getTypeSettings());
			if(data.getHidden()){
				preparedStatement.setInt(12, 1);
			}else{
				preparedStatement.setInt(12, 0);
			}
			preparedStatement.setString(13, data.getFriendlyURL());
			preparedStatement.setInt(14, data.getId());
			preparedStatement.executeUpdate();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteData(DataModel data){
		try {
			Connection c = DBConnection.createConnection();
			PreparedStatement preparedStatement = c.prepareStatement("DELETE FROM datos WHERE id = ?");
			preparedStatement.setInt(1, data.getId());
			preparedStatement.executeUpdate();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static DataModel resultSetToDataModel(ResultSet resultSet) {
		DataModel data = null;
		try {
			Integer id = resultSet.getInt(1);
			Integer userId = resultSet.getInt(2);
			Integer groupId = resultSet.getInt(3);
			Boolean privateLayout = resultSet.getBoolean(4);
			Integer parentLayoutId = resultSet.getInt(5);
			String name = resultSet.getString(6);
			String title = resultSet.getString(7);
			String description = resultSet.getString(8);
			String keywords = resultSet.getString(9);
			String robots = resultSet.getString(10);
			String type = resultSet.getString(11);
			String typeSettings = resultSet.getString(12);
			Boolean hidden = resultSet.getBoolean(13);
			String friendlyURL = resultSet.getString(14);
			data = new DataModel(id, userId, groupId, privateLayout, parentLayoutId, name, title, description, keywords,
					robots, type, typeSettings, hidden, friendlyURL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
}
