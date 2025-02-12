package cafePro.manager;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import cafePro.model.Member;

public class MemberManager {
	private Scanner scan = new Scanner(System.in);
	private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    
	public MemberManager(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
        this.ois = ois;
	}

	public void login() {
		Member login = inputIdPw();
		
	}
	
	public void register() {
		// TODO Auto-generated method stub
		
	}

	private Member inputIdPw() {
		System.out.print("아이디 : ");
        String id = scan.next();
        System.out.print("비밀번호 : ");
        String pw = scan.next();
		return new Member(id, pw);
	}
	

}
