package cafeProgram;

import lombok.Data;

@Data
public class Customer {
	private String name;
	private int money;
	private int stamp=0;
	private int coffeeCoupon=0;

	public Customer(String name, int money) {
		this.name = name;
		this.money = money;
	}

	@Override
	public String toString() {
		return name + "의 보유중인 금액 : " + money + ", 스탬프=" + stamp + ", 적립된 쿠폰=" + coffeeCoupon;
	}
	
	
}
