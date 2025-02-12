package cafePro;


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

import cafePro.model.Cafe;
import cafePro.model.Income;
import cafePro.model.Member;

public class Server {
	private static int Port = 9999; // 사용할 포트 번호

	private static List<Cafe> list; // 카페 메뉴 리스트
	private static List<Member> user; //사용자 정보 저장
	private static List<Income> incomes;
	
	private static String cafeFile = "src/cafePro/cafe.txt";
	private static String userFile = "src/cafePro/user.txt";
	private static String incomeFile = "src/cafePro/income.txt"; 

	public static void main(String[] args) {
		
		//불러오기
		list = (List<Cafe>) load(cafeFile);
		user = (List<Member>) load(userFile);
		incomes = (List<Income>) load(incomeFile);
		
		// 리스트 초기화 (null 체크 후)
        if (list == null) {
            list =  new ArrayList<Cafe>(); // 리스트 초기화
        }
		if (user == null || user.isEmpty()) {
            user =  new ArrayList<Member>(); // 리스트 초기화
            //sample data
            user.add(new Member("admin", "admin"));
        }
		if (incomes == null) {
            incomes =  new ArrayList<Income>(); // 리스트 초기화
        }
		System.out.println(list);
		System.out.println(user);
		System.out.println(incomes);
		
		//클라이언트 연결 대기
		try (ServerSocket serverSocket = new ServerSocket(Port)) {
			System.out.println("[서버가 실행 중 입니다...]");

			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("[클라이언트가 연결되었습니다.]");

				new Handler(socket).start();
				
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
						save(cafeFile, list);
						save(userFile, user);
						save(incomeFile, incomes);
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
			Member customer = (Member) ois.readObject();
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
					deleteMember();
					break;
				case 5:
					CheckIncome();
					break;
				case 6:
					System.out.println("[로그아웃을 합니다.]");
					break;
				default:
					System.out.println("[잘못된 메뉴 선택입니다.]");
				}
			} while (menu != 6);
		}
		private void deleteMember() {
			try {
				// 클라이언트로 리스트 보냄
				oos.writeObject(user);
				oos.flush();
				oos.reset();

				// 리스트가 null상태이거나 담긴 메뉴가 없으면 서버도 리턴처리
				if (user == null || user.isEmpty()) {
					return;
				}

				// 클라이언트로부터 삭제할 회원의 번호를 받음
				int index = ois.readInt();

				if (index == -1) { // 클라이언트에서 취소 신호를 보낸 경우
					System.out.println("[회원 삭제를 취소하였습니다.]");
					return;
				}

				// 인덱스가 유효한지 확인
				if (index < 0 || index >= user.size()) {
					oos.writeBoolean(false);
					oos.flush();
					System.out.println("[잘못된 번호입니다.]");
					return;
				}

				// 삭제할 회원 정보 가져오기
				Member targetUser = user.get(index);

				// admin 계정은 삭제 불가능하도록 처리
				if (targetUser.getId().equals("admin")) {
					oos.writeBoolean(false); // 삭제 실패 응답
					oos.flush();
					System.out.println("[관리자 계정(admin)은 삭제할 수 없습니다.]");
					return;
				}

				boolean res;
				if (user.remove(user.get(index))) {
					res = true;
				} else {
					res = false;
				}
				oos.writeBoolean(res);
				oos.flush();
				System.out.println("[회원이 삭제 되었습니다.]");
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
		}
		//관리자 : 메뉴 추가
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
		private void CheckIncome() throws IOException {
			int menu=0;
			do {
				 menu = ois.readInt(); // 클라이언트가 보낸 매출 요청
				
				if (menu == 6) { // "뒤로 가기" 선택 시 종료
					System.out.println("[이전 메뉴로 돌아갑니다.]");
					break;
				}
				
				int totalIncome = 0;
				
				switch (menu) {
				case 1:
					totalIncome = getDayIncome();// 일
					break;
				case 2:
					totalIncome = getWeekIncome();// 주
					break;
				case 3:
					totalIncome = getMonthIncome();// 월
					break;
				case 4:
					totalIncome = getYearIncome();// 년
					break;
				case 5:
					totalIncome = getTotalIncome(); // 총 매출 계산
					break;
				
				}
				
				// 정상적인 매출 데이터를 클라이언트에 전송
				oos.writeInt(totalIncome);
				oos.flush();
			}while(menu !=6);
		}
		private int getYearIncome() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String currentYear = sdf.format(new Date());
			int total = 0;

			for (Income income : incomes) {
				if (sdf.format(income.getDate()).equals(currentYear)) {
					total += income.getMoney();
				}
			}
			return total;
		}

		private int getMonthIncome() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			String currentMonth = sdf.format(new Date());
			int total = 0;

			for (Income income : incomes) {
				if (sdf.format(income.getDate()).equals(currentMonth)) {
					total += income.getMoney();
				}
			}
			return total;
		}

		private int getWeekIncome() {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			Date startOfWeek = cal.getTime();
			int total = 0;

			for (Income income : incomes) {
				if (!income.getDate().before(startOfWeek)) {
					total += income.getMoney();
				}
			}
			return total;
		}

		private int getDayIncome() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String today = sdf.format(new Date());
			int total = 0;

			for (Income income : incomes) {
				if (sdf.format(income.getDate()).equals(today)) {
					total += income.getMoney();
				}
			}
			return total;
		}

		private int getTotalIncome() {
			int total = 0;
			for (Income income : incomes) {
				total += income.getMoney();
			}
			return total;
			
		}

		//사용자 메뉴
		private void userMenu(Member customer) throws IOException {
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
		private void order(Member customer) {
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
		private void buyDrink(Member customer, int index) {
			//누가 어떤 메뉴를 주문할지에 대한 정보를 다 전달받은 상황.
			Cafe menu = list.get(index);
			//서버에 주문될 메뉴와 고객정보 보냄
			try {
				oos.writeObject(menu);
				oos.writeObject(customer);
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
						System.out.println(incomes.getLast());
						return;
					}
					
					//X라고 대답하면 안쓰고 정상결제 및 스탬프 찍어주기 위해 그냥 넘어감
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
				System.out.println(incomes.getLast());
				
			}catch (Exception e) {
				e.printStackTrace();
			}

		}

		//메뉴 : 회원가입
		private void signUp() throws IOException, ClassNotFoundException {
			//클라이언트에서 정보 잘못입력하면 false 전송후 리턴시키는 것에 맞춰가기
			boolean inputRes = ois.readBoolean();
			if(!inputRes) {
				return;
			}
			
			//회원정보 정보 받음
			Member customer = (Member) ois.readObject();
			
			//등록된 아이디인지 확인
			boolean res = true;
			for(Member tmp : user) {
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