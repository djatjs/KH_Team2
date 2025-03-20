package dao;

import org.apache.ibatis.annotations.Param;
import model.vo.Member;


public interface MemberDAO {
	Member selectMember(@Param("member")Member member);

	void insertMember(@Param("member")Member member);

	Member findPw(@Param("member")Member member);

	boolean updateMember(@Param("member")Member member);

	boolean selectDeletedId(@Param("member")Member login);

	void Updat(@Param("member")Member member);

	boolean UpdateDeleteEvent(@Param("member")Member member);

	boolean restory(@Param("member")Member member);

	boolean dropEvent(@Param("member")Member member);
	



		




	
}
