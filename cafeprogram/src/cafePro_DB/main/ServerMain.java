package cafePro_DB.main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
	static ObjectInputStream ois;
	static ObjectOutputStream oos;
	private static int Port = 9999;
	public static void main(String[] args) {
		
		try {
			ServerSocket serverSocket = new ServerSocket(Port);
			System.out.println("[서버가 실행 중 입니다...]");
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("[클라이언트 연결 성공!]");
				
				int menu = 0;
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
				do {
					menu = ois.readInt();
					runMenu(menu);
				} while (menu!=5);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void runMenu(int menu) {
		switch (menu) {
		case 1:
			System.out.println("1번");
			break;
		case 2:
			System.out.println("2번");
			break;
		case 3:
			System.out.println("3번");
			break;
		case 4:
			System.out.println("4번");
			break;
		case 5:
			System.out.println("5번");
			break;
		default:
			System.out.println("[종료]");
		}
		
		
	}
}
