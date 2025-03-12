package cafePro.program;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import cafePro.manager.UserManager;

public class UserProgram {
	private Scanner scan = new Scanner(System.in);
	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;
	
	public UserProgram(ObjectOutputStream oos, ObjectInputStream ois) {
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
			}while(menu!=2);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	private static void printMenu() {
		System.out.println("------------------");
	    System.out.println("1. 주문하기");
	    System.out.println("2. 로그아웃");
	    System.out.println("------------------");
	    System.out.print("메뉴 선택: ");
	}
	private static void runMenu(int menu) {
		UserManager userManager = new UserManager(oos,ois);
		switch (menu) {
		case 1:
			userManager.order();
			break;
		case 2:
			System.out.println("[로그아웃을 합니다.]");
			break;
		default:
			System.out.println("[잘못된 메뉴 선택입니다.]");
		}
	}

}
