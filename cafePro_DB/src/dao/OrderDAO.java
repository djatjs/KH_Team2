package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.vo.Cart;
import model.vo.Member;
import model.vo.Order;

public interface OrderDAO {

	List<Order> orderView(String mId);

	void insertOrder(
			@Param("ctNum")int i, 
			@Param("mId")String string, 
			@Param("useCoupon")int useCoupon, 
			@Param("totalAmount")int totalAmount, 
			@Param("resMoney")int resMoney);

}
