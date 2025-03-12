package cafePro.manager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import cafePro.model.Member;
import cafePro.program.AdminProgram;
import cafePro.program.UserProgram;

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
		try {
			oos.writeObject(login);
			oos.flush();
			
			//로그인 결과
			boolean res = ois.readBoolean();
			
			if(res) {
				boolean type = ois.readBoolean();
				if (type) {
					//관리자
					AdminProgram adminProgram = new AdminProgram(oos, ois);
					adminProgram.run();
				}else {
					//고객
					UserProgram userProgram = new UserProgram(oos, ois);
					userProgram.run();
				}
			}else {
				System.out.println("[아이디 또는 비밀번호가 틀렸습니다.]");
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void register()  {
		try {
			Member signUp = inputIdPw();
			
			System.out.print("비밀번호 확인 : ");
			String checkPw = scan.nextLine();
			
			if(!signUp.getPw().equals(checkPw)) {
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Member inputIdPw() {
		System.out.print("아이디 : ");
        String id = scan.next();
        System.out.print("비밀번호 : ");
        String pw = scan.next();
        scan.nextLine();
		return new Member(id, pw);
	}
	

}
