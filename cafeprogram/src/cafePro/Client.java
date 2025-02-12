package cafePro;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cafePro.program.MemberProgram;

public class Client {

	public void run() {
		String ip = "127.0.0.1"; // 서버 IP
        int port = 9999; // 서버 포트
		
        try (Socket socket = new Socket(ip, port)) {
        	ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        	ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("[서버와 연결되었습니다.]");
            
            
            MemberProgram memberProgram = new MemberProgram(oos,ois);
            memberProgram.run();

        } catch (Exception e) {
            System.out.println("[서버와 연결이 끊어졌습니다.]");
        }
		
	}


	


	



}