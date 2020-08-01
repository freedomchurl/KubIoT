package vaninside.kubiot.collector.dao;

import java.io.File;

public interface ICollectorDao {
	public boolean createDir(String folderName);
	public boolean register();
	public boolean insertData(String folderName, File file);
}
