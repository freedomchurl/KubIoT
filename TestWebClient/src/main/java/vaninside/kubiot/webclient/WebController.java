package vaninside.kubiot.webclient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class WebController {

	//String collector_url = "http://101.101.219.90:8080/"; // 수집 서버
	String collector_url = "http://101.101.219.90:8081/sendFData"; // 수집 서버
	
	public static String ID = "HTTPF01"; // 디바이스 아이디
	public static String TYPE = "float"; // 디바이스 데이터 형식
	
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
		RestTemplate restTemplate = new RestTemplate();
		
		
		//Request : JSON : (string) deviceId, type, data double 배열, time String 배열, regi (String 0 or 1)
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
		Date time = new Date();
		String time1 = format1.format(time);	
		Double data = Math.random();
		String msg = "{\"deviceId\":\""+ ID+ "\", \"type\":\"" + TYPE +"\",\"regi\":\"" +regi+"\", \"time\":[\"" + time1 + "\"], \"data\":["+data+"]}";
 		System.out.println("전송 데이터 :" + data);
		HttpEntity<String> entity = new HttpEntity<>(msg,headers);
		String answer = restTemplate.postForObject(collector_url, entity, String.class);
		
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
		    //System.out.println("subscirbe topic"+groupName);
		   // instance.subscribe(TOPIC+ID, 0);
		    // 그룹 추가
		    groupList.add((String)obj.get("name"));
		  	}
		  // 제어 실행.
		  control(obj.get("req").toString());
	}
	
	public void control(String msg) {
		System.out.println("관리자 제어 명령:" + msg);
		// 제어 내용
	}
	
	public void registrGroup() {
		// 그룹 가입 시, 토픽 구독.
	}
}
