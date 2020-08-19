package vaninside.kubiot.collector.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import vaninside.kubiot.collector.service.CollectorService;

@RestController
@SpringBootApplication
public class CollectorController {
	public static String topic = "topic";
	private static final String MQTT_PUBLISHER_ID = "collector-server";
	private static final String MQTT_SERVER_ADDRES= "tcp://101.101.219.90:1883";
	private static IMqttClient instance;
   
	@Autowired
	CollectorService service;
	
	public CollectorController() throws MqttException{
		init();
	}
	
	public void init() throws MqttException{
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
            //instance.publish(topic, new MqttMessage("hello".getBytes()));
            instance.setCallback(new MqttCallback() {
				@Override
				public void connectionLost(Throwable cause) {}

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					MQTT_DataReceive(message);
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {}
            }); 
        }
        // Subscribe to topic
        instance.subscribe(topic);
	}
			
	@RequestMapping(value="/sendFData", method=RequestMethod.POST)
	public HashMap<String, Object> HTTP_FDataReceive(@RequestBody HashMap<String, Object> map) throws IOException, ClassNotFoundException, SQLException {
		
		String deviceId = (String) map.get("deviceId");
		String dataType = (String) map.get("type");
		ArrayList<Double> data = (ArrayList<Double>) map.get("data");
		ArrayList<String> time = (ArrayList<String>) map.get("time");
		String regi = (String) map.get("regi");
		
		boolean result = service.saveData(deviceId, dataType, data, time, regi, "HTTP"); 
		
		// return json
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
        
        if(regi.equals("0")) {
        	hashMap.put("status", 2);
        	 hashMap.put("regi", "1");
        } else {hashMap.put("status", 0);
        	 hashMap.put("regi", "1");
        }
		return hashMap;
	}
	
	@RequestMapping(value="/sendBData", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public HashMap<String, Object> HTTP_BDataReceive(@RequestBody String msg) throws Exception {
		
		/*
		boolean result = false;
		for (MultipartFile file : files) {
			result = service.saveData(deviceId, type, file, time, regi, "HTTP"); 
		}
		*/
		boolean result = false;
		
		JSONParser parser = new JSONParser();
		JSONObject obj = new JSONObject();
		
		String[] str = msg.split("!@", 2);
		// str[0] : json info, str[1] : Image data 
		
		obj = (JSONObject) parser.parse(str[0]); 
		String deviceId = (String) obj.get("deviceId");
		String dataType = (String) obj.get("type");
		String regi = (String) obj.get("regi");
		System.out.println(str.length + "##");
		
			// image data
			String data = str[1];
			String time = (String) obj.get("time");
			result = service.saveData(deviceId, dataType, data, time, regi, "HTTP");
		
		
		// return json
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
        
        if(regi.equals("0")) {
        	hashMap.put("status", 2);
        	hashMap.put("regi", "1");
       } else {
    	   hashMap.put("status", 0);
       	 hashMap.put("regi", "1");
       }
		return hashMap;
	}
	
	public HashMap<String, Object> MQTT_DataReceive(MqttMessage msg) throws ParseException {
		System.out.println(msg.toString());
		
		JSONParser parser = new JSONParser();
		JSONObject obj = new JSONObject();
		
		String[] str = msg.toString().split("!@", 2);
		// str[0] : json info, str[1] : Image data 
		
		System.out.println(0);

		System.out.println(str[0]);
		obj = (JSONObject) parser.parse(str[0]); 
		String deviceId = (String) obj.get("deviceId");
		String dataType = (String) obj.get("type");
		String regi = (String) obj.get("regi");
		System.out.println(str.length + "##");
		
		boolean result;
		if(str.length == 1) {
			// float data
			ArrayList<Double> data = (ArrayList<Double>) obj.get("data");
			ArrayList<String> time =  (ArrayList<String>) obj.get("time");
			result = service.saveData(deviceId, dataType, data, time, regi, "MQTT");
		} 
		
		else { 
			System.out.println(1);
			// image data
			String data = str[1];
			String time = (String) obj.get("time");
			result = service.saveData(deviceId, dataType, data, time, regi, "MQTT");
		}
		
		// return json
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("status", result?1:0);
        //int status = result? 1:0;
        int status = 0;
        if(regi.equals("0")) {
       	 hashMap.put("regi", result?"1":"0");
       } else {
       	 hashMap.put("regi", "1");
       }
        String resultstr = "{\"status\":"+status+",\"regi\":\"1\"}";
        JSONObject resultObj = new JSONObject();
		resultObj = (JSONObject) parser.parse(resultstr);
		
		MqttMessage m = new MqttMessage(resultObj.toString().getBytes());
		
		try {
			instance.publish("topic"+deviceId, m);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		instance.disconnect();
	}
}

