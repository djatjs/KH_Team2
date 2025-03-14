package dao;

import java.util.List;

import model.vo.Cart;
import model.vo.CartList;
import model.vo.Member;

public interface CartDAO {

	Boolean insertCart(Member member);
	
	Cart selectCart(Member member);
	
	List<CartList> selectCartList(int ctNum);

	
	
	
	




	

	
}