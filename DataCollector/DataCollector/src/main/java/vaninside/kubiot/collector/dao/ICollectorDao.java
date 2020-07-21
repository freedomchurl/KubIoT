package vaninside.kubiot.collector.dao;

public interface ICollectorDao {
	void insertData(String deviceId, String data, String group, String time); // 기기 아이디, 정보
	void createDevice(); // 기기 아이디 / 형식
	void selectDevice(); // 기기 조회
}
