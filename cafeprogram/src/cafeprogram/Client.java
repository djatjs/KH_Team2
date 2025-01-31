package cafeprogram;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
		System.out.println("1. 주문"); //주문은 로그인 후에 가능하도록 하기
		System.out.println("2. 회원가입");
		System.out.println("3. 종료");
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		
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
			int menu = ois.readInt();
			AfterLoginMenu(menu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void AfterLoginMenu(int menu) {
		switch(menu) {
		case 0:
			System.out.println("[로그인 실패]");
			break;
		case 1:
			//음료 메뉴 출력 후 주문대기
			System.out.println("[회원 아이디 로그인 성공!]");
			break;
		case 2:
			//관리자 기능 안내
			System.out.println("[관리자 아이디 로그인 성공!]");			
			break;
		}
		
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
