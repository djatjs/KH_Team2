package cafePro_DB.dao;

import org.apache.ibatis.annotations.Param;
import cafePro_DB.model.vo.Coupon;


public interface CouponDAO {

	void insertCoupon(String mId);
	

}
