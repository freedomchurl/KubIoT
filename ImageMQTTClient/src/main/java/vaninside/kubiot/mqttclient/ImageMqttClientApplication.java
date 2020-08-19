package vaninside.kubiot.mqttclient;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication
public class ImageMqttClientApplication {
	private static final String MQTT_PUBLISHER_ID = "client";
	private static final String MQTT_SERVER_ADDRES= "tcp://101.101.219.90:1883";
	private static IMqttClient instance;
	
	public static String ID = "MQTTI01"; // 디바이스 아이디
	public static String TYPE = "image/png"; // 디바이스 데이터 형식
	public static String TOPIC = "topic"; // 서버 토픽
	
	public static String regi = new String("0");   // 등록 상태
	public static ArrayList<String> groupList = new ArrayList<String>();
					
	public static void main(String[] args) throws MqttSecurityException, MqttException {
		// Mqtt Initialize
        if (instance == null) {
            instance = new MqttClient(MQTT_SERVER_ADDRES, MQTT_PUBLISHER_ID);                
        }

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);

        if (!instance.isConnected()) {
            instance.connect(options);
            System.out.println("Connected to MQTT Broker. I am "+ID);
            instance.setCallback(new MqttCallback() {
				@Override
				public void connectionLost(Throwable cause) {}

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					// 메시지를 받았을 때.
					  String msg = message.toString();
					  
					  JSONParser parser = new JSONParser();
					  JSONObject obj = new JSONObject();
					  obj = (JSONObject) parser.parse(msg);
					  
					  // 등록 정보 수정
					  if(obj.get("regi").equals("1")){
					    regi ="1";
					  }
					  // 그룹 등록
					  if((Integer) obj.get("status") == 1){
					    String groupName = (String) obj.get("name");
					    // 그룹 채널 가입
					    instance.subscribe(TOPIC+ID, 0);
					    // 그룹 추가
					    groupList.add((String)obj.get("name"));
					  }
					  // 제어 실행.
					  control(message);
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {}
            }); 
        }
        // Subscribe to topic
        instance.subscribe(TOPIC+ID);
        
		SpringApplication.run(ImageMqttClientApplication.class, args);
	}
	
	public static void control(MqttMessage message) {
		System.out.println("관리자 제어 명령 : "+message.toString());
		// 제어 내용
	}
		
	@Scheduled(cron = "*/4 * * * * *")
	public void send() throws IOException, MqttException {
		//File file = new File(".\\up_tri.png");
        byte[] imageInByte;
        
        BufferedImage originalImage = ImageIO.read(new File(".\\up_tri.png"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "png", baos);
        baos.flush();
         
        imageInByte = baos.toByteArray();
        //System.out.println(Arrays.toString(imageInByte));
         
        baos.close();
        System.out.println("이미지를 전송하였습니다.");
        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
		Date time = new Date();
		String time1 = format1.format(time);	
		
        String msg = "{\"deviceId\":\""+ ID+ "\", \"type\":\"" + TYPE +"\",\"regi\":\"" +regi+"\", \"time\":\"" + time1 + "\"}";
 		String final_msg = msg + "!@"+byteArrayToBinaryString(imageInByte);
 		
 		MqttMessage m = new MqttMessage(final_msg.getBytes());
 		instance.publish(TOPIC, m);
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
