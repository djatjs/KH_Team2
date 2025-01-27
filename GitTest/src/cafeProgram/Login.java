package cafeProgram;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import day16.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

public class Login {

	static Scanner scan = new Scanner(System.in);
	static ArrayList<customer>list = new ArrayList<customer>();
	
	public static void main(String[] args) {
		
		String ip = "127.0.0.1";
		int port = 9999;
		Socket socket = null;
		ObjectInputStream ois;
		ObjectOutputStream oos;
		
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
		
		do {
			printMenu();
			
			menu = scan.nextInt();
			scan.nextLine();
			
			try {
				oos.writeInt(menu);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			runMenu(menu, ois, oos);
			
		}while(menu != 3);
	}

	
	private static void printMenu() {
		System.out.println("--------------------");
		System.out.println("1. 로그인");
		System.out.println("2. 회원가입");
		System.out.println("3. 뒤로가기");
		System.out.println("--------------------");
		System.out.println("메뉴 선택 : ");
	}

	
	private static void runMenu(int menu, ObjectInputStream ois, ObjectOutputStream oos) {
		switch (menu) {
		case 1: login(ois, oos);
			break;
		case 2: signUp();
			break;
		case 3: 
			break;
		default: System.out.println("잘못된 입력입니다.");
		}
	}


	private static void login(ObjectInputStream ois, ObjectOutputStream oos) {
		try {
			customer customer = input();
			
			oos.writeObject(customer);
			oos.flush();
			
			//서버에서 등록 결과를 받음
			boolean res = ois.readBoolean();
			
			//알림을 출력
			if(res) {
				System.out.println("회원가입이 완료됐습니다.");
			}else {
				System.out.println("회원가입이 실패했습니다.");
				System.out.println("아이디 중복 또는 비밀번호가 맞지 않습니다.");
			}
		}catch(Exception e) {
			System.out.println("예외 발생, 나중에 지우기");
		}
	}

	private static Post inputBase() {
		System.out.print("제목 : ");
		String title = scan.nextLine();
		System.out.print("내용 : ");
		String content = scan.nextLine();
		return new Post(title, content, "");
	}
	
	private static Post input() {
		Post post = inputBase();
		
		System.out.print("작성자 : ");
		String writer = scan.next();
		
		post.setWriter(writer);
		
		return post;
	}

	private static void signUp() {
		// TODO Auto-generated method stub
		
	}

	login
	private static void insertCustomer() {
		System.out.print("아이디 : ");
		String name = scan.nextLine();
	
		System.out.println("[회원가입이 완료 됐습니다.]");
	}
}
@Data
@AllArgsConstructor
class customer{
	private String name;
	private String passWord;
	
	@Override
	public String toString() {
		return "아이디 : " +name + "," +"비밀번호 : "+ "passWord";
	}
}