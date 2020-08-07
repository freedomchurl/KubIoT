package vaninside.kubiot.collector.dao;

import java.io.File;

public interface ICollectorDao {
	public boolean createDir(String folderName);
	public boolean register(String deviceId, String dataType, String protocol);
	public boolean insertData(String folderName, File file);
}
