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

    private static Scanner scan = new Scanner(System.in);
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    public static void main(String[] args) {
        String ip = "127.0.0.1"; // 서버 IP
        int port = 9999; // 서버 포트

        try (Socket socket = new Socket(ip, port)) {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("서버에 연결되었습니다.");

            int menu;
            do {
                printMainMenu();
                menu = scan.nextInt();
                scan.nextLine(); 

                oos.writeInt(menu);
                oos.flush();

                switch (menu) {
                    case 1: // 로그인
                        login();
                        break;
                    case 2: // 회원가입
                        signUp();
                        break;
                    case 3: // 종료
                        System.out.println("[프로그램을 종료합니다.]");
                        break;
                    default:
                        System.out.println("[잘못된 메뉴 선택입니다.]");
                }
            } while (menu != 3);
        } catch (IOException e) {
            System.out.println("[서버와 연결이 끊어졌습니다.]");
        }
    }

    private static void printMainMenu() {
        System.out.println("------------------");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("3. 종료");
        System.out.println("------------------");
        System.out.print("메뉴 선택 : ");
    }

    private static void login() throws IOException {
        System.out.print("아이디 : ");
        String id = scan.next();
        System.out.print("비밀번호 : ");
        String pw = scan.next();

        Customer customer2 = new Customer(id, pw);
        
        oos.writeObject(customer2); // 서버에 회원 정보 전송
        oos.flush();

        boolean res = ois.readBoolean(); // 서버로부터 결과 받기
        
        if (res) {
            boolean admin = ois.readBoolean(); // 관리자인지 여부 받기

            if (admin) {
                adminMenu(); // 관리자 메뉴 실행
            } else {
                userMenu(0); // 일반 사용자 메뉴 실행
            }
        } else {
            System.out.println("[아이디 또는 비밀번호가 틀렸습니다.]");
        }
    }

    private static void adminMenu() throws IOException{
    	int menu;
        do {
            adminPrintMainMenu(); 
            menu = scan.nextInt(); 
            scan.nextLine(); 

            oos.writeInt(menu);
            oos.flush();

            adminRunMainMenu(menu);
        } while (menu != 4);
    }
	       
	private static void adminPrintMainMenu() {
			
		System.out.println("------------------");
	    System.out.println("1. 메뉴 추가");
	    System.out.println("2. 메뉴 수정");
	    System.out.println("3. 매출 확인");
	    System.out.println("4. 로그아웃");
	    System.out.println("------------------");
	    System.out.print("메뉴 선택: ");
	}

	private static void adminRunMainMenu(int menu) {
		 
			switch (menu) {
		     case 1:
		         System.out.println("메뉴 추가 (구현 필요)");
		         break;
		     case 2:
		         System.out.println("메뉴 수정 (구현 필요)");
		         break;
		     case 3:
		     	System.out.println("매출확인 (구현 필요)");
		     	break;
		     case 4:
		         System.out.println("[로그아웃을 합니다.]");
		         return;
		     default:
		         System.out.println("[잘못된 메뉴 선택입니다.]");
			}
	}

	private static void userMenu(int menu) throws IOException {
		  do {
		        userPrintMainMenu();
		        menu = scan.nextInt(); 
		        scan.nextLine();
	
		        oos.writeInt(menu); 
		        oos.flush();
	
		        userRunMainMenu(menu); 
		    } while (menu != 2);
		}

	private static void userPrintMainMenu() {
		
		System.out.println("------------------");
	    System.out.println("1. 주문하기");
	    System.out.println("2. 로그아웃");
	    System.out.println("------------------");
	    System.out.print("메뉴 선택: ");
		
	}

	private static void userRunMainMenu(int menu) {
			
				switch (menu) {
			     case 1:
			         System.out.println("주문 기능 (구현 필요)");
			         break;
			     case 2:
			         System.out.println("[로그아웃을 합니다.]");
			         return;
			     default:
			         System.out.println("[잘못된 메뉴 선택입니다.]");
			}
	}

	private static void signUp() throws IOException {
        System.out.print("아이디 : ");
        String id = scan.nextLine();
        System.out.print("비밀번호 : ");
        String pw = scan.nextLine();
        System.out.print("비밀번호 확인 : ");
        String pw2 = scan.nextLine();

        if (!pw.equals(pw2)) {
            System.out.println("[비밀번호가 일치하지 않습니다.]");
            return;
        }

        Customer customer = new Customer(id, pw);
        oos.writeObject(customer); // 서버에 회원 정보 전송
        oos.flush();

        boolean res = ois.readBoolean(); // 서버로부터 결과 받기
        if (res) {
        	System.out.println("[회원가입이 완료되었습니다.]");
        } else {
            System.out.println("[이미 존재하는 아이디입니다.]");
        }
    }
}
