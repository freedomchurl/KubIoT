package vaninside.kubiot.collector.service;

public interface ICollectorService {
	boolean saveDoubleData(String deviceId, String data, String group, String time);
	boolean saveImageData(String deviceId, String data, String group, String time);
	boolean checkRegister(String deviceId);
	boolean register(String deviceId);
	boolean saveData(String deviceId, String dataType, String data, String group, String time);
}
