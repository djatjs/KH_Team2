package cafePro.program;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import cafePro.manager.MemberManager;

public class MemberProgram{
	private static Scanner scan = new Scanner(System.in);
	private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private MemberManager memberManager;
	
	public MemberProgram(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
		this.memberManager = new MemberManager(oos, ois);
	}
	
	public void run() {   
		int menu=0;
		do {
			try {
				printMenu();
				menu=scan.nextInt();
				scan.nextLine();
				
				oos.writeInt(menu);
				oos.flush();

				runMenu(menu);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}while(menu!=3);
		
	}
	public void printMenu() {
		System.out.println("------------------");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("3. 종료");
        System.out.println("------------------");
        System.out.print("메뉴 선택 : ");
	}
	
	public void runMenu(int menu) throws Exception {
		switch (menu) {
        case 1: 
        	memberManager.login();
            break;
        case 2:
        	memberManager.register();
            break;
        case 3:
            System.out.println("[프로그램을 종료합니다.]");
            break;
        default:
            System.out.println("[잘못된 메뉴 선택입니다.]");
		}
	}
}
