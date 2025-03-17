package service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import main.CustomerProgram;
import main.AdminProgram;
import model.vo.Member;

public class CustomerManager {
	private static Scanner scan = new Scanner(System.in);
	private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
	public CustomerManager(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
	}
	public String login() {
		String type="실패";
		try {
			Member login = inputToLogin();
			oos.writeObject(login);
			oos.flush();
			
			//아이디가 탈퇴신청한 상태인지 확인
			boolean is_Del = ois.readBoolean();
			if(is_Del) {
				System.out.println("탈퇴신청된 아이디입니다. 탈퇴취소를 해주세요");
				return type;
			}
			
			//로그인 결과
			boolean loginRes = ois.readBoolean();
			if(!loginRes) {
				return type;
			}
			 type = ois.readUTF();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return type;
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
		try {
	        // 아이디와 전화번호 입력 받기
	        System.out.print("아이디 : ");
	        String id = scan.next();
	        System.out.print("전화번호 : ");
	        String number = scan.next();
	        scan.nextLine();

	        // Member 객체 생성
	        Member member = new Member(id, null, number, null);

	        // 서버로 객체 전송
	        oos.writeObject(member);
	        oos.flush();
	        // 서버로부터 비밀번호 응답 받기
	        String pw = ois.readUTF();
	        // 응답 출력
	        if (pw != null) {
	            System.out.println("[비밀번호는: " + pw + " 입니다.]");
	        } else {
	            System.out.println("[아이디나 전화번호가 일치하지 않습니다.]");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}
	private Member inputToLogin() {
		System.out.print("아이디 : ");
        String id = scan.next();
        System.out.print("비밀번호 : ");
        String pw = scan.next();
        scan.nextLine();
        return new Member(id, pw);
	}
	
	
	private Member inputIdPw() {
		System.out.print("아이디 : ");
        String id = scan.next();
        System.out.print("닉네임 : ");
        String nickname = scan.next();
        System.out.print("전화번호 : ");
        String number = scan.next();
        System.out.print("비밀번호 : ");
        String pw = scan.next();
        scan.nextLine();
		return new Member(id, pw, number, nickname);
	}
	
	
	public boolean withdrawMembership() {
		boolean res=false;
		try {
			// 여유생기면 비밀번호 입력해서 본인 맞는지 재인증 단계 거쳐보기
			System.out.print("아이디 : ");
	        String id = scan.next();
	        System.out.print("비밀번호 : ");
	        String pw = scan.next();
	        scan.nextLine();
			// 탈퇴할지 말지 여부를 다시 한 번 물어봄
			String answer ="";
			while(true) {
				System.out.print("정말 탈퇴하시겠습니까? (Y or N)");
				answer = scan.next();
				scan.nextLine();
				if(answer.equals("Y") || answer.equals("N")) {break;}
				else {System.out.println("잘못된 입력");}
			}
			oos.writeUTF(answer);
			oos.flush();
			if(answer.equals("N")) {
				return false;
			}
			// 탈퇴한다는 신호를 서버로 보냄
			oos.writeUTF(id);
			oos.writeUTF(pw);
			oos.flush();
			// 서버의 db작업 성공 유무 받음
			res = ois.readBoolean();
			
			
			if(res) {
				System.out.println("[삭제 성공! 로그인 화면으로 돌아갑니다.]");
			}else {
				System.out.println("[삭제 실패 : 잘못된 아이디 또는 비밀번호 입력]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public void updateInfo() {
		try {
			System.out.print("아이디 : ");
			String id = scan.next();
			System.out.print("비밀번호 : ");
			String pw = scan.next();
			scan.nextLine();
			
			oos.writeUTF(id);
			oos.writeUTF(pw);
			oos.flush();

			boolean upRes = ois.readBoolean();
			
			if(!upRes) {
				System.out.println("[아이디/비밀번호가 틀렸습니다.]");
				return;
			}
			System.out.println("------------------");
			System.out.print("새로운 닉네임 : ");
			String newNic = scan.nextLine();
			System.out.print("새로운 전화번호 : ");
			String newNum = scan.nextLine();
			System.out.print("새로운 비밀번호 : ");
            String newPw = scan.nextLine();
            System.out.print("비밀번호 확인 : ");
			String checkPw = scan.nextLine();
			System.out.println("------------------");

			if(!newPw.equals(checkPw)) {
				System.out.println("[비밀번호가 일치하지 않습니다.]");
				return;
			}		
			oos.writeUTF(newNic);	//서버에 보내기
	        oos.writeUTF(newNum);
	        oos.writeUTF(newPw);
	        oos.flush();
	        
	        boolean upRes2 = ois.readBoolean();
	        if (upRes2) {
                System.out.println("회원 정보가 성공적으로 수정되었습니다.");
            } else {
                System.out.println("회원 정보 수정에 실패했습니다.");
            }
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}
}