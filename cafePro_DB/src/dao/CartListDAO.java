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

	void deleteList(@Param("ctNum")int ctNum);

	boolean selectCartList(int clNum);

	boolean updateCartList(@Param("clNum")int clNum, @Param("clAmount")int clAmount);

}