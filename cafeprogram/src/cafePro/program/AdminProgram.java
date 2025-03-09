package cafePro.program;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import cafePro.manager.AdminManager;

public class AdminProgram {
	private Scanner scan = new Scanner(System.in);
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public AdminProgram(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
	}

	public void run() {
		try {
			int menu=0;
			do {
				printMenu(); 
				menu = scan.nextInt(); 
				scan.nextLine(); 
				oos.writeInt(menu);
				oos.flush();
				runMenu(menu);
			} while (menu != 6);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printMenu() {
		System.out.println("------------------");
	    System.out.println("1. 메뉴 추가");
	    System.out.println("2. 메뉴 수정");
	    System.out.println("3. 메뉴 삭제");
	    System.out.println("4. 회원 관리");
	    System.out.println("5. 매출 확인");
	    System.out.println("6. 로그아웃");
	    System.out.println("------------------");
	    System.out.print("메뉴 선택: ");
	}

	private void runMenu(int menu) {
		AdminManager adminManager = new AdminManager(oos, ois);
		switch (menu) {
		case 1:
			adminManager.insertCafeMenu();
			break;
		case 2:
			adminManager.editCafeMenu();
			break;
		case 3:
			adminManager.deleteCafeMenu();
			break;
		case 4:
			adminManager.deleteMember();
			break;
		case 5:
			adminManager.checkIncome();
			break;
		case 6:
			System.out.println("[로그아웃을 합니다.]");
			break;
		default:
			System.out.println("[잘못된 메뉴 선택입니다.]");
		}
		
	}
	

}
