package cafeprogram;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		int port = 5001;
		
		while(true) {			
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				Socket socket = serverSocket.accept();
				System.out.println("클라이언트와 연결됨");
				System.out.println("[새로운 고객이 카페에 들어왔습니다!]");
				
				//카페 프로그램 실행
				while(true) {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

	}

}
