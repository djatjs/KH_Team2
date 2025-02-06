package cafeProgram;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



public class Server2 {
	private static int Port = 9999; // 사용할 포트 번호
	private static List<Cafe> list ; // 카페 메뉴 리스트
	private static List<Customer> user = new ArrayList<Customer>(); //사용자 정보 저장
	private static String fileName = "src/cafeProgram/data2.txt";//데이터 저장
	private static String fileName2 = "src/cafeProgram/cafe.txt";//데이터 저장
	
	public static void main(String[] args) {
		
		//sample data
		user.add(new Customer("admin", "admin"));
		
		user = (List<Customer>) (load(fileName));
		list = (List<Cafe>) (load(fileName2));
	
		//리스트 null 상태 확인
		if(user ==null || user.isEmpty()) {
			user = new ArrayList<Customer>();
			
			//sample data
			user.add(new Customer("admin","admin"));
		}
		if(list ==null || list.isEmpty()) {
			list = new ArrayList<Cafe>();
		}
		
		try (ServerSocket serverSocket = new ServerSocket(Port)) {
			System.out.println("[서버가 실행 중 입니다...]");

			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("[클라이언트가 연결되었습니다.]");

				new Handler(socket).start();
			}
		} catch (IOException e) {
			System.out.println("[오류가 발생했습니다.]");
			save(fileName, user);
			save(fileName2, list);
		}
	}
	private static void save(String fileName, Object obj) {
		try(FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos)){
			oos.writeObject(obj);
			System.out.println("저장");
		} catch (Exception e) {
			System.out.println("-----------------");
			System.out.println("저장하기 실패");
			System.out.println("-----------------");
		}
		
	}

	private static Object load(String fileName) {
		try(FileInputStream fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis)){
			System.out.println("불러오기");
			return ois.readObject();
		} catch (Exception e) {
			System.out.println("-----------------");
			System.out.println("불러오기 실패");
			System.out.println("-----------------");
		}
		 return null;
	
}
	
	private static class Handler extends Thread {
		private Socket socket;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());

				while (true) {
					int menu = ois.readInt(); // 클라이언트가 선택한 메뉴

					switch (menu) {
					case 1: // 로그인 처리
						login();
						break;
					case 2: // 회원가입 처리
						signUp();
						break;
					case 3: // 클라이언트 종료
						System.out.println("[클라이언트가 종료하였습니다.]");
						save(fileName, user);
						save(fileName2, list);
						socket.close();
						return;
					default:
						System.out.println("[잘못된 입력입니다.]");
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("[클라이언트가 연결을 종료하였습니다.]");
			}
		}

		private void login() throws IOException, ClassNotFoundException {
			//로그인 정보 받음
			Customer customer = (Customer) ois.readObject();
			//로그인 확인
			boolean res = user.contains(customer);
			
			oos.writeBoolean(res);
			oos.flush();

			if (res) {
				boolean admin = customer.getId().equals("admin");// 관리자인지 여부 받기
				oos.writeBoolean(admin); 
			    oos.flush();
			    
				System.out.println("[" + customer.getId() + "님이 로그인 하였습니다.]");

				if (admin) {
					adminMenu(); // 관리자 메뉴 실행
				}else {
					userMenu(customer); // 일반 사용자 메뉴 실행
				}
			}else {
				System.out.println("[아이디/비밀번호가 일치하지 않습니다.]");
			}
		}

		private void adminMenu() throws IOException {
			int menu;
			
			do {
				menu = ois.readInt(); // 클라이언트로부터 메뉴 선택 받기
				switch (menu) {
				case 1:
					insertCafeMenu();
					break;
				case 2:
					editCafeMenu();
					break;
				case 3:
					deleteCafeMenu();
					break;
				case 4:
					CheckIncome();
					break;
				case 5:
					deleteUser();
					break;
				case 6:
					System.out.println("[로그아웃을 합니다.]");
					return;
				default:
					System.out.println("[잘못된 메뉴 선택입니다.]");
				}
			} while (menu != 6);
		}

		private void deleteUser() {
			try {
				//클라이언트로 리스트 보냄
				oos.writeObject(user);
				oos.flush();
				oos.reset();
				//리스트가 null상태이거나 담긴 메뉴가 없으면 서버도 리턴처리
				if(user ==null || user.isEmpty()) {
					return;
				}
				
				//클라이언트로부터 삭제할 메뉴의 번호를 받음
				int index = ois.readInt();
				boolean res;
				if(user.remove(user.get(index))) {
					res = true;
				}
				else {
					res =false;
				}
				oos.writeBoolean(res);
				oos.flush();
				System.out.println("회원이 삭제 되었습니다.");
			}catch (Exception e) {
				e.printStackTrace();
			}save(fileName, user);
			save(fileName2, list);
		}

		private void insertCafeMenu() {
			try {
				//클라이언트로부터 카페(메뉴) 객체 받아옴
				Cafe menu = (Cafe)ois.readObject();
				//이미 등록된 메뉴인지 확인
				boolean res;
				if(!list.contains(menu)) {
					res = true;
					list.add(menu);
				}else {
					res = false;
				}
				oos.writeBoolean(res);
				oos.flush();
				System.out.println(list + " 메뉴가 추가 되었습니다.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		private void editCafeMenu() {
			try {
				//클라이언트로 리스트 보냄
				oos.writeObject(list);
				oos.flush();
				oos.reset();
				//리스트가 null상태이거나 담긴 메뉴가 없으면 서버도 리턴처리
				if(list ==null || list.isEmpty()) {
					return;
				}
				//클라이언트로부터 수정할 메뉴의 번호를 받음
				int index = ois.readInt();
				//클라이언트로부터 수정할 메뉴 정보까지 받음
				Cafe tmp = (Cafe)ois.readObject();
				Cafe menu = list.get(index);
				//수정 후 결과 반환
				boolean res;
				if(menu.update(tmp)) {
					res = true;
				}else {
					res =false;
				}
				oos.writeBoolean(res);
				oos.flush();
				System.out.println(list + " 메뉴가 수정 되었습니다.");
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		private void deleteCafeMenu() {
			try {
				//클라이언트로 리스트 보냄
				oos.writeObject(list);
				oos.flush();
				oos.reset();
				//리스트가 null상태이거나 담긴 메뉴가 없으면 서버도 리턴처리
				if(list ==null || list.isEmpty()) {
					return;
				}
				
				//클라이언트로부터 삭제할 메뉴의 번호를 받음
				int index = ois.readInt();
				//System.out.println(list.get(index)+"삭제할 예정");
				boolean res;
				if(list.remove(list.get(index))) {
					res = true;
				}
				else {
					res =false;
				}
				oos.writeBoolean(res);
				oos.flush();
				System.out.println(list + " 메뉴가 삭제 되었습니다.");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}


		private void CheckIncome() {
			// TODO Auto-generated method stub
			
		}

		private void userMenu(Customer customer) throws IOException {
			int menu;
			
			do {
				menu = ois.readInt();
				switch (menu) {
				case 1:
					System.out.println("주문 기능 (구현 필요)");
					buyDrink(customer);
					break;
				case 2:
					System.out.println("[로그아웃을 합니다.]");
					return;
				default:
					System.out.println("[잘못된 메뉴 선택입니다.]");
				}
			} while (menu != 2);
		}

		private void buyDrink(Customer customer) {
			try {
				//클라이언트로 리스트 보냄
				oos.writeObject(list);
				oos.flush();
				oos.reset();
				//리스트가 null상태이거나 담긴 메뉴가 없으면 서버도 리턴처리
				if(list ==null || list.isEmpty()) {
					return;
				}
			}catch (Exception e) {
					// TODO: handle exception
				}
		
		}

		private void signUp() throws IOException, ClassNotFoundException {
			//회원정보 정보 받음
			Customer customer = (Customer) ois.readObject();
			
			boolean res = true;
			for(Customer tmp : user) {
				if(tmp.getId().equals(customer.getId())) {
					res= false;
					break;
				}
			}
			
			if (res) {
				user.add(customer);
				save(fileName, user);
				save(fileName2, list);
			}

			oos.writeBoolean(res);
			oos.flush();

			if (res) {
				System.out.println(customer);
				System.out.println("[서버 : 회원가입이 완료되었습니다.]");
			} else {
				System.out.println("[서버 : 회원가입이 실패했습니다.]");
				System.out.println("[아이디 중복 혹은 비밀번호가 일치하지 않습니다.]");
			}
		}
	}
}