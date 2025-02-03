package cafeProgram;

import java.util.ArrayList;
import java.util.List;

public class Client {
	
	static List<Customer> list2 = new ArrayList<Customer>();
	public static void main(String[] args) {
		Customer customer1 = new Customer("sun",50000);
		Customer customer2 = new Customer("선",50000);
		System.out.println(customer1);
		System.out.println(customer2);

		
		//메뉴 등록
		Cafe americano = new Cafe("아메리카노",2000);
		Cafe espresso = new Cafe("에스프레소",1500);
		Cafe caramelMacchiato = new Cafe("카라멜마끼아또",3500);
		
		//
		
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		buyCoffee(customer1, americano);
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		buyCoffee(customer1, espresso);
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		buyCoffee(customer1, caramelMacchiato);
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		buyCoffee(customer2, caramelMacchiato);
		
		
		
	}
	//커피 구매
	private static void buyCoffee(Customer customer, Cafe cafe) {
		if(customer.getMoney()<cafe.getPrice()) {
			System.out.println(cafe.getMenu()+ "을(를)돈이 부족하여 살 수 없음");
			return;
		}
		
		System.out.println(customer.getName()+" : " +cafe.getMenu()+ "을(를) 구매중");
//		//쿠폰이 있다면
//		if(customer.getCoffeeCoupon()>0) {
//			System.out.print("쿠폰을 사용할건가요? (yes : y, no : n)");
//			//String str = scan.
//		}
		
		//멤버쉽 가입 된 고객이라면 몇프로 할인 이거는 나중에 생각해보기
		
		customer.setMoney(customer.getMoney()-cafe.getPrice()); 
		addStamp(customer);
		System.out.println("구매 및 스탬프 적립 완료");
		System.out.println(customer);
		cafe.addIncome(cafe.getPrice());
		System.out.println("카페의 판매 수익 : "+ cafe.getIncome());
	}
	
	
	//스탬프 찍기
	public static void addStamp(Customer customer) {
		customer.setStamp(customer.getStamp()+1);
		if(customer.getStamp() == 10) {
			customer.setStamp(0);
			customer.setCoffeeCoupon(customer.getCoffeeCoupon()+1);
		}
	}
	
	
	
}
