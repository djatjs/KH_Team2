package cafePro.manager;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Scanner;

import cafePro.model.Cafe;
import cafePro.model.Member;

public class UserManager {
	private static Scanner scan = new Scanner(System.in);
	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;
	
	public UserManager(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
	}

	public void order() {
		try {
			//서버로부터 카페 메뉴 리스트 받아옴
			List<Cafe> list = (List)ois.readObject();
			
			//리스트가 null상태이거나 담긴 메뉴가 없으면 없다하고 끝
			if(list ==null || list.isEmpty()) {
				System.out.println("[등록된 메뉴가 없음]");
				return;
			}
			
			//리스트 출력
			for(int i=0; i<list.size(); i++) {
				System.out.println(i+1 +". "+ list.get(i));
			}
			
			//주문할 메뉴 입력 및 서버로 전송
			int index;
			System.out.print("주문할 메뉴 입력 : ");
			index = scan.nextInt()-1;
			scan.nextLine();
			oos.writeInt(index);
			oos.flush();
			
			buyDrink();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private static void buyDrink() {
		try {
			//주문하는 메뉴의 객체도 받음
			Cafe menu = (Cafe) ois.readObject();
			
			//사용자 객체 받고 쿠폰 갯수 확인해서 있으면 사용할건지 물어봄
			Member user = (Member) ois.readObject();
			System.out.println("------------------");
			System.out.println(menu+" /구매중");
			
			if(user.getCoupon()>0) {
				
				System.out.println("쿠폰을 사용하시겠습니까?");
				
				System.out.print("입력(O/X) : ");
				String isUse =scan.nextLine();
				oos.writeUTF(isUse);
				oos.flush();
			}
			
			boolean res = ois.readBoolean();
			if(res) {
				System.out.println(menu.getMenu()+" : 주문 완료");
			}else {
				System.out.println("주문 실패");
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
