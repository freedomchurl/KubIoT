package vaninside.kubiot.controller.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

@Repository
public class ControlDao implements IControlDao{
	// MySQL Info
		private String driver = "com.mysql.cj.jdbc.Driver";
		private String url = "jdbc:mysql://localhost:3306/kubiot?serverTimezone=UTC&characterEncoding=UTF-8";
		
		private String userid = "root";
		private String userpw = "1234";
		
		private Connection conn = null;
		private PreparedStatement pstmt = null;
		private ResultSet rs =null;
		
		public ControlDao() {
			try {
				// JDBC
				Class.forName(driver);
				conn = DriverManager.getConnection(url, userid, userpw);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	@Override
	public boolean insertLog(String Id, String req) {
		String sql = "INSERT INTO control_log(Id, req) VALUES(?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, Id);
			pstmt.setString(2, req);
			pstmt.executeUpdate();
			
			pstmt.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
