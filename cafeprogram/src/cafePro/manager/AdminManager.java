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

	public void deleteCafeMenu() {
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
	
	public void deleteMember() {
		try {
			// 서버로부터 회원 메뉴 리스트 받아옴
			List<Member> list = (List<Member>) ois.readObject();

			// 리스트가 null상태이거나 담긴 메뉴가 없으면 없다고 하고 끝
			if (list == null || list.isEmpty()) {
				System.out.println("[등록된 회원이 없음]");
				return;
			}

			// 리스트 출력
			for (int i = 0; i < list.size(); i++) {
				System.out.println(i + 1 + ". " + list.get(i));
			}

			// 삭제할 회원의 번호 입력
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

			// 서버로 삭제될 회원 번호 전송
			oos.writeInt(index);
			oos.flush();

			// 서버로부터 삭제 결과 받음
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
		System.out.println("매출확인");
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
