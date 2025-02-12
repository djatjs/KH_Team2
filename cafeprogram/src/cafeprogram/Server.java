package cafeprogram;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Server {
	private static int Port = 9999; // 사용할 포트 번호

	private static List<Cafe1> list; // 카페 메뉴 리스트
	private static List<Customer> user = new ArrayList<Customer>(); //사용자 정보 저장
	private static List<Income1> incomes= new ArrayList<Income1>();

	public static void main(String[] args) {
		
		//불러오기
		String cafeFile = "src/cafePro/cafe.txt";
		String userFile = "src/cafePro/user.txt";
		String incomeFile = "src/cafePro/income.txt"; 
		list = (List<Cafe1>) load(cafeFile);
		user = (List<Customer>) load(userFile);
		incomes = (List<Income1>) load(incomeFile);
		
		// 리스트 초기화 (null 체크 후)
        if (list == null) {
            list =  new ArrayList<Cafe1>(); // 리스트 초기화
        }
		if (user == null || user.isEmpty()) {
            user =  new ArrayList<Customer>(); // 리스트 초기화
            //sample data
            user.add(new Customer("admin", "admin"));
        }
		if (incomes == null) {
            incomes =  new ArrayList<Income1>(); // 리스트 초기화
        }
		
		
		//클라이언트 연결 대기
		try (ServerSocket serverSocket = new ServerSocket(Port)) {
			System.out.println("[서버가 실행 중 입니다...]");

			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("[클라이언트가 연결되었습니다.]");

				new Handler(socket).start();
				save(cafeFile, list);
				save(userFile, user);
				save(incomeFile, incomes);
			}
		} catch (IOException e) {
			System.out.println("[오류가 발생했습니다.]");
		}
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
		//로그인
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
		//관리자 메뉴
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
					System.out.println("[로그아웃을 합니다.]");
					break;
				default:
					System.out.println("[잘못된 메뉴 선택입니다.]");
				}
			} while (menu != 5);
		}
		//관리자 : 메뉴 추가
		private void insertCafeMenu() {
			try {
				//클라이언트로부터 카페(메뉴) 객체 받아옴
				Cafe1 menu = (Cafe1)ois.readObject();
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
				System.out.println(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		//관리자 : 메뉴 수정
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
				Cafe1 tmp = (Cafe1)ois.readObject();
				Cafe1 menu = list.get(index);
				//수정 후 결과 반환
				boolean res;
				if(menu.update(tmp)) {
					res = true;
				}else {
					res =false;
				}
				oos.writeBoolean(res);
				oos.flush();
				System.out.println(list);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		//관리자 : 메뉴 삭제
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
				System.out.println(list);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		//관리자 : 매출 확인
		private void CheckIncome() {
			try {
				int sum=0;
				for(Income1 tmp : incomes) {
					sum+=tmp.getMoney();
				}
				oos.writeInt(sum);
				oos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
//			int menu=0;
//			do {
//				try {
//					menu = ois.readInt();
//					switch(menu) {
//					case 1:
//						//getDayIncome();
//						break;
//					case 2:
//						//getWeekIncome();
//						break;
//					case 3:
//						//getMonthIncome();
//						break;
//					case 4:
//						//getYearIncome();
//						break;
//					case 5:
//						getTotalIncome();
//						break;
//					case 6:
//						System.out.println("[이전 메뉴로 돌아갑니다]");
//						break;
//					default:
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//			}while(menu!=6);
		}
		private void getTotalIncome() {
			
			
		}

		//사용자 메뉴
		private void userMenu(Customer customer) throws IOException {
			int menu;
			
			do {
				menu = ois.readInt();
				switch (menu) {
				case 1:
					order(customer);
					break;
				case 2:
					System.out.println("[서버 : 로그아웃을 합니다.]");
					return;
				default:
					System.out.println("[서버 : 잘못된 메뉴 선택입니다.]");
				}
			} while (menu != 2);
		}
		//사용자 : 주문(메뉴 목록 및 주문할 메뉴 번호 받기)
		private void order(Customer customer) {
			try {
				//클라이언트에 카페 메뉴 목록 전송
				oos.writeObject(list);
				oos.flush();
				oos.reset();
				//리스트가 null상태이거나 담긴 메뉴가 없으면 서버도 리턴처리
				if(list ==null || list.isEmpty()) {
					return;
				}
				//클라이언트로부터 삭제할 메뉴의 번호를 받음
				int index = ois.readInt();
				
				//구매 절차
				buyDrink(customer, index);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		//사용자 : 주문(결제)
		private void buyDrink(Customer customer, int index) {
			//누가 어떤 메뉴를 주문할지에 대한 정보를 다 전달받은 상황.
			Cafe1 menu = list.get(index);
			//서버에 주문될 메뉴와 고객정보 보냄
			try {
				oos.writeObject(menu);
				oos.writeObject(customer); //이거 보내야하나?
				oos.flush();
				//사용자가 쿠폰을 가지고 있다면
				if(customer.getCoupon()>0) {
					//클라이언트로부터 쿠폰 사용할지 여부 전달받음
					String isUse = ois.readUTF();
					//O라고 대답하면 쿠폰 사용
					if(isUse.equals("o") || isUse.equals("O")) {
						System.out.println("쿠폰 사용");
						customer.useCoupon(menu, incomes);
						//쿠폰 사용 완료시 참 반환하게 끔 수정 필요
						oos.writeBoolean(true);
						oos.flush();
						System.out.println("확인용 주문 내역");
						//이거 아니긴함
						System.out.println(incomes);
						return;
					}
					//X라고 대답하면 안쓰고 스탬프 찍어줌
					else if(isUse.equals("x") || isUse.equals("X")){
						System.out.println("쿠폰을 사용하지 않아 결제 단계로 넘어갑니다");
					}
				}
				
				//결제 완료시 참 반환하게 끔 수정 필요
				customer.addStamp(menu, incomes);
				oos.writeBoolean(true);
				oos.flush();
				System.out.println("확인용 주문 내역");
				//이거 아니긴함
				System.out.println(incomes);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			//사용자가 쿠폰을 가지고 있다면 
			
			
		}

		//메뉴 : 회원가입
		private void signUp() throws IOException, ClassNotFoundException {
			//회원정보 정보 받음
			Customer customer = (Customer) ois.readObject();
			
			//등록된 아이디인지 확인
			boolean res = true;
			for(Customer tmp : user) {
				if(tmp.getId().equals(customer.getId())) {
					res= false;
					break;
				}
			}
			//등록된 아이디가 아니라면 회원가입 처리
			if (res) {
				user.add(customer);
			}
			//결과 반환
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
	private static Object load(String fileName) {
		try(FileInputStream fis = new FileInputStream(fileName);
				ObjectInputStream ois = new ObjectInputStream(fis)){
			
			return ois.readObject();
			
		} catch (Exception e) {
			System.out.println("-----------------");
			System.out.println("불러오기 실패");
			System.out.println("-----------------");
		}
		return null;
	}
	
	private static void save(String fileName, Object obj) {
		try(FileOutputStream fos = new FileOutputStream(fileName);
				ObjectOutputStream oos = new ObjectOutputStream(fos)){
			
			oos.writeObject(obj);
			
		} catch (Exception e) {
			System.out.println("-----------------");
			System.out.println("저장하기 실패");
			System.out.println("-----------------");
		}
		
	}
	
}