package vaninside.kubiot.collector.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vaninside.kubiot.collector.dao.CollectorDao;

@Service
public class CollectorService implements ICollectorService {
	@Autowired
	CollectorDao dao;

	@Override
	public boolean saveData(String deviceId, String dataType, ArrayList<Double> data, ArrayList<String> time, String regi, String protocol) {
		// save float data
		if(regi.equals("0")) { // 등록안된 기기
			if(!register(deviceId, dataType, protocol)) return false;
		} 
		return saveDoubleData(deviceId, data, time);
	}

	@Override
	public boolean saveData(String deviceId, String dataType, MultipartFile data, String time, String regi, String protocol) {
		if(regi.equals("0")) {// 등록안된 기기
			if(!register(deviceId, dataType, protocol)) return false;
		} 
		return saveImageData(deviceId, data);
	}

	@Override
	public boolean saveData(String deviceId, String dataType, String data, String time, String regi, String protocol) {
		System.out.println(99);
		if(regi.equals("0")) {// 등록안된 기기
			System.out.println("888");
			if(!register(deviceId, dataType, protocol)) return false;
		} 
		
		if(dataType.contains("image")) {
			System.out.println(2);
			return saveImageData(deviceId, data, dataType);
		} 
		
		return true;
	}

	@Override
	public boolean saveDoubleData(String deviceId, ArrayList<Double> data, ArrayList<String> time) {
		System.out.println("Save double data");
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddmmss");
		Date curtime = new Date();
		String time1 = format1.format(curtime);	
		String fileName = deviceId +"_" + time1;
	/*
		File file = new File("./file/"+fileName+".csv");
		
		//csv 파일 생성
		try { 
			FileOutputStream fOut = new FileOutputStream( file ); 
			BufferedOutputStream bos = new BufferedOutputStream(fOut);
			
			for(int i=0; i<data.size(); i++) {
				bos.write(data.get(i).toString().getBytes());
				bos.write(",".getBytes());
				bos.write(time.get(i).getBytes());
				bos.write('\n');
			}
			
			bos.close();
			fOut.close();
			
			SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy-MM-dd");
			Date curtime2 = new Date();
			String time2 = format2.format(curtime2);
			*/
			//dao.insertData("/"+time2, file);
			dao.insertRedis(deviceId, data.get(0), time.get(0));
		/*} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}*/
		return true;
	}

	@Override
	public boolean saveImageData(String deviceId, String data, String type) {
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddmmss");
		Date curtime = new Date();
		String time1 = format1.format(curtime);
		
		String fileName = deviceId +"_" + time1;
		String[] str = type.split("/");
		File file;

		System.out.println(3);
		if(str.length >= 2)
			file = new File("./file/"+fileName+"."+str[1]);
		else
			file = new File("./file/"+fileName);
		
		try {
			FileOutputStream fOut = new FileOutputStream( file ); 
			BufferedOutputStream bos = new BufferedOutputStream(fOut);
			
			bos.write(binaryStringToByteArray(data));
			
			bos.close();
			fOut.close();
			
			SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy-MM-dd");
			Date curtime2 = new Date();
			String time2 = format2.format(curtime2);
			
			dao.insertData("/"+time2, file);
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean saveImageData(String deviceId, MultipartFile data) {
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddmmss");
		Date curtime = new Date();
		String time1 = format1.format(curtime);
		
		String fileName = deviceId +"_" + time1;
		
		File file = new File("./file/"+fileName+"."+FilenameUtils.getExtension(data.getOriginalFilename()));
		
		try {
			FileOutputStream fOut = new FileOutputStream( file ); 
			BufferedOutputStream bos = new BufferedOutputStream(fOut);
			
			bos.write(data.getBytes());
			
			bos.close();
			fOut.close();
			
			SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy-MM-dd");
			Date curtime2 = new Date();
			String time2 = format2.format(curtime2);
			
			dao.insertData("/"+time2, file);
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean register(String deviceId, String dataType, String protocol) {
		return dao.register(deviceId, dataType, protocol);
	}

	@Override
	@Scheduled(cron = "0 0 0 * * *")
	public void createFolder() {
		// 날짜마다 폴더를 만들기.
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
		Date time = new Date();
		String time1 = format1.format(time);		
	    dao.createDir(time1 + "/");
	}
	
	public static byte[] binaryStringToByteArray(String s) {
        int count = s.length() / 8;
        byte[] b = new byte[count];
        for (int i = 1; i < count; ++i) {
            String t = s.substring((i - 1) * 8, i * 8);
            b[i - 1] = binaryStringToByte(t);
        }
        return b;
    }

    public static byte binaryStringToByte(String s) {
        byte ret = 0, total = 0;
        for (int i = 0; i < 8; ++i) {
            ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0;
            total = (byte) (ret | total);
        }
        return total;
    }
	
}
