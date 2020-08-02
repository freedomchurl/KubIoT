package vaninside.kubiot.collector.dao;

import java.io.File;

public interface ICollectorDao {
	public boolean createDir(String folderName);
	public boolean register(String deviceId, String dataType);
	public boolean insertData(String folderName, File file);
}
