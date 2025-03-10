package cafePro;

import java.util.Scanner;

public class Kiosk implements ConsoleProgram{
	private Scanner scan = new Scanner(System.in);
	private static MemberProgram memberProgram = new MemberProgram();
	
	
	@Override
	public void run() {
		//처음 메뉴
		int menu=0;
		do {
			printMenu();
			menu=scan.nextInt();
			scan.nextLine();
			runMenu(menu);			
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


	public void runMenu(int menu) {
		/*customer 프로그램(클래스), 매니저(실질적인 기능 구현할 파일)만들어서
		 * 로그인, 회원가입 가능하게 하면 좋을듯함*/
		
		switch (menu) {
        case 1: 
        	Member member = memberProgram.login();
        	
            break;
        case 2:
        	memberProgram.register();
            break;
        case 3:
            System.out.println("[프로그램을 종료합니다.]");
            break;
        default:
            System.out.println("[잘못된 메뉴 선택입니다.]");
		
		}
	}



}