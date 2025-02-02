package cafeprogram;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;



public class Client {

	static Scanner scan = new Scanner(System.in);
	static ObjectInputStream ois;
	static ObjectOutputStream oos;
	
	public static void main(String[] args) {
		String ip = "127.0.0.1";
		int port = 5001;
		
		 try {
			Socket socket = new Socket(ip, port);
			System.out.println("[카페 프로그램 연결 완료]");
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			int menu = 0;
			while(true) {
				printMenu();
				menu = scan.nextInt();
				scan.nextLine();
				oos.writeInt(menu);
				oos.flush();
				runMenu(menu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void printMenu() {
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		System.out.println("1. 로그인"); //주문은 로그인 후에 가능하도록 하기
		System.out.println("2. 회원가입");
		System.out.println("3. 종료");
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		System.out.print("입력 : ");
		
	}

	private static void runMenu(int menu) {
		switch(menu) {
		case 1:
			login();
			break;
		case 2:
			registerId();
			break;
		case 3:
			System.out.println("[프로그램 종료]");
			break;
		default:
			System.out.println("잘못된 입력.");
		}
		
	}

	private static void login() {
		Customer tmp = inputInfo();
		try {
			// 서버로 객체 보낸 후 로그인 성공 유무 및 아이디 유형에 따른 메뉴를 위한 반환값 대기
			oos.writeObject(tmp);
			oos.flush();
			//로그인 실패 : 0, 회원 : 1, 관리자 : 2로 나눔
			int menu = ois.readInt();
			AfterLoginMenu(menu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void AfterLoginMenu(int menu) {
		int subMenu=0;
		switch(menu) {
		case 0:
			System.out.println("[로그인 실패]");
			break;
		case 1:
			//음료 메뉴 출력 후 주문대기
			System.out.println("[회원 아이디 로그인 성공!]");
//			System.out.println("<카페 메뉴>");
//			
//			printCustomerMenu();
//			subMenu = scan.nextInt();
//			scan.nextLine();
//			try {
//				//1. 주문하기 2. 이전으로
//				oos.writeInt(subMenu);
//				oos.flush();
//				//회원 기능 실행
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			break;
		case 2:
			//관리자 기능 안내
			System.out.println("[관리자 아이디 로그인 성공!]");
			do {
				printAdminMenu();
				subMenu = scan.nextInt();
				scan.nextLine();
				try {
					//서버로 원하는 기능의 번호를 보냄
					//이거를 서버에서 받아야하는데 받는걸 처리하지 못해서 오류 발생함
					oos.writeInt(subMenu);
					oos.flush();
					//관리자 기능 실행
					runAdminMenu(subMenu);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}while(subMenu!=5);
			break;
		}
		
	}
	//회원 안내 메뉴
	private static void printCustomerMenu() {
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		System.out.println("1. 주문");
		System.out.println("2. 이전으로");
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		System.out.print("입력 : ");
	}
	//관리자 안내 메뉴
	private static void printAdminMenu() {
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		System.out.println("1. 메뉴 추가");
		System.out.println("2. 메뉴 수정");
		System.out.println("3. 메뉴 삭제");
		System.out.println("4. 매출 확인");
		System.out.println("5. 이전으로");
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		System.out.print("입력 : ");
	}
	//관리자 메뉴 실행
	private static void runAdminMenu(int subMenu) {
		switch(subMenu) {
		case 1:
			insertMenu();
			break;
		case 2:
			editMenu();
			break;
		case 3:
			deleteMenu();
			break;
		case 4:
			checkIncome();
			break;
		case 5:
			System.out.println("[로그아웃 : 이전으로 돌아갑니다]");
			break;
		default:
			System.out.println("[잘못된 입력. 다시 입력하시오]");
		}
		
	}
	//카페 메뉴 등록
	private static void insertMenu() {
		Cafe menu = inputCafeMenu();
		try {
			oos.writeObject(menu);
			oos.flush();
			boolean res = ois.readBoolean();
			if(res){
				System.out.println("[카페 메뉴 등록 완료]");
			}
			else {
				System.out.println("[카페 메뉴 등록 실패 : 이미 등록된 메뉴]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//카페 메뉴 수정
	private static void editMenu() {
		try {
			// 서버로부터 카페 메뉴 리스트 받아오고 출력
			List<Cafe> list = (ArrayList<Cafe>) ois.readObject();
			if(list.isEmpty()) {
				System.out.println("등록된 메뉴가 없습니다.");
				return;
			}
			for(Cafe cafe : list) {
				System.out.println(cafe);
			}
			// 수정하고자 하는 메뉴의 번호를 수정하고자하는 정보를 담은 객체와 함께 서버로 전송
			//음료 번호
			int num= -1;
			do {
				System.out.print("수정할 메뉴 번호 : ");
				num = scan.nextInt();
				if(num<1 || num>list.size()) {
					System.out.println("[잘못된 입력. 다시 입력하시오.]");
				}
			}while(num<1 || num>list.size());
			//수정될 정보 객체
			Cafe menu = inputCafeMenu();
			//서버로 전송
			oos.writeInt(num);
			oos.writeObject(menu);
			oos.flush();
			// 수정 결과 여부 반환받음
			boolean res= ois.readBoolean();
			if(res) {
				System.out.println("[메뉴 수정 완료]");
			}else {
				System.out.println("[메뉴 수정 실패]");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//카페 메뉴 삭제
	private static void deleteMenu() {
		try {
			// 서버로부터 카페 메뉴 리스트 받아오고 출력
			List<Cafe> list = (ArrayList<Cafe>) ois.readObject();
			if(list.isEmpty()) {
				System.out.println("등록된 메뉴가 없습니다.");
				return;
			}
			for(Cafe cafe : list) {
				System.out.println(cafe);
			}
			// 삭제하고자 하는 메뉴의 번호를 서버로 전송
			int num= -1;
			do {
				System.out.print("삭제할 메뉴 번호 : ");
				try {
					num = scan.nextInt();
					if(num<1 || num>list.size()) {
						System.out.println("[잘못된 입력. 다시 입력하시오.]");
					}					
				} catch (InputMismatchException e) {
					System.out.println("[잘못된 입력. 숫자를 입력하시오.]");
					scan.nextLine();
				}
			}while(num<1 || num>list.size());
			
			oos.writeInt(num);
			oos.flush();
			
			// 삭제 결과 여부 반환받음
			boolean res= ois.readBoolean();
			if(res) {
				System.out.println("[메뉴 삭제 완료]");
			}else {
				System.out.println("[메뉴 삭제 실패]");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//매출 확인
	private static void checkIncome() {
		try {
			int income = ois.readInt();
			printIncome(income);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//매출 확인 출력
	private static void printIncome(int income) {
		//일단은 총 매출
		System.out.println("총 매출 : "+income+"원");
	}
	
	//카페 메뉴(음료) 정보 입력
	private static Cafe inputCafeMenu() {
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		System.out.print("메뉴명 : ");
		String name = scan.next();
		scan.nextLine();
		System.out.print("가격 : ");
		int price = scan.nextInt();
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		return new Cafe(name, price);
	}

	//회원가입
	private static void registerId() {
		Customer tmp = inputForRegister();
		try {
			// 서버로 객체 보낸 후 회원가입 성공 유무 반환 대기
			oos.writeObject(tmp);
			oos.flush();
			// 성공 유무에 따른 알림
			boolean res = ois.readBoolean();
			if(res) {
				System.out.println("[회원 가입 성공!]");
			}else {
				System.out.println("[회원 가입 실패 : 중복된 아이디]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static Customer inputInfo() {
		System.out.print("아이디 : ");
		String id = scan.nextLine();
		System.out.print("비밀번호 : ");
		String password = scan.nextLine();
		return new Customer(id, password);
	}
	private static Customer inputForRegister() {
		Customer tmp = inputInfo();
		//비밀번호 확인
		String checkPassword = "";
		while(!checkPassword.equals(tmp.getPassword())) {
			System.out.print("비밀번호 확인 : ");
			checkPassword = scan.nextLine();
			if(!checkPassword.equals(tmp.getPassword())) {
				System.out.println("비밀번호와 일치하지 않음. 다시 입력하세요.");
			}
		}
		return tmp;
	}

}
