package vaninside.kubiot.collector.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vaninside.kubiot.collector.dao.CollectorDao;

@Service
public class CollectorService implements ICollectorService {

	@Autowired
	CollectorDao dao;

	@Override
	public boolean saveDoubleData(String deviceId, String data, String group, String time) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveImageData(String deviceId, String data, String group, String time) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkRegister(String deviceId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean register(String deviceId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveData(String deviceId, String dataType, String data, String group, String time) {
		// TODO Auto-generated method stub
		if(!checkRegister(deviceId)) {
			// 등록 안된 기기일 경우
			// 등록하세요.
			register(deviceId);
		}
		
		if(dataType.equals("0")) {
			//실수형.
			saveDoubleData(deviceId, data, group, time);
		} else {
			//이미지.
			saveImageData(deviceId, data, group, time);
		}
		
		return false;
	}
	
	
}
