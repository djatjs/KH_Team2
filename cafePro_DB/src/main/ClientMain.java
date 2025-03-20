package main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import service.CustomerManager;


public class ClientMain {
	
	static ObjectInputStream ois;
	static ObjectOutputStream oos;
	private static int port = 9999;
	private static String ip = "127.0.0.1";
	static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		try {
			// 서버와 연결
			Socket socket = new Socket(ip, port);
			
			System.out.println("[연결 성공]");
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			int menu = 0;
			do {
				printMenu();
				menu = scan.nextInt();
				oos.writeInt(menu);
				oos.flush();
				runMenu(menu);
			} while (menu!=5);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void runMenu(int menu) {
		CustomerManager memberManager = new CustomerManager(oos,ois);
		switch(menu){
		case 1:
			String type = memberManager.login();
			switch(type) {
			case "ADMIN":
				AdminProgram AdminProgram = new AdminProgram(oos, ois);
				AdminProgram.runAdmin();
				break;
			case "CUSTOMER":
				CustomerProgram customerProgram = new CustomerProgram(oos, ois);
				customerProgram.runCustomer();
				break;
			default:
				System.out.println(type);
				System.out.println("[로그인 실패 : 아이디 또는 비밀번호 오류]");
			}
			
			break;
		case 2:
			memberManager.register();
			break;
		case 3:
			memberManager.findPw();
			break;
		case 4:
			memberManager.restory();
			break;
		case 5:
			System.out.println("[프로그램을 종료합니다.]");
			break;
		default : 
			System.out.println("[잘못된 입력]");
		}
		
	}

	private static void printMenu() {
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		System.out.println("1. 로그인");
		System.out.println("2. 회원가입");
		System.out.println("3. 비밀번호 찾기");
		System.out.println("4. 아이디 복구");
		System.out.println("5. 종료");
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		System.out.print("입력 : ");
	}
	
}
