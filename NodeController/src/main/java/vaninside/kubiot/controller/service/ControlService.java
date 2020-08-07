package vaninside.kubiot.controller.service;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.stereotype.Service;

@Service
public class ControlService implements IControlService{

	public static String topic = "topic2";
	private static final String MQTT_PUBLISHER_ID = "control-server";
    private static final String MQTT_SERVER_ADDRES= "tcp://127.0.0.1:1883";
    private static IMqttClient instance;
	
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean MqttReq(String deviceId, String request) {
		
        MqttMessage mqttMessage = new MqttMessage(request.getBytes());
        try {
			instance.publish(topic, mqttMessage);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
			return false;
		} catch (MqttException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean GroupReq(String deviceId, String protocol, String request, String group) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveLogFile(String log) {
		// TODO Auto-generated method stub
		return false;
	}

}
