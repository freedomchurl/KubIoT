package vaninside.kubiot.controller.controller;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import vaninside.kubiot.controller.service.ControlService;

@RestController
@SpringBootApplication
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ControlController {

	@Autowired
	ControlService service;
	
	@RequestMapping(value="/control", method=RequestMethod.POST)
	public HttpEntity<HashMap> control(@RequestBody HashMap<String, Object> map) throws IOException {
		String deviceId = (String) map.get("deviceId");
		String protocol = (String) map.get("protocol");
		String request = (String) map.get("request");
		
		boolean result = service.Request(deviceId, protocol, request);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Headers", "Authorization");
		headers.add("Access-Control-Expose-Headers", "*");
		
		// return json
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("status", result?1:0);
        

		HttpEntity<HashMap> entity = new HttpEntity<>(hashMap,headers);
		
		return entity;
	}
	
	//HashMap<String, Object>
	@RequestMapping(value="/group-control", method=RequestMethod.POST)
	public HttpEntity<HashMap> groupControl(@RequestBody HashMap<String, Object> map) throws IOException {

		String request = (String) map.get("request");
		int groupId = (int)map.get("groupId");
		
		boolean result = service.GroupReq(groupId, request);
		
		// return json
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("status", result?1:0);
        
        HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Headers", "Authorization");
		headers.add("Access-Control-Expose-Headers", "*");
		

		HttpEntity<HashMap> entity = new HttpEntity<>(hashMap,headers);
		
		return entity;
        
		//return hashMap;
	}
	
	
	// 그룹 생성
	@RequestMapping(value="/create-group", method=RequestMethod.POST)
	public HashMap<String, Object> groupCreate(@RequestBody HashMap<String, Object> map) {
		int groupId = (int)map.get("groupId");
		
		boolean result = service.createGroup(groupId);
		
		// return json
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("status", result?1:0);
        
		return hashMap;
	}
	
	// 수집 서비스에서 등록할 시에 요청.
	@RequestMapping(value="/connect", method=RequestMethod.GET)
	public SseEmitter register(@RequestParam(value = "id")String id) {
		System.out.println(id);
		return service.sseConnect(id);
	}
	
}
