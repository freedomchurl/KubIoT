package vaninside.kubiot.collector.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONObject;
import org.mortbay.util.ajax.JSON;
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
	public boolean saveData(String deviceId, String dataType, ArrayList<Double> data, ArrayList<String> time, String regi) {
		// save float data
		if(regi.equals("0")) {
			// 등록안된 기기
			if(!register(deviceId, dataType)) return false;
		} 
		
		return saveDoubleData(deviceId, data, time);
	}

	@Override
	public boolean saveData(String deviceId, String dataType, MultipartFile data, String time, String regi) {
		if(regi.equals("0")) {
			// 등록안된 기기
			if(!register(deviceId, dataType)) return false;
		} 
		
		return saveImageData(deviceId, data, time);
	}

	@Override
	public boolean saveData(String deviceId, String dataType, String data, String time, String regi) {
		System.out.println("inside");
		if(regi.equals("0")) {
			// 등록안된 기기
			if(!register(deviceId, dataType)) return false;
		} 
		
		if(dataType.equals("image")) {
			return saveImageData(deviceId, data, time);
		} else {
			// double 위치인데 필요 없어짐.
			return true;
		}
	}

	@Override
	@SuppressWarnings("resource") 
	public boolean saveDoubleData(String deviceId, ArrayList<Double> data, ArrayList<String> time) {
		// Workbook 생성 
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddmmss");
		Date curtime = new Date();
		String time1 = format1.format(curtime);
		
		String fileName = deviceId +"_" + time1;
		
		File file = new File(fileName);
		
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
				
				dao.insertData("/"+time2, file);
				
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		
			return true;
	}

	@Override
	public boolean saveImageData(String deviceId, String data, String time) {
		// TODO Auto-generated method stub
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddmmss");
		Date curtime = new Date();
		String time1 = format1.format(curtime);
		
		String fileName = deviceId +"_" + time1;
		
		File file = new File(fileName);
		
		//File file = new File(data.getOriginalFilename());
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(binaryStringToByteArray(data));
			fos.close();
			
			SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy-MM-dd");
			Date curtime2 = new Date();
			String time2 = format2.format(curtime2);
			
			dao.insertData("/"+time2, file);
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean saveImageData(String deviceId, MultipartFile data, String time) {
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddmmss");
		Date curtime = new Date();
		String time1 = format1.format(curtime);
		
		String fileName = deviceId +"_" + time1;
		
		File file = new File(fileName);
		
		//File file = new File(data.getOriginalFilename());
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data.getBytes());
			fos.close();
			
			SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy-MM-dd");
			Date curtime2 = new Date();
			String time2 = format2.format(curtime2);
			
			dao.insertData("/"+time2, file);
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean register(String deviceId, String dataType) {
		return dao.register(deviceId, dataType);
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
