package cafeProgram;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements Serializable {
	private static int Port = 9999; // 사용할 포트 번호
	private static Map<String, String> user = new HashMap<>(); // 아이디-비밀번호 저장
	private static String fileName = "src/cafeProgram/data.txt";//데이터 저장
	static {
		user.put("admin", "admin");//관리자 추가
	}

	public static void main(String[] args) {
		user = (Map<String, String>) (load(fileName));
	
		try (ServerSocket serverSocket = new ServerSocket(Port)) {
			System.out.println("[서버가 실행 중 입니다...]");

			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("[클라이언가 연결되었습니다.]");

				new Handler(socket).start();
			}
		} catch (IOException e) {
			System.out.println("[오류가 발생했습니다.]");
			save(fileName, user);
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
			 return new HashMap<>();
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
			Customer customer = (Customer) ois.readObject();
			String id = customer.getId();
			String pw = customer.getPw();

			boolean res = user.containsKey(id) && user.get(id).equals(pw);
			oos.writeBoolean(res);
			oos.flush();

			if (res) {
				boolean admin = id.equals("admin");// 관리자인지 여부 받기
				oos.writeBoolean(admin); 
			    oos.flush();
			    
				System.out.println("[" + id + "님이 로그인 하였습니다.]");

				if (admin) {
					adminMenu(); // 관리자 메뉴 실행
				} else {
					userMenu(); // 일반 사용자 메뉴 실행
				}
			} else {
				System.out.println("[아이디/비밀번호가 일치하지 않습니다.]");
			}
		}

		private void adminMenu() throws IOException {
			int menu;

			do {
				menu = ois.readInt(); // 클라이언트로부터 메뉴 선택 받기 - 오준호
				switch (menu) {
				case 1:
					System.out.println("메뉴 추가 (구현 필요)");
					break;
				case 2:
					System.out.println("메뉴 수정 (구현 필요)");
					break;
				case 3:
					System.out.println("매출확인 (구현 필요)");
					break;
				case 4:
					System.out.println("[로그아웃을 합니다.]");
					return;
				default:
					System.out.println("[잘못된 메뉴 선택입니다.]");
				}
			} while (menu != 4);
		}

		private void userMenu() throws IOException {
			int menu;
			
			do {
				menu = ois.readInt();
				switch (menu) {
				case 1:
					System.out.println("주문 기능 (구현 필요)");
					break;
				case 2:
					System.out.println("[로그아웃을 합니다.]");
					return;
				default:
					System.out.println("[잘못된 메뉴 선택입니다.]");
				}
			} while (menu != 2);
		}

		private void signUp() throws IOException, ClassNotFoundException {
			Customer customer = (Customer) ois.readObject();
			String id = customer.getId();
			String pw = customer.getPw();

			boolean res2 = !user.containsKey(id);
			if (res2) {
				user.put(id, pw);
				save(fileName, user);
			}

			oos.writeBoolean(res2);
			oos.flush();

			if (res2) {
				System.out.println(customer);
				System.out.println("[회원가입이 완료되었습니다.]");
			} else {
				System.out.println("[회원가입이 실패했습니다.]");
				System.out.println("[아이디 중복 혹은 비밀번호가 일치하지 않습니다.]");
			}
		}
	}
}