package cafePro;

import java.util.Scanner;

public class MemberProgram{
	private static Scanner scan = new Scanner(System.in);

	public Member login() {
		System.out.print("아이디 : ");
        String id = scan.next();
        System.out.print("비밀번호 : ");
        String pw = scan.next();
        
        
		return new Member(null, null);
	}
	public void register() {
		// TODO Auto-generated method stub
		
	}

	

}
