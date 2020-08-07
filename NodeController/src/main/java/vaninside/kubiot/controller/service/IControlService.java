package vaninside.kubiot.controller.service;

public interface IControlService {
	public boolean Request(String deviceId, String protocol, String request);
	public boolean HttpReq(String deviceId, String request);
	public boolean MqttReq(String deviceId, String request);
	public boolean GroupReq(String deviceId, String protocol, String request, String group);
	public boolean saveLogFile(String log);
}
