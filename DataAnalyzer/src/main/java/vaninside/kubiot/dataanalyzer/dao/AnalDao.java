package vaninside.kubiot.dataanalyzer.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Repository
public class AnalDao {

	// MySQL Info
		private String driver = "com.mysql.cj.jdbc.Driver";
		//private String url = "jdbc:mysql://localhost:3306/kubiot?serverTimezone=UTC&characterEncoding=UTF-8";
		private String url = "jdbc:mysql://101.101.219.90:3306/kubiot?serverTimezone=UTC&characterEncoding=UTF-8";
		
		private String userid = "root";
		private String userpw = "dlcjf2779!";
		
		private Connection conn = null;
		private PreparedStatement pstmt = null;
		private ResultSet rs =null;
		
		@Autowired
		RedisTemplate<String, Object> redisTemplate;
		
		// Object Storage Info
		public static String bucketName = "openinfra";
		final AmazonS3 s3;
		final String endPoint = "https://kr.object.ncloudstorage.com";
		final String regionName = "kr-standard";
		final String accessKey = "ltDXrFCuHyha8pmwqTcX";
		final String secretKey = "XFDdqJEASit4wHVFjCEEX6Yi2oD17ekIzCec2z41";
		
		
		public AnalDao() {
			// S3 client
			s3 = AmazonS3ClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
			    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
			    .build();
			
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
		

		// 현재 float 인 디바이스 리스트
		public ArrayList<String> getDeviceList() {
			ArrayList<String> result = new ArrayList<String>();
			Statement stat;
			try {
				stat = conn.createStatement();
				String sql = String.format( "select deviceid from control_device;");
		        rs= stat.executeQuery(sql);
		        if(rs.next()) {
		        	System.out.printf("%s\n"
	                        ,rs.getString("deviceid"));
		        	result.add(rs.getString("deviceid"));
		        }    
		        stat.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
		// 디바이스 아이디로 min, max 불러오기
		public HashMap<String, Double> getMinMax(String deviceName) {
			
			HashMap<String, Double> hashMap = new HashMap<String, Double>();
	        //hashMap.put("status", result?1:0);
			
			Statement stat;
			try {
				stat = conn.createStatement();
				String sql = String.format( "select min, max from control_device where deviceid = '%s';", deviceName);
		        rs= stat.executeQuery(sql);
		        if(rs.next()) {
		        	System.out.printf("%s\n"
	                        ,rs.getDouble("min"));
		        	hashMap.put("min", rs.getDouble("min"));
		        	hashMap.put("max", rs.getDouble("max"));
		        	
		        }    
		        stat.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return hashMap;
		}
		// 디바이스 아이디로 redis 에서 현재 값 불러오기
		public double getCurrentValue(String deviceName) {
			List<Object> result;
			
			Long size = redisTemplate.opsForList().size(deviceName + ":input:data");
			
			result = redisTemplate.opsForList().range(deviceName + ":input:data", size-1, size); 
			return (double) result.get(0);
		}
		
		public int getID(String deviceName) {
			System.out.println("dao");
			int result=0;
			Statement stat;
			try {
				stat = conn.createStatement();
				String sql = String.format( "select id from device where name = '%s';", deviceName);
		        rs= stat.executeQuery(sql);
		        if(rs.next()) {
		        	System.out.printf("%s\n"
	                        ,rs.getInt("id"));
		        	result =  rs.getInt("id");
		        
		        }    
		        stat.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(result);
			return result;
		}
}

