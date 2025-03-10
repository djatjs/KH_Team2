package dao;

import org.apache.ibatis.annotations.Param;
import model.vo.Coupon;


public interface CouponDAO {

	void insertCoupon(String mId);
	

}
