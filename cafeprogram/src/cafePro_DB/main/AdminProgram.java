package cafePro_DB.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import cafePro_DB.model.vo.Category;
import cafePro_DB.service.AdminManager;

public class AdminProgram {
	private static Scanner scan = new Scanner(System.in);
	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;

	public AdminProgram(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
	}

	public void run() {
		try {
			int menu = 0;
			do {
				printMenu();
				menu = scan.nextInt();
				scan.nextLine();
				oos.writeInt(menu);
				oos.flush();
				runMenu(menu);
			} while (menu != 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printMenu() {
		System.out.println("------------------");
		System.out.println("1. 메뉴");
		System.out.println("2. 태그");
		System.out.println("3. 카테고리");
		System.out.println("4. 매출 확인");
		System.out.println("5. 로그아웃");
		System.out.println("------------------");
		System.out.print("메뉴 선택: ");
	}

	private void runMenu(int menu) {
		switch (menu) {
		case 1:
			System.out.println("메뉴 관리");
			break;
		case 2:
			System.out.println("태그 관리");
			break;
		case 3:
			category();
			break;
		case 4:
			System.out.println("매출 확인");
			break;
		case 5:
			System.out.println("[로그아웃을 합니다.]");
			break;
		default:
			System.out.println("[잘못된 메뉴 선택입니다.]");
		}
	}

	private void category() {
		try {
			int menu = 0;
			do {
				printCategoryMenu();
				menu = scan.nextInt();
				scan.nextLine();
				oos.writeInt(menu);
				oos.flush();
				categoryMenu(menu);
			} while (menu != 4); // 뒤로 가기 메뉴 추가
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printCategoryMenu() {
		System.out.println("------------------");
		System.out.println("1. 카테고리 추가");
		System.out.println("2. 카테고리 수정");
		System.out.println("3. 카테고리 삭제");
		System.out.println("4. 뒤로 가기");
		System.out.println("------------------");
		System.out.print("메뉴 선택: ");
	}

	private void categoryMenu(int menu) {
		AdminManager adminManager = new AdminManager(oos, ois);
		System.out.println("메뉴" + menu);

		switch (menu) {
		case 1:
			adminManager.insertCategory(); // 카테고리 추가
			break;
		case 2:
			adminManager.editCategory(); // 카테고리 수정
			break;
		case 3:
			adminManager.deleteCategory(); // 카테고리 삭제
			break;
		case 4:
			// 뒤로 가기
			break;
		default:
			System.out.println("[잘못된 메뉴 선택입니다.]");
			break;
		}
	}
}