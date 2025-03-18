package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.vo.CartList;
import model.vo.Menu;

public interface CartListDAO {
	Boolean insertMenuToCartList(
			@Param("ctNum")int ctNum, 
			@Param("menu") Menu menu, 
			@Param("amount") int amount);
	
	Boolean deletetMenuToCartList(
			@Param("ctNum")int ctNum, 
			@Param("menu") Menu menu, 
			@Param("amount") int amount);

	CartList seletCartByNum(@Param("clNum")int clNum);

	boolean deleteCart(@Param("clNum")int clNum);

	boolean selectCartList(int clNum);

	boolean updateCartList(@Param("clNum")int clNum, @Param("clAmount")int clAmount);

	void deleteList(int ctNum);


}