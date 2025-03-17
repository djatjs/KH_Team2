package dao;

import org.apache.ibatis.annotations.Param;
import model.vo.Coupon;
import model.vo.Member;


public interface CouponDAO {

	void insertCoupon(String mId);

	boolean deleteMember(String id);

	int selectCoupon(String mId);

	void plusCoupon(String mId);
	

}
