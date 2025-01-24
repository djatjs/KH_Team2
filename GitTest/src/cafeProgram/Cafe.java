package cafeProgram;

import lombok.Data;

@Data
public class Cafe {
	// 메뉴, 가격, 매출
	private String menu;
	private int price;
	private static int income;

	

	public Cafe(String menu, int price) {
		this.menu = menu;
		this.price = price;
	}

	public int getIncome() {
		return income;
	}
	public static void addIncome(int price) {
        income += price; // 수익 추가
    }
	
	
}
