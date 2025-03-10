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

public class Server {
	private static int Port = 9999; // 사용할 포트 번호
	private static List<Cafe> list; // 카페 메뉴 리스트
	private static List<Member> user = new ArrayList<Member>(); // 사용자 정보 저장
	private static List<Income> amount = new ArrayList<Income>(); // 매출 저장
	private static String fileName = "src/cafeProgram/user.txt";// 회원 데이터 저장
	private static String fileName2 = "src/cafeProgram/cafe.txt";// 카페 데이터 저장
	private static String fileName3 = "src/cafeProgram/amount.txt";// 카페 데이터 저장

	public static void main(String[] args) {

		// sample data
		user.add(new Member("admin", "admin"));

		user = (List<Member>) (load(fileName));
		list = (List<Cafe>) (load(fileName2));
		amount = (List<Income>) (load(fileName3));

		if (amount == null || amount.isEmpty()) {
			amount = new ArrayList<Income>();
		}

		// 리스트 null 상태 확인
		if (user == null || user.isEmpty()) {
			user = new ArrayList<Member>();

			// sample data
			user.add(new Member("admin", "admin"));
		}
		if (list == null || list.isEmpty()) {
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
		try (FileOutputStream fos = new FileOutputStream(fileName);
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(obj);
			System.out.println("저장");
		} catch (Exception e) {
			System.out.println("-----------------");
			System.out.println("저장하기 실패");
			System.out.println("-----------------");
		}

	}

	private static Object load(String fileName) {
		try (FileInputStream fis = new FileInputStream(fileName); ObjectInputStream ois = new ObjectInputStream(fis)) {
			System.out.println("불러오기");
			return ois.readObject();
		} catch (Exception e) {
			System.out.println("-----------------");
			System.out.println("불러오기 실패");
			System.out.println("-----------------");
		}
		return null;

	}

	private static class Handler<Incom> extends Thread {
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
						save(fileName3, amount);
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
			// 로그인 정보 받음
			Member member = (Member) ois.readObject();
			// 로그인 확인
			boolean res = user.contains(member);

			oos.writeBoolean(res);
			oos.flush();

			if (res) {
				boolean admin = member.getId().equals("admin");// 관리자인지 여부 받기
				oos.writeBoolean(admin);
				oos.flush();

				System.out.println("[" + member.getId() + "님이 로그인 하였습니다.]");

				if (admin) {
					adminMenu(); // 관리자 메뉴 실행
				} else {
					userMenu(member); // 일반 사용자 메뉴 실행
				}
			} else {
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
			save(fileName, user);
		}

		private void insertCafeMenu() {
			try {
				// 클라이언트로부터 카페(메뉴) 객체 받아옴
				Cafe menu = (Cafe) ois.readObject();
				// 이미 등록된 메뉴인지 확인
				boolean res = false;
				if (!list.contains(menu)) {
					list.add(menu);
					save(fileName2, list); // 파일 저장
					res = true;
					System.out.println("[" + menu + "]" + "[메뉴가 추가 되었습니다.] "); // 방금 추가된 메뉴만 출력
				} else {
					System.out.println("[이미 존재하는 메뉴입니다.]");
				}

				// 클라이언트에게 응답 전송
				oos.writeBoolean(res);
				oos.writeObject(menu); // 방금 추가된 메뉴만 전송
				oos.flush();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void editCafeMenu() {
			try {
				// 클라이언트로 리스트 보냄
				oos.writeObject(list);
				oos.flush();
				oos.reset();
				// 리스트가 null상태이거나 담긴 메뉴가 없으면 서버도 리턴처리
				if (list == null || list.isEmpty()) {
					return;
				}

				// 클라이언트로부터 수정할 메뉴의 번호를 받음
				int index = ois.readInt();
				// 클라이언트로부터 수정할 메뉴 정보까지 받음
				Cafe tmp = (Cafe) ois.readObject();
				Cafe menu = list.get(index);
				// 수정 후 결과 반환
				boolean res;
				if (menu.update(tmp)) {
					res = true;
				} else {
					res = false;
				}
				oos.writeBoolean(res);
				oos.writeObject(menu);
				oos.flush();
				System.out.println("[" + menu + "]" + "[메뉴가 수정 되었습니다.] ");
			} catch (Exception e) {
				e.printStackTrace();
			}
			save(fileName2, list);
		}

		private void deleteCafeMenu() {
			try {
				// 클라이언트로 리스트 보냄
				oos.writeObject(list);
				oos.flush();
				oos.reset();
				// 리스트가 null상태이거나 담긴 메뉴가 없으면 서버도 리턴처리
				if (list == null || list.isEmpty()) {
					return;
				}

				// 클라이언트로부터 삭제할 메뉴의 번호를 받음
				int index = ois.readInt();
				// System.out.println(list.get(index)+"삭제할 예정");
				boolean res;
				if (list.remove(list.get(index))) {
					res = true;
				} else {
					res = false;
				}
				oos.writeBoolean(res);
				oos.flush();
				System.out.println("[메뉴가 삭제 되었습니다.]");
			} catch (Exception e) {
				e.printStackTrace();
			}
			save(fileName2, list);
		}

		// 관리자 : 매출 확인
		private void CheckIncome() throws IOException {
			while (true) {
				int menu = ois.readInt(); // 클라이언트가 보낸 매출 요청

				if (menu == 5) { // "뒤로 가기" 선택 시 종료
					System.out.println("[이전 메뉴로 돌아갑니다.]");
					return;
				}

				int totalIncome = 0;

				switch (menu) {
				case 1:
					totalIncome = getDayIncome();
					break;
				case 2:
					totalIncome = getWeekIncome();
					break;
				case 3:
					totalIncome = getMonthIncome();
					break;
				case 4:
					totalIncome = getYearIncome();
					break;
				default:
					System.out.println("[잘못된 입력입니다.]");
					oos.writeInt(-1); // 잘못된 입력 시 클라이언트에 -1 전송
					oos.flush();
					continue;
				}

				// 정상적인 매출 데이터를 클라이언트에 전송
				oos.writeInt(totalIncome);
				oos.flush();
			}
		}

		private int getDayIncome() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String today = sdf.format(new Date());
			int total = 0;

			for (Income income : amount) {
				if (sdf.format(income.getDateStr()).equals(today)) {
					total += income.getMoney();
				}
			}
			return total;
		}

		// 주별 매출 계산
		private int getWeekIncome() {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			Date startOfWeek = cal.getTime();
			int total = 0;

			for (Income income : amount) {
				if (!income.getDateStr().before(startOfWeek)) {
					total += income.getMoney();
				}
			}
			return total;
		}

		// 월별 매출 계산
		private int getMonthIncome() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			String currentMonth = sdf.format(new Date());
			int total = 0;

			for (Income income : amount) {
				if (sdf.format(income.getDateStr()).equals(currentMonth)) {
					total += income.getMoney();
				}
			}
			return total;
		}

		// 연별 매출 계산
		private int getYearIncome() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String currentYear = sdf.format(new Date());
			int total = 0;

			for (Income income : amount) {
				if (sdf.format(income.getDateStr()).equals(currentYear)) {
					total += income.getMoney();
				}
			}
			return total;
		}

		// 사용자 메뉴
		private void userMenu(Member member) throws IOException {
			int menu;

			do {
				menu = ois.readInt();
				switch (menu) {
				case 1:
					order(member);
					break;
				case 2:
					System.out.println("[로그아웃을 합니다.]");
					return;
				default:
					System.out.println("[잘못된 메뉴 선택입니다.]");
				}
			} while (menu != 2);
		}

		// 사용자 : 주문(메뉴 목록 및 주문할 메뉴 번호 받기)
		private void order(Member member) {
			try {
				oos.writeObject(list); // 카페 메뉴 리스트 전송
				oos.flush();

				if (list == null || list.isEmpty()) {
					return;
				}

				int index = ois.readInt(); // 주문할 메뉴 인덱스 받기
				buyDrink(member, index); // 주문 처리
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 사용자 : 주문(결제)
		private void buyDrink(Member member, int index) {

			Cafe menu = list.get(index);
			try {
				oos.writeObject(menu);
				oos.writeObject(member);
				oos.flush();

				boolean usedCoupon = false;
				int price = menu.getPrice();

				if (member.getCoupon() > 0) {
					String isUse = ois.readUTF();
					if (isUse.equalsIgnoreCase("O")) {
						System.out.println("쿠폰 사용");
						member.useCoupon(menu);
						usedCoupon = true;
						price = 0;
					}
				}

				Income newIncome = new Income(price);
				amount.add(newIncome);
				save(fileName3, amount);

				member.addStamp(menu);
				oos.writeBoolean(true);
				oos.flush();

				System.out.println("주문 완료: " + newIncome);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 회원가입
		private void signUp() throws IOException, ClassNotFoundException {
			// 회원정보 정보 받음
			Member member = (Member) ois.readObject();

			boolean res = true;
			for (Member tmp : user) {
				if (tmp.getId().equals(member.getId())) {
					res = false;
					break;
				}
			}

			if (res) {
				user.add(member);
				save(fileName, user);
			}

			oos.writeBoolean(res);
			oos.flush();

			if (res) {
				System.out.println(member);
				System.out.println("[서버 : 회원가입이 완료되었습니다.]");
			} else {
				System.out.println("[서버 : 회원가입이 실패했습니다.]");
				System.out.println("[아이디 중복 혹은 비밀번호가 일치하지 않습니다.]");
			}
		}
	}
}