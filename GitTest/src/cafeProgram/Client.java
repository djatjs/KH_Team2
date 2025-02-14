package cafeProgram;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
	
	static Scanner scan = new Scanner(System.in);
	static ObjectOutputStream oos;	
	static ObjectInputStream ois;
	static List<Customer> list2 = new ArrayList<Customer>();
	public static void main(String[] args) {
		
		//ip, port 설정
		String ip = "127.0.0.1";
		int port = 5002;
		
		//소켓 객체 생성 및 서버와 연결
		try {
			Socket socket = new Socket(ip, port);
			System.out.println("[매장에 오신 걸 환영합니다.]");
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		}catch (Exception e) {
			e.printStackTrace();
		}
		//sample data : 회원 등록
		//Customer customer1 = new Customer("sun","1234", 50000);
		Customer customer2 = new Customer("선","1234");
		
		//sample data : 메뉴 등록
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
		
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		customer2.setCoffeeCoupon(1);
		System.out.println(customer2);
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		buyCoffee(customer2, americano);
	}
	//커피 구매
	private static void buyCoffee(Customer customer, Cafe cafe) {
		//비회원
		if(customer.getIsMembership()==0) { 
			
		}
		//회원
		else if (customer.getIsMembership()==1) {
			System.out.println(customer.getName()+" : " +cafe.getMenu()+ "을(를) 구매중");
			//쿠폰이 있다면
			if(customer.getCoffeeCoupon()>0) {
				System.out.print("쿠폰을 사용할건가요? (yes : y, no : n)");
				char str = scan.next().charAt(0);
				switch(str) {
				case 'y':
					//쿠폰을 사용한다면
					useCoupon(customer, cafe);
					return;
				case 'n':
					System.out.println("쿠폰을 사용하지 않아 결제단계로 넘어갑니다");
					System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
					break;
				}
			}
		}
		
		
		addStamp(customer);
		System.out.println("구매 및 스탬프 적립 완료");
		System.out.println(customer);
		
		//이건 나중에 관리자가 매출 확인 할 때 조회 가능하도록 수정할 예정
		cafe.addIncome(cafe.getPrice());
		System.out.println("카페의 판매 수익 : "+ cafe.getIncome());
		
		//멤버쉽 가입 된 고객이라면 몇프로 할인 이거는 나중에 생각해보기
		
		
	}
	
	//쿠폰 쓰면 아메리카노 드림
	private static void useCoupon(Customer customer, Cafe cafe) {
		System.out.println(customer.getName()+" : 쿠폰을 사용하여 아메리카노 구매함");
		customer.setCoffeeCoupon(customer.getCoffeeCoupon()-1);
		//카페는 이득을 보지는 않음. 재료값만 나간다고 해야하나..
		//재료값이 만약에 250원이라고 치면
		cafe.addIncome(-250);
		System.out.println(customer);
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
