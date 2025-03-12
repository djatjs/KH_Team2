package main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import service.MenuManager;


public class MenuProgram {
	private Scanner scan = new Scanner(System.in);
	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;
	
	public MenuProgram(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
	}

	public void runAdmin() {
		try {
			int menu=0;
			do {
				printMenu();
				menu = scan.nextInt(); 
				scan.nextLine();
				oos.writeInt(menu);
				oos.flush();
				
				runAdminMenu(menu); 
			}while(menu!=5);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	private static void printMenu() {
		System.out.println("------------------");
	    System.out.println("1. 카테고리");
	    System.out.println("2. 태그");
	    System.out.println("3. 메뉴");
	    System.out.println("4. 매출 확인");
	    System.out.println("5. 로그아웃");
	    System.out.println("------------------");
	    System.out.print("메뉴 선택: ");
	}
	private static void runAdminMenu(int menu) {
		MenuManager menuManager = new MenuManager(oos,ois);
		switch (menu) {
		case 1:
			menuManager.category();
			break;
		case 2:
			menuManager.tag();
			break;
		case 3:
			menuManager.menu();
			break;
		case 4:
			menuManager.income();
			break;
		case 5:
			System.out.println("[로그아웃 합니다]");
			break;
		default:
			System.out.println("[잘못된 메뉴 선택입니다.]");
		}
	}

	public void runCustomer() {
		// TODO Auto-generated method stub
		
	}
}
