package vaninside.kubiot.controller.controller;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vaninside.kubiot.controller.service.ControlService;

@RestController
@SpringBootApplication
public class ControlController {

	@Autowired
	ControlService service;
	
	@RequestMapping(value="/control", method=RequestMethod.POST)
	public HashMap<String, Object> control(@RequestBody HashMap<String, Object> map) throws IOException {
		String deviceId = (String) map.get("deviceId");
		String protocol = (String) map.get("protocol");
		String request = (String) map.get("request");
		
		boolean result = service.Request(deviceId, protocol, request);
		
		// return json
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("status", result?1:0);
        
		return hashMap;
	}
	
	@RequestMapping(value="/group-control", method=RequestMethod.POST)
	public HashMap<String, Object> groupControl(@RequestBody HashMap<String, Object> map) throws IOException {
		String deviceId = (String) map.get("deviceId");
		String protocol = (String) map.get("protocol");
		String request = (String) map.get("request");
		String group = (String)map.get("group");
		
		boolean result = service.Request(deviceId, protocol, request);
		
		// return json
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("status", result?1:0);
        
		return hashMap;
	}
	
	// 수집 서비스에서 등록할 시에 요청.
	@RequestMapping(value="/connect", method=RequestMethod.POST)
	public HashMap<String, Object> register(@RequestBody HashMap<String, Object> map) {
		
		boolean result = true;
		
		// return json
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("status", result?1:0);
        
		return hashMap;
	}
	
}
