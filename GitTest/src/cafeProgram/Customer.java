package cafeProgram;

import lombok.Data;

@Data
public class Customer {
	private String name;
	private String passWord;
	private int money;
	private int stamp=0;
	private int coffeeCoupon=0;
	private boolean isMembership;

	public Customer(String name, String passWord) {
		this.name = name;
		this.passWord = passWord;
	}

	@Override
	public String toString() {
		return name + "의 보유중인 금액 : " + money + ", 스탬프=" + stamp + ", 적립된 쿠폰=" + coffeeCoupon;
	}
	
	
}
