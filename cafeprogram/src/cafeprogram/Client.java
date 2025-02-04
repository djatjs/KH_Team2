package cafeprogram;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;



public class Client {

    private static Scanner scan = new Scanner(System.in);
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    public static void main(String[] args) {
        String ip = "127.0.0.1"; // 서버 IP
        int port = 9999; // 서버 포트

        try (Socket socket = new Socket(ip, port)) {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("서버에 연결되었습니다.");

            int menu;
            do {
                printMainMenu();
                menu = scan.nextInt();
                scan.nextLine(); 

                oos.writeInt(menu);
                oos.flush();

                switch (menu) {
                    case 1: // 로그인
                        login();
                        break;
                    case 2: // 회원가입
                        signUp();
                        break;
                    case 3: // 종료
                        System.out.println("[프로그램을 종료합니다.]");
                        break;
                    default:
                        System.out.println("[잘못된 메뉴 선택입니다.]");
                }
            } while (menu != 3);
        } catch (IOException e) {
            System.out.println("[서버와 연결이 끊어졌습니다.]");
        }
    }

    private static void printMainMenu() {
        System.out.println("------------------");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("3. 종료");
        System.out.println("------------------");
        System.out.print("메뉴 선택 : ");
    }

    private static void login() throws IOException {
        System.out.print("아이디 : ");
        String id = scan.next();
        System.out.print("비밀번호 : ");
        String pw = scan.next();

        Customer customer2 = new Customer(id, pw);
        
        oos.writeObject(customer2); // 서버에 회원 정보 전송
        oos.flush();

        boolean res = ois.readBoolean(); // 서버로부터 결과 받기
        
        if (res) {
            boolean admin = ois.readBoolean(); // 관리자인지 여부 받기

            if (admin) {
                adminMenu(); // 관리자 메뉴 실행
            } else {
                userMenu(0); // 일반 사용자 메뉴 실행
            }
        } else {
            System.out.println("[아이디 또는 비밀번호가 틀렸습니다.]");
        }
    }

    private static void adminMenu() throws IOException{
    	int menu;
        do {
            adminPrintMainMenu(); 
            menu = scan.nextInt(); 
            scan.nextLine(); 

            oos.writeInt(menu);
            oos.flush();

            adminRunMainMenu(menu);
        } while (menu != 5);
    }
	       
	private static void adminPrintMainMenu() {
			
		System.out.println("------------------");
	    System.out.println("1. 메뉴 추가");
	    System.out.println("2. 메뉴 수정");
	    System.out.println("3. 메뉴 삭제");
	    System.out.println("4. 매출 확인");
	    System.out.println("5. 로그아웃");
	    System.out.println("------------------");
	    System.out.print("메뉴 선택: ");
	}

	private static void adminRunMainMenu(int menu) {
		
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
			checkIncome();
			return;
		case 5:
			System.out.println("[로그아웃을 합니다.]");
			return;
		default:
			System.out.println("[잘못된 메뉴 선택입니다.]");
		}
	}

	private static void insertCafeMenu() {
		Cafe menu = inputMenuInfo();
		try {
			oos.writeObject(menu);
			oos.flush();
			boolean res = ois.readBoolean();
			if(res) {
				System.out.println("[메뉴 등록 완료]");
			}else {
				System.out.println("[메뉴 등록 실패]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static Cafe inputMenuInfo() {
		System.out.println("------------------");
		System.out.print("메뉴 이름 : ");
		String name = scan.next();
		System.out.print("메뉴 가격 : ");
		int price = scan.nextInt();
		scan.nextLine();
		System.out.println("------------------");
		return new Cafe(name, price);
	}

	private static void editCafeMenu() {
		//서버로부터 카페 메뉴 리스트 받아옴
		try {
			List<Cafe>list = (List)ois.readObject();
			//리스트가 null상태이거나 담긴 메뉴가 없으면 없다하고 끝
			if(list ==null || list.isEmpty()) {
				System.out.println("[등록된 메뉴가 없음]");
				return;
			}
			//리스트 출력
			for(int i=0; i<list.size(); i++) {
				System.out.println(i+1 +". "+ list.get(i));
			}
			//수정할 메뉴의 번호 입력
			int index=0;
			do {
				System.out.print("수정할 메뉴 입력 : ");
				index = scan.nextInt()-1;
				if(index>=list.size()||index<0) {
					System.out.println("리스트에 있는 번호로 입력하시오");
				}							
			}while(index>=list.size()||index<0);
			//수정할 메뉴 정보 입력
			Cafe tmp = inputMenuInfo();
			//번호와 수정할 메뉴 정보 서버로 전송
			oos.writeInt(index);
			oos.writeObject(tmp);
			oos.flush();
			//결과 받아옴
			boolean res = ois.readBoolean();
			if(res) {
				System.out.println("[메뉴 수정 완료]");
			}else {
				System.out.println("[메뉴 수정 실패]");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void deleteCafeMenu() {
		try {
			//서버로부터 카페 메뉴 리스트 받아옴
			List<Cafe>list = (List)ois.readObject();
			
			//리스트가 null상태이거나 담긴 메뉴가 없으면 없다하고 끝
			if(list ==null || list.isEmpty()) {
				System.out.println("[등록된 메뉴가 없음]");
				return;
			}
			
			//리스트 출력
			for(int i=0; i<list.size(); i++) {
				System.out.println(i+1 +". "+ list.get(i));
			}
			//삭제할 메뉴의 번호 입력
			int index=0;
			do {
				System.out.print("삭제할 메뉴 입력 : ");
				index = scan.nextInt()-1;
				if(index>=list.size()||index<0) {
					System.out.println("리스트에 있는 번호로 입력하시오");
				}							
			}while(index>=list.size()||index<0);
			
			//서버로 삭제될 메뉴 번호 전송
			oos.writeInt(index);
			oos.flush();
			
			//서버로부터 삭제 결과 받음 
			boolean res = ois.readBoolean();
			if(res) {
				System.out.println("[메뉴 삭제 완료]");
			}else {
				System.out.println("[메뉴 삭제 실패]");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void checkIncome() {
		
	}

	private static void userMenu(int menu) throws IOException {
		  do {
		        userPrintMainMenu();
		        menu = scan.nextInt(); 
		        scan.nextLine();
	
		        oos.writeInt(menu); 
		        oos.flush();
	
		        userRunMainMenu(menu); 
		    } while (menu != 2);
		}

	private static void userPrintMainMenu() {
		System.out.println("------------------");
	    System.out.println("1. 주문하기");
	    System.out.println("2. 로그아웃");
	    System.out.println("------------------");
	    System.out.print("메뉴 선택: ");
		
	}

	private static void userRunMainMenu(int menu) {
			
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
	}

	private static void signUp() throws IOException {
        System.out.print("아이디 : ");
        String id = scan.nextLine();
        System.out.print("비밀번호 : ");
        String pw = scan.nextLine();
        System.out.print("비밀번호 확인 : ");
        String pw2 = scan.nextLine();

        if (!pw.equals(pw2)) {
            System.out.println("[비밀번호가 일치하지 않습니다.]");
            return;
        }

        Customer customer = new Customer(id, pw);
        oos.writeObject(customer); // 서버에 회원 정보 전송
        oos.flush();

        boolean res = ois.readBoolean(); // 서버로부터 결과 받기
        if (res) {
        	System.out.println("[회원가입이 완료되었습니다.]");
        } else {
            System.out.println("[이미 존재하는 아이디입니다.]");
        }
    }
}
