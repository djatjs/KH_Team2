package dao;

import org.apache.ibatis.annotations.Param;
import model.vo.Member;


public interface MemberDAO {
	Member selectMember(@Param("member")Member member);

	void insertMember(@Param("member")Member member);

	Member findPw(@Param("member")Member member);

	boolean deleteMember(@Param("member")Member member);
}
