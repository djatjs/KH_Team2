package cafePro_DB.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import cafePro_DB.model.vo.Member;

public class MemberManager {
	private static Scanner scan = new Scanner(System.in);
	private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
	public MemberManager(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
	}
	public void login() {
		System.out.println("[클라이언트 : 로그인]");
		
	}
	public void register() {
		try {
			Member signUp = inputIdPw();
			
			System.out.print("비밀번호 확인 : ");
			String checkPw = scan.nextLine();
			
			if(!signUp.getMPw().equals(checkPw)) {
				System.out.println("[비밀번호가 일치하지 않습니다.]");
				oos.writeBoolean(false);
				oos.flush();
				return;
			}
			oos.writeBoolean(true);
			oos.writeObject(signUp);
			oos.flush();
			
			boolean res = ois.readBoolean(); // 서버로부터 결과 받기
			if (res) {
				System.out.println("[회원가입이 완료되었습니다.]");
			} else {
				System.out.println("[이미 존재하는 아이디입니다.]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void findPw() {
		
		
	}
	
	private Member inputIdPw() {
		System.out.print("아이디 : ");
        String id = scan.next();
        System.out.print("비밀번호 : ");
        String pw = scan.next();
        System.out.print("전화번호 : ");
        String number = scan.next();
        System.out.print("닉네임 : ");
        String nickname = scan.next();
        scan.nextLine();
		return new Member(id, pw, number, nickname);
	}
}
