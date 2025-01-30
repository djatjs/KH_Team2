package cafeProgram;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Login {

	static Scanner scan = new Scanner(System.in);
	static ArrayList<Customer2>list = new ArrayList<>();
	static ObjectOutputStream oos;
	static ObjectInputStream ois;
	
	public static void main(String[] args) {
		
		String ip = "127.0.0.1";
		int port = 9999;
		Socket socket = null;

		try {
			socket = new Socket(ip, port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("연결 성공");
		}catch (Exception e) {
			System.out.println("서버와 연결이 되지 않아 프로그램을 종료합니다.");
			return;
		}	
		
		int menu = 0;
		while (true) {
			printMenu();
		try {
            menu = scan.nextInt();
            scan.nextLine(); 

            oos.writeInt(menu);
            oos.flush();

            if (menu == 3) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            runMenu(menu);
        } catch (InputMismatchException e) {
            System.out.println("잘못입력하셨습니다.");
            scan.nextLine(); 
        } catch (IOException e) {
            System.out.println("오류가 발생했습니다.");
            break;
        }
	}
	 try {
	        if (socket != null) socket.close();
	    } catch (IOException e) {
	        System.out.println("소켓 종료 중 오류 발생");
	    }
}

	
	private static void printMenu() {
		System.out.println("--------------------");
		System.out.println("1. 로그인");
		System.out.println("2. 회원가입");
		System.out.println("3. 뒤로가기");
		System.out.println("--------------------");
		System.out.println("메뉴 선택 : ");
	}

	
	private static void runMenu(int menu) throws IOException {
		switch (menu) {
		case 1: login();
			break;
		case 2: signUp();
			break;
		case 3: 
			break;
		default: System.out.println("잘못된 입력입니다.");
		}
	}


	private static void login() {
		
		Customer2 customer2 = inputCustomer2();
		System.out.println("[접속 중]");
		Customer2 customer3 = null;
		try {
	            oos.writeInt(1);
	            oos.writeObject(customer2);
	            oos.flush();

	            customer3 = (Customer2) ois.readObject();
	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
        if (customer3 == null) {
            System.out.println("아이디 또는 비밀번호가 틀렸습니다.");
            return;
        }

        System.out.println("로그인이 성공했습니다.");
        
        int menu = 0;
		do {
			try {
				printLoginMenu();
				
				menu = scan.nextInt();
				scan.nextLine();
				
				runLoginMenu(menu, customer3);
			}catch (InputMismatchException e) {
				System.out.println("[메뉴는 숫자입니다.]");
				scan.nextLine();
			}catch (Exception e) {
				
			}
		}while(menu != 4);
		
	}
	
	
	private static void printLoginMenu() {
	    System.out.println("========= 카페 프로그램 =========");
        System.out.println("1. 커피 구매");
        System.out.println("2. 고객 정보 보기");
        System.out.println("3. 프로그램 종료");
        System.out.print("메뉴를 선택하세요: ");
	}
	
	private static void runLoginMenu(int menu, Customer2 customer2) {
		switch(menu) {
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		default:
		}
		
	}
        
	
	private static Customer2 inputCustomer2() {

		System.out.print("아이디 : ");
		String name = scan.nextLine();
		System.out.print("비밀번호 : ");
		String passWord = scan.nextLine();

		return new Customer2(name, passWord);
	}

	
	 private static void signUp() {
	        try {
	            Customer2 cus = inputCustomer2();

	            oos.writeInt(2); 
	            oos.flush();
	            oos.writeObject(cus);
	            oos.flush();

	            boolean isSuccess = ois.readBoolean();

	            if (isSuccess) {
	                System.out.println("회원가입이 성공했습니다.");
	            } else {
	                System.out.println("아이디 중복 또는 비밀번호가 틀렸습니다.");
	            }
	        } catch (IOException e) {
	            System.out.println("오류가 발생했습니다.");
	        }
	   	}
}
