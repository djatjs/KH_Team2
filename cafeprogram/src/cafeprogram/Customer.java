package cafeprogram;

import lombok.Data;

@Data
public class Customer {
	private String id;
	private String password;
	private int isMember;
	private int stamp;
	private int coupon;
	
	
	public Customer(String id, String password) {
		this.id = id;
		this.password = password;
	}
	
	
}
