package es.chg.portal.requests.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import es.chg.portal.requests.RequestsUtils;

public class RequestsDAO {

	public static String getUserIdByIdentifier(String identifier) throws SQLException {
		if (identifier == null || identifier.trim().isEmpty()) {
			return null;
		}
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("select id from users where identifier = ?");
			ps.setString(1, identifier.trim().toUpperCase());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			} else {
				return null;
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public static List<TemplateVO> getListTemplates() throws SQLException {
		List<TemplateVO> result = new ArrayList<TemplateVO>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("select id, name from templates where active = 1");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				TemplateVO template = new TemplateVO();
				template.setId(rs.getLong(1));
				template.setName(rs.getString(2));
				result.add(template);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return result;
	}
	
	public static List getRequestsPendingApprovalByUser(String userId) throws SQLException {
		List result = new ArrayList();
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("select r.id, r.pf_reference from requests r where r.users_fk = ? and r.status = 'PENDIENTE_APROBACION'");
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				RequestVO requestvo = new RequestVO();
				requestvo.setId(rs.getLong(1));
				requestvo.setPfReference(rs.getString(2));
				result.add(requestvo);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return result;
	}
	
	public static String getListRequestsByUserJSON(String userId) throws SQLException {
		JSONArray result = new JSONArray();
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("select r.id, r.register_date, r.status, t.name from requests r join templates t on r.templates_fk = t.id where r.users_fk = ? order by id desc");
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				JSONObject reg = new JSONObject();

				reg.put("id", rs.getLong(1));
				reg.put("register_date", RequestsUtils.getFormatDate(rs.getTimestamp(2)));
				reg.put("status", rs.getString(3));
				reg.put("name_template", rs.getString(4));
				
				result.add(reg);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return result.toString();
	}
	
	public static byte[] getContentTemplate(String idTemplate) throws SQLException {
		byte[] result = null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("select content from templates where id = ?");
			ps.setString(1, idTemplate);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result = rs.getBytes(1);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return result;
	}
	
	public static String insertRequest(String userId, String templateId, InputStream docContent) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("insert into requests (USERS_FK, TEMPLATES_FK, DOC_CONTENT) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, userId);
			ps.setString(2, templateId);
			ps.setBinaryStream(3, docContent);
			ps.executeUpdate();
			
			rs = ps.getGeneratedKeys();
		    if (rs.next()) {
		    	return rs.getString(1);		    	
		    } else {
		    	return null;
		    }

		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public static void updateRequest(String rowid, String userId, String templateId, InputStream docContent) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("update requests set USERS_FK = ?, TEMPLATES_FK = ?, DOC_CONTENT = ? where rowid = ?");
			ps.setString(1, userId);
			ps.setString(2, templateId);
			ps.setBinaryStream(3, docContent);
			ps.setString(4, rowid);
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public static byte[] getRequestPDF(String userId, String requestId) throws SQLException {
		byte[] result = null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("select r.DOC_CONTENT from requests r join users u on r.users_fk = u.id where r.id = ? and u.id = ?");
			ps.setString(1, requestId);
			ps.setString(2, userId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result = rs.getBytes(1);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return result;
	}
	
	public static byte[] getRequestSignedPDF(String userId, String requestId) throws SQLException {
		byte[] result = null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("select r.DOC_SIGNED_CONTENT from requests r join users u on r.users_fk = u.id where r.id = ? and u.id = ?");
			ps.setString(1, requestId);
			ps.setString(2, userId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result = rs.getBytes(1);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return result;
	}
	
	public static RequestVO getRequestDetail(String userId, String requestId) throws SQLException {
		RequestVO result = null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("select r.PF_DETAILED_STATUS, r.STATUS, r.PF_REFERENCE, r.REGISTER_DATE, t.ID, t.NAME, t.APPROVAL_CIRCUIT from requests r join users u on r.users_fk = u.id join templates t on r.templates_fk = t.id where r.id = ? and u.id = ?");
			ps.setString(1, requestId);
			ps.setString(2, userId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result = new RequestVO();
				result.setPfDetailedStatus(rs.getString(1));
				result.setStatus(rs.getString(2));
				result.setPfReference(rs.getString(3));
				result.setRegisterDate(rs.getDate(4));
				
				TemplateVO templateVO = new TemplateVO();
				templateVO.setId(rs.getLong(5));
				templateVO.setName(rs.getString(6));
				templateVO.setApprovalCircuit(rs.getString(7));
				result.setTemplate(templateVO);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return result;
	}
	
	public static boolean isRequestOwner(String userId, String requestId) throws SQLException {
		boolean result = false;
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("select r.id from requests r join users u on r.users_fk = u.id where r.id = ? and u.id = ?");
			ps.setString(1, requestId);
			ps.setString(2, userId);
			ResultSet rs = ps.executeQuery();
			result = rs.next();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return result;
	}
	
	public static String getRequestStatus(String userId, String requestId) throws SQLException {
		String result = null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("select r.status from requests r join users u on r.users_fk = u.id where r.id = ? and u.id = ?");
			ps.setString(1, requestId);
			ps.setString(2, userId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result = rs.getString(1);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return result;
	}
	
	public static void updateRequestSignature(String requestId, InputStream signedContent) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("update requests set DOC_SIGNED_CONTENT = ?, STATUS = 'FIRMADO' where id = ?");
			ps.setBinaryStream(1, signedContent);
			ps.setString(2, requestId);
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public static UserVO getUserByUserId(String userId) throws SQLException {
		if (userId == null || userId.trim().isEmpty()) {
			return null;
		}
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("select id, identifier, name from users where id = ?");
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				UserVO uservo = new UserVO();
				uservo.setId(rs.getLong(1));
				uservo.setIdentifier(rs.getString(2));
				uservo.setName(rs.getString(3));
				return uservo;
			} else {
				return null;
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public static void updateRequestPfReference(String requestId, String pfReference) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("update requests set PF_REFERENCE = ? where id = ?");
			ps.setString(1, pfReference);
			ps.setString(2, requestId);
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public static void updateRequestPfStatus(String requestId, String pfDetailedStatus, String status) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getDBConnection();
			ps = conn.prepareStatement("update requests set PF_DETAILED_STATUS = ?, STATUS = ? where id = ?");
			ps.setString(1, pfDetailedStatus);
			ps.setString(2, status);
			ps.setString(3, requestId);
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
}