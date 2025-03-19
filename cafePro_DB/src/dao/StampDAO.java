package dao;

import org.apache.ibatis.annotations.Param;

import model.vo.Member;
import model.vo.Stamp;


public interface StampDAO {

	void insertStamp(String mId);

	boolean deleteMember(String id);

	void plusStamp(String mId);

	int selectStamp(String mId);

	void resetStamp(String mId);
	

}
