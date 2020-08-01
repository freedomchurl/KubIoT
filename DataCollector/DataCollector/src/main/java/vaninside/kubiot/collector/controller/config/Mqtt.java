package vaninside.kubiot.collector.controller.config;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



public class Mqtt {
    private static final String MQTT_PUBLISHER_ID = "spring-server";
    private static final String MQTT_SERVER_ADDRES= "tcp://127.0.0.1:1883";
    private static IMqttClient instance;

    public static IMqttClient getInstance() {
        try {
            if (instance == null) {
                instance = new MqttClient(MQTT_SERVER_ADDRES, MQTT_PUBLISHER_ID);
                
            }



            MqttConnectOptions options = new MqttConnectOptions();

            options.setAutomaticReconnect(true);

            options.setCleanSession(true);

            options.setConnectionTimeout(10);



            if (!instance.isConnected()) {

                instance.connect(options);
                instance.setCallback(new MqttCallback() {

					@Override
					public void connectionLost(Throwable cause) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void messageArrived(String topic, MqttMessage message) throws Exception {
						// TODO Auto-generated method stub
					System.out.println("hi");	
					}

					@Override
					public void deliveryComplete(IMqttDeliveryToken token) {
						// TODO Auto-generated method stub
						
					}
                
                });
                
            }

        } catch (MqttException e) {

            e.printStackTrace();

        }



        return instance;

    }



    private Mqtt() {



    }

}