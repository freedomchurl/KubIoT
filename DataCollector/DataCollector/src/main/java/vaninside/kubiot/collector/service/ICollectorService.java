package vaninside.kubiot.collector.service;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

public interface ICollectorService {
	boolean saveData(String deviceId, String dataType, ArrayList<Double> data, ArrayList<String> time, String regi, String protocol);
	boolean saveData(String deviceId, String dataType, MultipartFile data, String time, String regi, String protocol);
	boolean saveData(String deviceId, String dataType, String data, String time, String regi, String protocol);
	
	boolean saveDoubleData(String deviceId, ArrayList<Double> data, ArrayList<String> time);
	boolean saveImageData(String deviceId, String data, String type);
	boolean saveImageData(String deviceId, MultipartFile data);
	boolean register(String deviceId, String dataType, String protocol);
	
	void createFolder();
}
