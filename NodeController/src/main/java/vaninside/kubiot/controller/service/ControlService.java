package vaninside.kubiot.controller.service;

import java.io.IOException;
import java.time.LocalTime;
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
	
	public static String topic = "topic2";
	private static final String MQTT_PUBLISHER_ID = "control-server";
    private static final String MQTT_SERVER_ADDRES= "tcp://127.0.0.1:1883";
    private static IMqttClient instance;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	
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
		for(SseEmitter emitter : emitters ) { 
            try {
                emitter.send(request);
                return saveLogFile(deviceId, request);
            } catch (Exception e) {
            	e.printStackTrace();
            	return false;
            }
        }
		return false;
	}

	@Override
	public boolean MqttReq(String deviceId, String request) {
		
        MqttMessage mqttMessage = new MqttMessage(request.getBytes());
        try {
			instance.publish(topic, mqttMessage);
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
	public boolean GroupReq(String groupId, String protocol, String request) {
		
		return false;
	}

	@Override
	public boolean saveLogFile(String Id, String req) {
		return dao.insertLog(Id, req);	
	}

	@Override
	public SseEmitter sseConnect() {
        final SseEmitter emitter = new SseEmitter();
        ExecutorService service = Executors.newSingleThreadExecutor();
        this.emitters.add(emitter); 
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

}
