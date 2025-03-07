package cafePro_DB.main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


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
			} while (menu!=5);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printMenu() {
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		System.out.println("일단 서버 클라이언트 형식은 갖추어봄");
		System.out.println("1. 1번");
		System.out.println("2. 2번");
		System.out.println("3. 3번");
		System.out.println("4. 4번");
		System.out.println("5. 종료");
		System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
		System.out.print("입력 : ");
	}
}
