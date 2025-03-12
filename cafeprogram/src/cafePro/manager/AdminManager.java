package cafePro.manager;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Scanner;

import cafePro.model.Cafe;
import cafePro.model.Member;

public class AdminManager {
	private static Scanner scan = new Scanner(System.in);
	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;
	
	public AdminManager(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
	}

	public void insertCafeMenu() {
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

	public void editCafeMenu() {
		try {
			
			List<Cafe>list = (List)ois.readObject();
			
			if(list ==null || list.isEmpty()) {
				System.out.println("[등록된 메뉴가 없음]");
				return;
			}
			
			for(int i=0; i<list.size(); i++) {
				System.out.println(i+1 +". "+ list.get(i));
			}
			
			int index=0;
			do {
				System.out.print("수정할 메뉴 입력 : ");
				index = scan.nextInt()-1;
				if(index>=list.size()||index<0) {
					System.out.println("리스트에 있는 번호로 입력하시오");
				}							
			}while(index>=list.size()||index<0);
			
			Cafe tmp = inputMenuInfo();
			
			oos.writeInt(index);
			oos.writeObject(tmp);
			oos.flush();
			
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

	public void deleteCafeMenu() {
		try {
			
			List<Cafe>list = (List)ois.readObject();
			
			
			if(list ==null || list.isEmpty()) {
				System.out.println("[등록된 메뉴가 없음]");
				return;
			}
			
			for(int i=0; i<list.size(); i++) {
				System.out.println(i+1 +". "+ list.get(i));
			}
			
			int index=0;
			do {
				System.out.print("삭제할 메뉴 입력 : ");
				index = scan.nextInt()-1;
				if(index>=list.size()||index<0) {
					System.out.println("리스트에 있는 번호로 입력하시오");
				}							
			}while(index>=list.size()||index<0);
			
			oos.writeInt(index);
			oos.flush();
			 
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
	
	public void deleteMember() {
		try {
			
			List<Member> list = (List<Member>) ois.readObject();

			
			if (list == null || list.isEmpty()) {
				System.out.println("[등록된 회원이 없음]");
				return;
			}

			for (int i = 0; i < list.size(); i++) {
				System.out.println(i + 1 + ". " + list.get(i));
			}

			int index;
			do {
				System.out.print("[삭제할 회원번호(0: 취소): ");
				index = scan.nextInt() - 1;
				if (index == -1) {
					System.out.println("[삭제를 취소합니다.]");
					oos.writeInt(-1); // 서버로 -1을 전송하여 취소 의사 전달
					oos.flush();
					return;
				}
				if (index >= list.size() || index < 0) {
					System.out.println("[리스트에 있는 번호로 입력하세요.]");
				}
			} while (index >= list.size() || index < 0);

			oos.writeInt(index);
			oos.flush();

			boolean res = ois.readBoolean();
			if (res) {
				System.out.println("[회원 삭제 완료]");
			} else {
				System.out.println("[회원 삭제 실패(관리자 계정은 삭제할 수 없습니다)]");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void checkIncome() {
		try {
			int menu=0;
			do {
				printIncomeMenu();
				
				menu = scan.nextInt();
				scan.nextLine();
				
				// 서버에 매출 조회 요청
				oos.writeInt(menu); 
				oos.flush();
				
				if(menu == 6) {
					return;
				}
				int totalIncome = ois.readInt();
				
				if (menu == 5) {
					System.out.println("[총 매출: " + totalIncome + "원]");
				} else {
					System.out.println("[매출: " + totalIncome + "원]");
				}
			}while(menu != 6);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printIncomeMenu() {
		System.out.println("------------------");
		System.out.println("1. 일별 매출");
		System.out.println("2. 주별 매출");
		System.out.println("3. 월별 매출");
		System.out.println("4. 연별 매출");
		System.out.println("5. 총 매출");
		System.out.println("6. 뒤로 가기");
		System.out.println("------------------");
		System.out.print("메뉴 선택: ");
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

	
	
}
