package vaninside.kubiot.controller.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class ControlDao implements IControlDao{
	// MySQL Info
		private String driver = "com.mysql.cj.jdbc.Driver";
		private String url = "jdbc:mysql://101.101.219.90:3306/kubiot?serverTimezone=UTC&characterEncoding=UTF-8";
		
		private String userid = "root";
		private String userpw = "dlcjf2779!";
		
		private Connection conn = null;
		private PreparedStatement pstmt = null;
		private ResultSet rs =null;
		
		@Autowired
		private JdbcTemplate jdbcTemplate;
		
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
		String sql = "INSERT INTO control_log(deviceid, request) VALUES(?,?)";
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
	
	public ArrayList<String> getMember(int groupId) {
		ArrayList<String> result = new ArrayList<String>();
		Statement stat;
		try {
			stat = conn.createStatement();
			String sql = String.format("select device.name from groupregi inner join device "
					+ "where groupregi.deviceid=device.id and groupid = %d;", groupId);
	        rs= stat.executeQuery(sql);
	        while(rs.next()) {
	        	System.out.printf("%s\n"
                        ,rs.getString("name"));
	        	result.add(rs.getString("name"));
	        }    
	        stat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
	public String getType(String id) {
		// http 인가 mqtt 인가 알려주겟다
		String result = null;
		Statement stat;
		try {
			stat = conn.createStatement();
			String sql = String.format( "select protocol from device where name = \'%s\';", id);
	        rs= stat.executeQuery(sql);
	        System.out.println("here");
	        if(rs.next()) {
	        	System.out.printf("%s\n"
                        ,rs.getString("protocol"));
	        	result = rs.getString("protocol");
	        }    
	        stat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
