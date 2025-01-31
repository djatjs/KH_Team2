package cafeprogram;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	static List<Customer> customers = new ArrayList<Customer>();
	static List<Cafe> list = new ArrayList<Cafe>();
	
	static ObjectInputStream ois;
	static ObjectOutputStream oos;
	
	public static void main(String[] args) {
		int port = 5001;
		
		//관리자 아이디 : false로 설정함으로써 관리자 아이디로 설정함
		Customer admin = new Customer("admin", "1234");
		admin.setIsMember("관리자");
		System.out.println(admin);
		
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(true) {			
				try {
					// 클라이언트와의 연결
					Socket socket = serverSocket.accept();
					System.out.println("클라이언트와 연결됨");
					System.out.println("[새로운 고객이 카페에 들어왔습니다!]");
					// IO 작업용
					oos = new ObjectOutputStream(socket.getOutputStream());
					ois = new ObjectInputStream(socket.getInputStream());
					
					//카페 프로그램 실행
					while(true) {
						int menu = ois.readInt();
						System.out.println("[선택 : "+ menu +"]");
						runMenu(menu);
					}
				} catch (Exception e) {
					System.out.println("[고객이 카페에서 나갔습니다]");
				}
			}
		} catch (Exception e) {
			System.out.println("[예상치 못한 오류 발생]");
		}
		

	}
	private static void runMenu(int menu) {
		switch(menu) {
		case 1:
			login();
			break;
		case 2:
			registerId();
			break;
		case 3:
			break;
		default:
			
		}
		
	}
	
	//로그인
	private static void login() {
		try {
			// 클라이언트로부터 로그인을 위한 객체 받음
			Customer tmp = (Customer) ois.readObject();
			
			int index = customers.indexOf(tmp);
			if(index == -1) {
				System.out.println("로그인 실패");
				oos.writeInt(0);
				oos.flush();
				return;
			}
			//있다면 로그인 성공
			//로그인 한 아이디의 유형(회원 or 관리자)에 따른 메뉴 안내
			tmp = customers.get(index);
			switch(tmp.getIsMember()) {
			case "회원":
				oos.writeInt(0);
				oos.flush();
				loginMember(tmp);
				break;
			case "관리자":
				oos.writeInt(0);
				oos.flush();
				loginAdmin();
				break;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void loginMember(Customer tmp) {
		System.out.println("사용자 메뉴를 보여줄 예정");
		
	}
	private static void loginAdmin() {
		System.out.println("관리자 메뉴를 보여줄 예정");
		
	}
	//회원가입
	private static void registerId() {
		try {
			// 클라이언트로부터 가입을 위한 객체 받음
			Customer tmp = (Customer) ois.readObject();
			//중복인지 확인하기
			boolean res = true;
			for(Customer cus : customers) {
				if(cus.getId().equals(tmp.getId())) {
					//중복된 아이디라면 아이디 생성 실패.
					res = false;
					oos.writeBoolean(res);
					oos.flush();
					return;
				}				
			}
			//중복된 아이디가 아니라면 추가후 true
			customers.add(tmp);
			res = true;
			oos.writeBoolean(res);
			oos.flush();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
