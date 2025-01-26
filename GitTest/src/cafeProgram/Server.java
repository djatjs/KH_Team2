package cafeProgram;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Server {


	private Socket socket;
	private List<Record> list;
	
	public void run() {
		Thread t = new Thread(()->{
			try {
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				System.out.println("[연결 성공!]");
				
				while(true) {
					
					//메뉴를 입력받음(클라이언트에게)
					int menu = ois.readInt();
					//메뉴에 따라 기능을 실행
					runMenu(menu, ois, oos);
				}
			}catch (Exception e) {
				System.out.println("[연결 해제]");
			}
		});
		t.start();
	}
	private void runMenu(int menu, ObjectInputStream ois, ObjectOutputStream oos) {
		switch (menu) {
		case 1:
			buyCoffee(ois, oos);
			break;
		case 2:
			customerInfo(ois, oos);
			break;
		case 3: 
            break;
        default:
            System.out.println("올바르지 않은 선택입니다. 다시 입력하세요.");
    }while(menu != 3);
	}
		
}
	

	
