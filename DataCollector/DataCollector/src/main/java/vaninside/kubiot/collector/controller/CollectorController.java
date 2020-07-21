package vaninside.kubiot.collector.controller;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vaninside.kubiot.collector.service.CollectorService;

@RestController
public class CollectorController {
	
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
}


