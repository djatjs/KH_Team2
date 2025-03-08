package cafePro_DB.dao;

import org.apache.ibatis.annotations.Param;
import cafePro_DB.model.vo.Member;


public interface MemberDAO {
	Member selectMember(@Param("member")Member member);

	void insertMember(@Param("member")Member member);

	Member findPw(@Param("member")Member member);
}
