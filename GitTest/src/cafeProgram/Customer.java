package cafeProgram;

import lombok.Data;

@Data
public class Customer {
	private String name;
	private String password;
	private int stamp=0;
	private int coffeeCoupon=0;
	//private money;
 	//0 : 비회원, 1 : 회원, 2 : 관리자
	private int isMembership = 0; 

	//회원 가입용 생성자
	public Customer(String name, String password) {
		this.name = name;
		this.password = password;
		this.isMembership = 1;
	}


	@Override
	public String toString() {
		return name + "의 스탬프=" + stamp + ", 적립된 쿠폰=" + coffeeCoupon;
	}
	


	public void setCoffeeCoupon(int coffeeCoupon) {
		this.coffeeCoupon = coffeeCoupon;
	}
	
	
	
	
}
