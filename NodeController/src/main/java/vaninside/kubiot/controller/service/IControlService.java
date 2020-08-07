package vaninside.kubiot.controller.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface IControlService {
	public boolean Request(String deviceId, String protocol, String request);
	public boolean HttpReq(String deviceId, String request);
	public boolean MqttReq(String deviceId, String request);
	public boolean GroupReq(String groupId, String protocol, String request);
	public boolean saveLogFile(String Id, String req);
	public SseEmitter sseConnect();
}
