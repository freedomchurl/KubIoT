package vaninside.kubiot.collector.dao;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;


@Repository
public class CollectorDao implements ICollectorDao{

	
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/kubiot?serverTimezone=UTC&characterEncoding=UTF-8";
	private String userid = "root";
	private String userpw = "jimin5238";
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs =null;
	public static String bucketName = "openinfra";
	final AmazonS3 s3;
	
	public CollectorDao() {
		final String endPoint = "https://kr.object.ncloudstorage.com";
		final String regionName = "kr-standard";
		final String accessKey = "ltDXrFCuHyha8pmwqTcX";
		final String secretKey = "XFDdqJEASit4wHVFjCEEX6Yi2oD17ekIzCec2z41";
		// S3 client
		s3 = AmazonS3ClientBuilder.standard()
			.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
		    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
		    .build();
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userid, userpw);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean createDir(String folderName) {
		//String folderName = "sample-folder/";

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(0L);
		objectMetadata.setContentType("application/x-directory");
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, new ByteArrayInputStream(new byte[0]), objectMetadata);
			
		try {
		    s3.putObject(putObjectRequest);
		    System.out.format("Folder %s has been created.\n", folderName);
		} catch (AmazonS3Exception e) {
		    e.printStackTrace();
		    return false;
		} catch(SdkClientException e) {
		    e.printStackTrace();
		    return false;
		}
		return true;
	}

	@Override
	public boolean register(String deviceId, String dataType) {
		String sql = "INSERT INTO device (name, type) VALUES(?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, deviceId);
			pstmt.setString(2, dataType);
			pstmt.executeUpdate();
			
			pstmt.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		//
		//conn.close();
	}

	@Override
	public boolean insertData(String folderName, File file) {
		String objectName = file.getName();
		//String objectName = "sample-object";
		//String filePath = "C:\\Users\\JIMIN\\Desktop\\sample.txt";

		try {
		    //s3.putObject(bucketName+"/sample-folder", objectName, file);
			s3.putObject(bucketName+folderName, objectName, file);
			System.out.format("Object %s has been created.\n", objectName);
		} catch (AmazonS3Exception e) {
		    e.printStackTrace();
		    return false;
		} catch(SdkClientException e) {
		    e.printStackTrace();
		    return false;
		}
		
		return true;
	}

}
