package cafePro_DB.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cafePro_DB.dao.MemberDAO;
import cafePro_DB.dao.StampDAO;
import cafePro_DB.model.vo.Member;

public class ServerManager {
	private MemberDAO memberDao;
	private StampDAO stampDao;
	private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
	public ServerManager(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
		
		String resource = "cafePro_DB/config/mybatis-config.xml";
		InputStream inputStream;
		SqlSession session;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			session = sessionFactory.openSession(true);
			memberDao = session.getMapper(MemberDAO.class);
			stampDao = session.getMapper(StampDAO.class);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void login() {
		System.out.println("[서버 : 로그인]");
		try {
			//로그인 정보 받음
			Member login = (Member) ois.readObject();
			//로그인 확인
			boolean loginRes = true;
			Member dbMember = memberDao.selectMember(login);
			if(dbMember == null) {
				loginRes = false;
				oos.writeBoolean(loginRes);
				oos.flush();
				return;
			}
			String type = dbMember.getMAuthority();
			oos.writeBoolean(loginRes);
			oos.writeUTF(type);
			oos.flush();
			
			switch(type) {
			case "ADMIN":
				runAdminService();
				break;
			case "CUSTOMER":
				runCustomerService(dbMember);
				break;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void runAdminService() {
		System.out.println("관리자");
		
	}
	private void runCustomerService(Member dbMember) {
		System.out.println("고객");
		System.out.println(dbMember);
		
	}
	public void register() {
		try {
			//클라이언트에서 정보 잘못입력하면 false 전송후 리턴시키는 것에 맞춰가기
			boolean inputRes = ois.readBoolean();
			if(!inputRes) {
				return;
			}
			//회원정보 정보 받음
			Member member = (Member) ois.readObject();
			//등록된 아이디인지 확인
			boolean res = true;
			if(contains(member)) {
				res = false;
			}
			//등록된 아이디가 아니라면 회원가입 처리
			if(res) {
				memberDao.insertMember(member);
				//stampDao.insertStamp(member);
			}
			//결과 반환
			oos.writeBoolean(res);
			oos.flush();

			if (res) {
				System.out.println(member);
				System.out.println("[서버 : 회원가입이 완료되었습니다.]");
			} else {
				System.out.println("[서버 : 회원가입이 실패했습니다.]");
				System.out.println("[아이디 중복 혹은 비밀번호가 일치하지 않습니다.]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void findPw() {
		try {
			// 클라이언트로부터 Member 객체를 받음
			Member member = (Member) ois.readObject();
			// 비밀번호 조회
			String pw = memberDao.findPw(member).getMPw();
			// 클라이언트로 결과 반환
			oos.writeUTF(pw);
			oos.flush();
			if (pw != null) {
				System.out.println("[서버 : 비밀번호 조회 완료]");
			} else {
				System.out.println("[서버 : 아이디나 전화번호가 일치하지 않습니다.]");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	
	
	public boolean contains(Member member) {
		//DB에서 member를 이용하여 사용자 정보를 가져옴 
		Member dbMember = memberDao.selectMember(member);
		
		//DB에서 가져온 학생 정보가 있으면 중복 
		if(dbMember != null) {
			return true;
		}
		return false;
	}
}
