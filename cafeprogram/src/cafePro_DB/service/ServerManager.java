package cafePro_DB.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cafePro_DB.dao.CategoryDAO;
import cafePro_DB.dao.MemberDAO;
import cafePro_DB.model.vo.Category;
import cafePro_DB.model.vo.Member;

public class ServerManager {
	private MemberDAO memberDao;
	private CategoryDAO categoryDao;
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
			categoryDao = session.getMapper(CategoryDAO.class);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void login() {
		System.out.println("[서버 : 로그인]");
		try {
			// 로그인 정보 받음
			Member login = (Member) ois.readObject();
			// 로그인 확인
			boolean loginRes = true;
			Member dbMember = memberDao.selectMember(login);
			if (dbMember == null) {
				loginRes = false;
				oos.writeBoolean(loginRes);
				oos.flush();
				return;
			}

			if ("ADMIN".equals(dbMember.getMId())) {
				memberDao.updateAuthority(dbMember.getMId());
				dbMember = memberDao.selectMember(login);
			}

			String type = dbMember.getMAuthority();
			oos.writeBoolean(loginRes);
			oos.writeUTF(type);
			oos.flush();

			switch (type) {
			case "ADMIN":
				runAdminService(dbMember);
				break;
			case "CUSTOMER":
				runCustomerService(dbMember);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void runAdminService(Member dbMember) {
		int menu = 0;

		try {
			do {
				menu = ois.readInt(); // 클라이언트로부터 메뉴 선택 받기
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
			} while (menu != 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void category() {
		int menu = 0;

		try {
			do {
				menu = ois.readInt();
				switch (menu) {
				case 1:
					insertCategory();
					break;
				case 2:
					editCategory();
					break;
				case 3:
					deleteCategory();
					break;
				case 4:
					break;
				default:
					System.out.println("[잘못된 메뉴 선택입니다.]");
				}
			} while (menu != 4);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void insertCategory() {
		try {
			// 클라이언트로부터 카테고리 객체 읽어오기
			Category category = (Category) ois.readObject();
			System.out.println("[서버: 카테고리 추가 요청] " + category);

			// 중복 확인
			boolean categoryExists = categoryDao.checkCategory(category);
			if (categoryExists) {
				System.out.println("[서버: 이미 존재하는 카테고리]");
				oos.writeBoolean(false); // 중복이면 false 반환
				oos.flush();
				return;
			}

			// 카테고리 추가
			categoryDao.insertCategory(category);
			System.out.println("[서버: 카테고리 등록 성공]");

			// 성공 여부 클라이언트에게 전송
			oos.writeBoolean(true);
			oos.flush();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				oos.writeBoolean(false); // 실패 시 false 반환
				oos.flush();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	private void editCategory() {
		List<Category> list = categoryDao.seletAllCategory();
		try {
			oos.writeObject(list);
			oos.flush();
			
			int can = ois.readInt();
			String cam = ois.readUTF();
			String cac = ois.readUTF();
			
			Category dbCategory = categoryDao.seletCategory(can);
			
			boolean res = true;
			
			if(dbCategory == null) {
				res = false;
				oos.writeBoolean(res);
				oos.flush();
				return;
			} 
			
			res= categoryDao.updateCategory(cam, cac, can);
			oos.writeBoolean(res);
			oos.flush();
					
			
			
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	private void deleteCategory() {
		System.out.println("삭제");
	}

	private void runCustomerService(Member dbMember) {
		System.out.println("고객");
		System.out.println(dbMember);

	}

	public void register() {
		try {
			// 클라이언트에서 정보 잘못입력하면 false 전송후 리턴시키는 것에 맞춰가기
			boolean inputRes = ois.readBoolean();
			if (!inputRes) {
				return;
			}
			// 회원정보 정보 받음
			Member member = (Member) ois.readObject();
			// 등록된 아이디인지 확인
			boolean res = true;
			if (contains(member)) {
				res = false;
			}
			// 등록된 아이디가 아니라면 회원가입 처리
			if (res) {
				memberDao.insertMember(member);
				// stampDao.insertStamp(member);
			}
			// 결과 반환
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
		// DB에서 member를 이용하여 사용자 정보를 가져옴
		Member dbMember = memberDao.selectMember(member);

		// DB에서 가져온 학생 정보가 있으면 중복
		if (dbMember != null) {
			return true;
		}
		return false;
	}
}
