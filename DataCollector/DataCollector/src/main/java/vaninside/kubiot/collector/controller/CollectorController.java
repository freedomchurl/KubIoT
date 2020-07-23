package vaninside.kubiot.collector.controller;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.paho.client.mqttv3.MqttException;
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
import org.springframework.web.bind.annotation.RestController;

import vaninside.kubiot.collecotr.controller.config.Mqtt;
import vaninside.kubiot.collector.model.MqttSubscribeModel;
import vaninside.kubiot.collector.service.CollectorService;

@RestController
@SpringBootApplication
@IntegrationComponentScan

public class CollectorController {

	public static String topic = "topic";
	
	public CollectorController() throws MqttException {
		Mqtt.getInstance().subscribeWithResponse(topic, (s, mqttMessage) -> {

            MqttSubscribeModel mqttSubscribeModel = new MqttSubscribeModel();
            mqttSubscribeModel.setId(mqttMessage.getId());
            mqttSubscribeModel.setMessage(new String(mqttMessage.getPayload()));
            mqttSubscribeModel.setQos(mqttMessage.getQos());           
            MQTTDataReceive(mqttSubscribeModel.getMessage());

        });
	}
	
	@Autowired
	CollectorService service;
	
	@RequestMapping(value="/sendData", method=RequestMethod.POST)
	public HashMap<String, Object> HTTPDataReceive(@RequestBody HashMap<String, Object> map) {
		String deviceId = (String) map.get("deviceId");
		String dataType = (String) map.get("type");
		String data = (String) map.get("data");
		String group = (String) map.get("group");
		String time = (String) map.get("time");
		
		boolean result = service.saveData(deviceId, dataType, data, group, time); 
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
	    
        hashMap.put("status", result?1:0);
        
		return hashMap;
	}
	
	public void MQTTDataReceive(String msg) throws ParseException {
		//System.out.println("original : " + msg);
		
		
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
	
}


