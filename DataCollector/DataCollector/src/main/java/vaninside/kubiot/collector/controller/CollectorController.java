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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vaninside.kubiot.collector.controller.config.Mqtt;
import vaninside.kubiot.collector.model.MqttSubscribeModel;
import vaninside.kubiot.collector.service.CollectorService;

@RestController
@SpringBootApplication
@IntegrationComponentScan

public class CollectorController {

	public static String topic = "topic";
	
	public CollectorController() throws MqttException {
		// Subscribe to topic.
		Mqtt.getInstance().setCallback(new MqttCallback() {
			@Override
			public void connectionLost(Throwable cause) {}

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("okay");
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {}
		});
		Mqtt.getInstance().subscribe(topic);
	}
	
	@Autowired
	CollectorService service;
	
	@RequestMapping(value="/sendFData", method=RequestMethod.POST)
	public HashMap<String, Object> HTTP_ADataReceive(@RequestBody HashMap<String, Object> map) throws IOException {
		String deviceId = (String) map.get("deviceId");
		String dataType = (String) map.get("type");
		String data = (String) map.get("data");
		String group = (String) map.get("group");
		String time = (String) map.get("time");
		
		boolean result = service.saveData(deviceId, dataType, data, group, time); 
		
		// return json
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("status", result?1:0);
        
		return hashMap;
	}
	
	@RequestMapping(value="/sendBData", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public String HTTP_BDataReceive(
			@RequestParam("deviceId") String deviceId, @RequestParam("type") String type, @RequestParam("group") String group, @RequestParam("time") String time, 
			@RequestParam("data") List<MultipartFile> files) throws Exception {
		
		System.out.println(deviceId);
		for (MultipartFile file : files) {
			String originalfileName = file.getOriginalFilename();
			File dest = new File("C:/Image/" + originalfileName);
			file.transferTo(dest);
			// TODO
		}
		
		return deviceId;
	}
	
	public void MQTT_FDataReceive(String msg) throws ParseException {
		System.out.println("original : " + msg);
		
		
		JSONParser parser = new JSONParser();
		JSONObject obj = new JSONObject();
		obj = (JSONObject) parser.parse(msg);
		
		String deviceId = (String) obj.get("deviceId");
		String dataType = (String) obj.get("type");
		String data = (String) obj.get("data");
		String group = (String) obj.get("group");
		String time = (String) obj.get("time");
		
		// System.out.println(obj.get("id"));
		service.saveData(deviceId, dataType, data, group, time); 
	}
	
	public void MQTT_BDataReceive(String msg) throws ParseException, IOException {

		
		JSONParser parser = new JSONParser();

		System.out.println("here");
		JSONObject obj = new JSONObject();

		System.out.println("here");
		String[] str = msg.split("!@", 2);
		
		System.out.println(str.length);
		System.out.println(str[0]);
		System.out.println(str[1]);
		
		obj = (JSONObject) parser.parse(str[0]);
		
		System.out.println("here");
		String deviceId = (String) obj.get("deviceId");
		//String dataType = (String) obj.get("type");
		//String data = (String) obj.get("data");
		String data = str[1];
		//String group = (String) obj.get("group");
		//String time = (String) obj.get("time");
		//String filename = (String) obj.get("filename");
		
		System.out.println("here2");
		//System.out.println(filename);
		//System.out.println(data);
		
		
		 File lOutFile = new File("C:/Image/fin_image.png");

         FileOutputStream lFileOutputStream = new FileOutputStream(lOutFile);
         
         byte[] re = binaryStringToByteArray(data);
         lFileOutputStream.write(re);

         lFileOutputStream.close();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		Mqtt.getInstance().disconnect();
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
}

class MessageCallback implements MqttCallback {

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("inside");
		CollectorController c = new CollectorController();
		MqttSubscribeModel mqttSubscribeModel = new MqttSubscribeModel();
        //mqttSubscribeModel.setId(mqttMessage.getId());

        mqttSubscribeModel.setId(message.getId());
        mqttSubscribeModel.setMessage(new String(message.getPayload()));
        mqttSubscribeModel.setQos(message.getQos());   
        
        c.MQTT_FDataReceive(mqttSubscribeModel.getMessage());
	
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}
	
}

