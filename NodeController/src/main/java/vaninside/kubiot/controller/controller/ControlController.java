package vaninside.kubiot.controller.controller;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


public class ControlController {

	public static String topic = "topic";
	
	//@Autowired
	//CollectorService service;
	
	@RequestMapping(value="/control", method=RequestMethod.POST)
	public HashMap<String, Object> control(@RequestBody HashMap<String, Object> map) throws IOException {
		String deviceId = (String) map.get("deviceId");
		String protocol = (String) map.get("protocol");
		String request = (String) map.get("request");
		
		// return json
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
        //hashMap.put("status", result?1:0);
        
		return hashMap;
	}
}
