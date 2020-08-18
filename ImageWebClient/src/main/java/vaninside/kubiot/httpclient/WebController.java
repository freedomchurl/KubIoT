package vaninside.kubiot.httpclient;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Controller
public class WebController {

	//String collector_url = "http://101.101.219.90:8080/"; // 수집 서버
	String collector_url = "http://localhost:8080/sendBData"; // 수집 서버
	
	public static String ID = "HTTPI01"; // 디바이스 아이디
	public static String TYPE = "image/png"; // 디바이스 데이터 형식
	
	public static String regi = new String("0");   // 등록 상태
	public static ArrayList<String> groupList = new ArrayList<String>();
	
	public WebController() {
		// 실행 시에 초기 설정.
		init();
	}
	
	public void init() {
		// 초기 설정
		// 제어와 이벤트 스트림 연결하기.
	}
	
	@Scheduled(cron = "*/4 * * * * *")
	public void send() {
		// http send
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
//		headers.add("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary8UhbmC4vAvBxT6z3"); //multipart/form-data 있는 경우 사용.

		RestTemplate restTemplate = new RestTemplate();
		
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
		Date time = new Date();
		String time1 = format1.format(time);	

		byte[] imageInByte = null;
        
       
        try {
        	 BufferedImage originalImage = ImageIO.read(new File(".\\down_tri.png"));
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "png", baos);
			 baos.flush();
	         
		        imageInByte = baos.toByteArray();
		        baos.close();
		        
		        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		
        String msg = "{\"deviceId\":\""+ ID+ "\", \"type\":\"" + TYPE +"\",\"regi\":\"" +regi+"\", \"time\":\"" + time1 + "\"}";
 		String final_msg = msg + "!@"+byteArrayToBinaryString(imageInByte);


		//Request : JSON : (string) deviceId, type, data double 배열, time String 배열, regi (String 0 or 1)
		
		Double data = Math.random();
		//String msg = "{\"deviceId\":\""+ ID+ "\", \"type\":\"" + TYPE +"\",\"regi\":\"" +regi+"\", \"time\":[\"" + time1 + "\"], \"data\":["+data+"]}";
 		System.out.println(msg);
		HttpEntity<String> entity = new HttpEntity<>(final_msg,headers);
		String answer = restTemplate.postForObject(collector_url, entity, String.class);
		System.out.println(answer);
		receive(answer);
	}
	
	public void receive(String msg) {
		// http receive 받았습니다...

		msg = msg.replaceAll("'", "\"");
		JSONParser parser = new JSONParser();
		  JSONObject obj = new JSONObject();
		  try {
			obj = (JSONObject) parser.parse(msg);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  // 등록 정보 수정
		  if(obj.get("regi").equals("1")){
		    regi ="1";
		  }
		  // 그룹 등록
		  if(obj.get("status").toString().equals("1")){
		    String groupName = (String) obj.get("name");
		    // 그룹 채널 가입
		    System.out.println("subscirbe topic"+groupName);
		   // instance.subscribe(TOPIC+ID, 0);
		    // 그룹 추가
		    groupList.add((String)obj.get("name"));
		  	}
		  // 제어 실행.
		  // control();
	}
	
	public void control(String msg) {
		System.out.println(msg);
		// 제어 내용
	}
	
	public void registrGroup() {
		// 그룹 가입 시, 토픽 구독.
	}
	
	public static byte[] binaryStringToByteArray(String s) {
        int count = s.length() / 8;
        byte[] b = new byte[count];
        for (int i = 1; i < count; ++i) {
            String t = s.substring((i - 1) * 8, i * 8);
            b[i - 1] = binaryStringToByte(t);
        }
        return b;
    }

    public static byte binaryStringToByte(String s) {
        byte ret = 0, total = 0;
        for (int i = 0; i < 8; ++i) {
            ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0;
            total = (byte) (ret | total);
        }
        return total;
    }	
    
    public String byteArrayToBinaryString(byte[] b){
        StringBuilder sb=new StringBuilder();
        for(int i=0; i<b.length; ++i){
            sb.append(byteToBinaryString(b[i]));
        }
        return sb.toString();
    }

    public String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for (int bit = 0; bit < 8; bit++) {
            if (((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }
}
