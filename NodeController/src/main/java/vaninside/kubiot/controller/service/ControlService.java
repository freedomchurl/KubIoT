package vaninside.kubiot.controller.service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import vaninside.kubiot.controller.dao.ControlDao;

@Service
public class ControlService implements IControlService{

	@Autowired
	ControlDao dao;
	
	public static String topic = "topic";
	private static final String MQTT_PUBLISHER_ID = "control-server";
    private static final String MQTT_SERVER_ADDRES= "tcp://101.101.219.90:1883";
	//private static final String MQTT_SERVER_ADDRES= "tcp://localhost:1883";
    
	private static IMqttClient instance;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ArrayList<String> deviceList = new ArrayList<String>();
	
    public ControlService() throws MqttException {
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
        }
	}
    
	@Override
	public boolean Request(String deviceId, String protocol, String request) {
		if(protocol.equals("HTTP")) {
			return HttpReq(deviceId, request);
		}else if(protocol.equals("MQTT")) {
			return MqttReq(deviceId, request);
		}
		return false;
	}

	@Override
	public boolean HttpReq(String deviceId, String request) {
		System.out.println("d "+deviceId);
		System.out.println("f "+deviceList.get(0));
		int index = 0;
		for(int i=0; i<deviceList.size(); i++) {
			if(deviceId.equals(deviceList.get(i))) {
				index = i;
				break;
			}
		}
		
		try {
			emitters.get(index).send(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		/*
		for(SseEmitter emitter : emitters ) { 
            try {
                emitter.send(request);
                return saveLogFile(deviceId, request);
            } catch (Exception e) {
            	e.printStackTrace();
            	return false;
            }
        }
        */
        
		
		return saveLogFile(deviceId, request);
	}

	@Override
	public boolean MqttReq(String deviceId, String request) {
		

        try {
        	String msg = new String("{\"req\":\""+request+"\"}");
            MqttMessage mqttMessage = new MqttMessage(msg.getBytes());
			instance.publish(topic+deviceId, mqttMessage);
            return saveLogFile(deviceId, request);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
			return false;
		} catch (MqttException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean GroupReq(int groupId, String request) {
		ArrayList<String> member= dao.getMember(groupId);
		// List get member
		
		for(int i=0; i<member.size(); i++) {
			System.out.println("a: "+member.get(i));
			String thisType = dao.getType(member.get(i));
			if(thisType.equals("HTTP")) {
				HttpReq(member.get(i), request);
			}else {
				MqttReq(member.get(i), request);
			}
		}
		return true;
	}

	@Override
	public boolean saveLogFile(String Id, String req) {
		return dao.insertLog(Id, req);	
	}

	@Override
	public SseEmitter sseConnect(String id) {
		System.out.println("device add");
        final SseEmitter emitter = new SseEmitter();
        ExecutorService service = Executors.newSingleThreadExecutor();
        this.emitters.add(emitter); 
        this.deviceList.add(id);
        System.out.println("device LIst " + deviceList.size());
        emitter.onCompletion( new Runnable() { 	 
            public void run() {
                emitters.remove(emitter); 
            }
        }); 
 
        emitter.onTimeout( new Runnable() {
            public void run() {
                emitters.remove(emitter); 
            }
        });
        
        try {
			emitter.send("Connection Success");
		} catch (IOException e) {
			e.printStackTrace();
		}
        return emitter;
	}



	public boolean createGroup(int groupId) {
		// TODO Auto-generated method stub
		String request = null;
		ArrayList<String> member= dao.getMember(groupId);
		for(int i=0; i<member.size(); i++) {
			String thisType = dao.getType(member.get(i));
			if(thisType.equals("HTTP")) {
				HttpReq(member.get(i), request);
			}else {
				MqttReq(member.get(i), request);
			}
		}
		return true;
	}

}
