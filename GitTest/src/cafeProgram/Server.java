package cafeProgram;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		
		int port = 5002;
		final int EXIT = 5;
		
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("[클라이언트 연결 성공]");
				
				//카페 프로그램 시작
				int menu=0;
				do {
				
					
				}while(menu !=EXIT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
