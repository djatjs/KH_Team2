package dao;

import java.util.List;

import model.vo.Order;

public interface OrderDAO {

	List<Order> orderView(String mId);


}
