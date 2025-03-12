package main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import service.CustomerManager;
import service.MenuManager;

public class CustomerProgram {
	private Scanner scan = new Scanner(System.in);
	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;


	
	public CustomerProgram(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
	}
	
	public void runCustomer() {
		try {
			int menu=0;
			do {
				printCustomerMenu();
				menu = scan.nextInt();
				scan.nextLine();
				oos.writeInt(menu);
				oos.flush();
				
				// 회원탈퇴를 위한 추가 코드
				if(menu == 4) {
					CustomerManager memberManager = new CustomerManager(oos,ois);
					boolean res = memberManager.withdrawMembership();
					if(res) {
						menu = 5;
					}
				}
				runCustomerMenu(menu); 
			}while(menu!=5);
		} catch (Exception e) {
			e.printStackTrace();
		} 	
	}
	private void printCustomerMenu() {
		System.out.println("------------------");
		System.out.println("1. 메뉴 조회");
		System.out.println("2. 주문 내역 조회");
		System.out.println("3. 회원 정보 수정");
		System.out.println("4. 회원 탈퇴");
		System.out.println("5. 로그아웃");
		System.out.println("------------------");
		System.out.print("메뉴 선택: ");
	}
	private void runCustomerMenu(int menu) {
		CustomerManager memberManager = new CustomerManager(oos,ois);
		MenuManager menuManager = new MenuManager(oos,ois);
		switch (menu) {
		case 1:
			menuManager.viewMenuList();
			break;
		case 2:
			menuManager.viewHistory();
			break;
		case 3:
			memberManager.updateInfo();
			break;
		case 4:
			memberManager.withdrawMembership();
			break;
		case 5:
			System.out.println("[로그아웃 합니다]");
			break;
		default:
			System.out.println("[잘못된 메뉴 선택입니다.]");
		}
		
	}
}
