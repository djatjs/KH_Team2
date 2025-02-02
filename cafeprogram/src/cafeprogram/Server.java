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
		Customer admin = new Customer("admin", "1111");
		admin.setType("관리자");
		customers.add(admin);
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
		//while(true) {
			switch(menu) {
			case 1:
				login();
				break;
			case 2:
				registerId();
				break;
			case 3:
				//그냥 고객(클라이언트)이 나간 경우 : 서버에서 뭐 할건 없긴함.
				break;
			default:
				//고객(클라이언트)의 입력이 잘못된 경우 : 서버에서 뭐 할건 없긴 함
				break;
			}		
		//}
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
			//반복을 해야했다면 여기서 해야했네
			do {
				switch(tmp.getType()) {
				case "회원":
					oos.writeInt(1);
					oos.flush();
					loginMember(tmp);
					break;
				case "관리자":
					
					loginAdmin();
					break;
				}
			}while(true);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("tlqkf");
		}
	}
	
	//회원 로그인 후
	private static void loginMember(Customer tmp) {
		System.out.println("사용자 메뉴를 보여줄 예정");
		
	}
	//관리자 로그인 후
	private static void loginAdmin() {
		int menu=0;
		do{
			try {
				oos.writeInt(2);
				oos.flush();
				menu = ois.readInt();
				System.out.println("[클라이언트 입력 : "+ menu +"]");
				runAdmin(menu);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}while(menu==5);
		
		
	}
	private static void runAdmin(int menu) {
		switch(menu) {
		case 1:
			insertMenu();
			break;
		case 2:
			editMenu();
			break;
		case 3:
			deleteMenu();
			break;
		case 4:
			checkIncome();
			break;
		case 5:
			//고객(클라이언트)이 로그아웃한 경우 : 서버에서 뭐 할건 없긴 함
			break;
		default:
			//고객(클라이언트)의 입력이 잘못된 경우 : 서버에서 뭐 할건 없긴 함
		}
		
	}
	//메뉴 추가
	private static void insertMenu() {
		try {
			//클라이언트로부터 추가하고자하는 메뉴 정보를 담은 객체를 받음
			Cafe menu = (Cafe)ois.readObject();
			//기존 list에 등록된 메뉴인지 확인 
			boolean res =false;
			if(list.contains(menu)) {
				//등록되어 있다면 추가하지 않고 false 반환
				oos.writeBoolean(res);
				oos.flush();
			}else {
				//등록되어 있지 않다면 추가 후  true 반환
				list.add(menu);
				System.out.println(list);
				res = true;
				oos.writeBoolean(res);
				oos.flush();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//메뉴 수정
	private static void editMenu() {
		try {
			//클라이언트에 카페 메뉴들을 보내줌. list를 보내주는게 좋을거같음
			oos.writeObject(list);
			oos.flush();
			//클라이언트로부터 수정하고자하는 메뉴의 번호와 수정될 메뉴 정보를 담은 객체를 받음
			int index = ois.readInt() - 1;
			Cafe tmp = (Cafe) ois.readObject();
			
			//수정 후 bool타입으로 결과 반환
			boolean res = true;
			if(index == -1) {
				System.out.println("[수정 중 오류 발생]");
				res = false;
				oos.writeBoolean(res);
				oos.flush();
				return;
			}
			Cafe edit = list.get(index);
			
			edit.setMenu(tmp.getMenu());
			edit.setPrice(tmp.getPrice());
			oos.writeBoolean(res);
			oos.flush();
			System.out.println(list);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//메뉴 삭제
	private static void deleteMenu() {
		try {
			//클라이언트에 카페 메뉴들을 보내줌. list를 보내주는게 좋을거같음
			oos.writeObject(list);
			oos.flush();
			//클라이언트로부터 수정하고자하는 메뉴의 번호를 받음
			int index = ois.readInt() - 1;
			//삭제 후 성공 유무를 bool타입으로 결과 반환
			Cafe menu = list.get(index);
			boolean res = false;
			if(list.remove(menu)) {
				res = true;
			}
			oos.writeBoolean(res);
			oos.flush();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//매출 확인
	private static void checkIncome() {
		//클라이언트에 일단은 매출 int 형식으로 보내줌
		
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
