package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dao.CategoryDAO;
import dao.CouponDAO;
import dao.IncomeDAO;
import dao.MemberDAO;
import dao.MenuDAO;
import dao.StampDAO;
import dao.TagDAO;
import model.vo.Category;
import model.vo.Member;
import model.vo.Menu;
import model.vo.Tag;

public class ServerManager {
	private MemberDAO memberDao;
	private StampDAO stampDao;
	private CouponDAO couponDao;
	private TagDAO tagDao;
	private CategoryDAO categoryDao;
	private IncomeDAO incomeDao;
	private MenuDAO menuDao;
	
	
	private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
	public ServerManager(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
		
		String resource = "config/mybatis-config.xml";
		InputStream inputStream;
		SqlSession session;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			session = sessionFactory.openSession(true);
			memberDao = session.getMapper(MemberDAO.class);
			stampDao = session.getMapper(StampDAO.class);
			couponDao = session.getMapper(CouponDAO.class);
			tagDao = session.getMapper(TagDAO.class);
			categoryDao = session.getMapper(CategoryDAO.class);
			menuDao = session.getMapper(MenuDAO.class);
			incomeDao = session.getMapper(IncomeDAO.class);

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
			String type = dbMember.getMAuthority();
			oos.writeBoolean(loginRes);
			oos.writeUTF(type);
			oos.flush();

			switch (type) {
			case "ADMIN":
				runAdminService();
				break;
			case "CUSTOMER":
				runCustomerService(dbMember);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 관리자 로그인
	private void runAdminService() {
		try {
			int menu = 0;
			do {
				menu = ois.readInt();
				runAdminMenu(menu);
			} while (menu != 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void runAdminMenu(int menu) {
		switch (menu) {
		case 1:
			try {
				int num = 0;
				do {
					num = ois.readInt();
					runCategoryMenu(num);
				} while (num != 4);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				int num = 0;
				do {
					num = ois.readInt();
					runTagMenu(num);
				} while (num != 4);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 3:
			try {
				int num = 0;
				do {
					num = ois.readInt();
					runMenuMenu(num);
				}while(num != 4);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 4:
			try {
				int num = 0;
				do {
					num = ois.readInt();
					runIncomeMenu(num);
				}while(num != 5);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 5:
			System.out.println("[로그아웃]");
			break;
		default:
			System.out.println("[잘못된 입력]");
		}
		
	}
	private void runMenuMenu(int num) {
		switch(num) {
		case 1:
			insertMenu();
			break;
		case 2:
			updateMenu();
			break;
		case 3:
			deleteMenu();
			break;
		case 4:
			break;
		default:
		}
		
	}
	private void insertMenu() {
		try {
			//카테고리 리스트를 보냄
			List<Category> list = categoryDao.seletAllCategory();
			oos.writeObject(list);
			//받아옴
			int caNum = ois.readInt();
			Menu menu = (Menu) ois.readObject();
			//중복 있는지 확인 : null이여야 등록 가능
			boolean is_null = true;
			Menu dbMenu = menuDao.selectMenuByNameAndHI(menu);
			if(dbMenu != null) {
				is_null = false;
				oos.writeBoolean(is_null);
				oos.flush();
				return;
			}
			//등록 후 결과 반환
			boolean res = menuDao.insertMenu(menu);
			oos.writeBoolean(res);
			oos.flush();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void updateMenu() {
		List<Menu> list = menuDao.selectAllMenu();
		try {
			oos.writeObject(list);
			oos.flush();
			
			String meCode = ois.readUTF();
			String meName = ois.readUTF();
			int mePrice = ois.readInt();
			String meContent = ois.readUTF();
			String meHotIce = ois.readUTF();
			
			// db안에 메뉴코드가 meCode인 제품 가져오기 
			Menu dbmenu = menuDao.selectMenuByCode(meCode);
			//Menu dbmenu2 = menuDao.selectMenu(meName, mePrice);
			boolean res = true;
			
			// db안에 메뉴코드가 meCode인 제품가 있는지
			// || dbmenu2 != null
			if(dbmenu == null ) {
				System.out.println("[업데이트 실패 : 존재하지 않는 메뉴.]");
				res = false;
				oos.writeBoolean(res);
				oos.flush();
				return;
			}
			// 현재 메뉴 이름과 입력받은 메뉴 이름이 다를 때 
			// 입력받은 메뉴 이름과 H or I 상태가 이미 등록되있는게 있을경우 
		    String currentMeName = dbmenu.getMeName();
		    if (!currentMeName.equals(meName)) {
		    	boolean exists = menuDao.menuExists(meName, meHotIce);
		    	if (exists) {
		    		System.out.println("[업데이트 실패 : 이미 존재하는 메뉴.]");
		    		res = false;
		    		oos.writeBoolean(res);
					oos.flush();
		    		return;
		    	}
		    }
		    

			res= menuDao.updateMenu(meCode, meName, mePrice, meContent, meHotIce);
			oos.writeBoolean(res);
			oos.flush();

		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		
	}
	private void deleteMenu() {
		List<Menu> list = menuDao.selectAllMenu();
		try {
			oos.writeObject(list);
			oos.flush();
		
			String meCode = ois.readUTF();
			
			Menu dbMenu = menuDao.selectMenuByCode(meCode);
			
			boolean res = true;

			if(dbMenu == null) {
				res = false;
				oos.writeBoolean(res);
				oos.flush();
				return;
			}
			try {
		        // 카테고리 삭제
				res=menuDao.deleteMenu(meCode);
		    } catch (PersistenceException e) {
		        // SQLException을 확인하여 외래 키 제약 조건 위반인지 확인
		        if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
		            System.out.println("[해당 메뉴는 다른 데이터와 연결되어 있어 삭제할 수 없습니다.]");
		            res = false;
		        } else {
		            System.out.println("[카테고리 삭제 중 오류가 발생했습니다]");
		        }
		    }
			
			
			oos.writeBoolean(res);
			oos.flush();

		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		
	}
	private void runCategoryMenu(int num) {
		
		switch (num) {
		case 1:
			insertCategory();
			break;
		case 2:
			updateCategory();
			break;
		case 3:
			deleteCategory();
			break;
		case 4:
			break;
		default:
		}
	}

	private void insertCategory() {
		try {
			// 받아옴
			Category category = (Category) ois.readObject();
			// 중복 있는지 확인 : null이여야 등록 가능

			boolean is_null = true;
			Category dbCategory = categoryDao.selectCategoryByName(category);
			if (dbCategory != null) {
				is_null = false;
				oos.writeBoolean(is_null);
				oos.flush();
				return;
			}
			// 등록 후 결과 반환
			boolean res = categoryDao.insertCategory(category);
			oos.writeBoolean(res);
			oos.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateCategory() {
		List<Category> list = categoryDao.seletAllCategory();
		try {
			oos.writeObject(list);
			oos.flush();
			int caNum = ois.readInt();
			String caName = ois.readUTF();
			String caCode = ois.readUTF();

			Category dbCategory = categoryDao.seletCategoryByNum(caNum);
			Category dbCategory2 = categoryDao.seletCategory(caName, caCode);
			boolean res = true;

			if (dbCategory == null || dbCategory2 != null) {
				System.out.println("[업데이트 실패 : 이미 등록중인 카테고리.]");
				res = false;
				oos.writeBoolean(res);
				oos.flush();
				return;
			}
			// 현재 카테고리 이름
			String currentCaName = dbCategory.getCaName();
			if (!currentCaName.equals(caName)) {
				boolean exists = categoryDao.categoryExists(caName);
				if (exists) {
					System.out.println("[업데이트 실패 : 이미 사용 중인 카테고리 이름.]");
					res = false;
					oos.writeBoolean(res);
					oos.flush();
					return;
				}
			}

			res = categoryDao.updateCategory(caNum, caName, caCode);
			oos.writeBoolean(res);
			oos.flush();

		} catch (IOException ioException) {
			ioException.printStackTrace();
		}

	}

	private void deleteCategory() {
		List<Category> list = categoryDao.seletAllCategory();
		try {
			oos.writeObject(list);
			oos.flush();
			int caNum = ois.readInt();

			Category dbCategory = categoryDao.seletCategoryByNum(caNum);

			boolean res = true;
			if (dbCategory == null) {
				res = false;
				oos.writeBoolean(res);
				oos.flush();
				return;
			}
			try {
				// 카테고리 삭제
				res = categoryDao.deleteCategory(caNum);
			} catch (PersistenceException e) {
				// SQLException을 확인하여 외래 키 제약 조건 위반인지 확인
				if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
					System.out.println("[해당 카테고리는 다른 데이터와 연결되어 있어 삭제할 수 없습니다.]");
					res = false;
				} else {
					System.out.println("[카테고리 삭제 중 오류가 발생했습니다]");
				}
			}
			oos.writeBoolean(res);
			oos.flush();

		} catch (IOException ioException) {
			ioException.printStackTrace();
		}

	}

	private void runTagMenu(int num) {
		switch (num) {
		case 1:
			insertTag();
			break;
		case 2:
			updateTag();
			break;
		case 3:
			deleteTag();
			break;
		case 4:
			break;
		default:
		}

	}

	private void insertTag() {
		try {
			// 받아옴
			Tag tag = (Tag) ois.readObject();
			// 중복 있는지 확인 : null이여야 등록 가능
			boolean is_null = true;
			Tag dbTag = tagDao.selectTag(tag);
			if (dbTag != null) {
				is_null = false;
				oos.writeBoolean(is_null);
				oos.flush();
				return;
			}
			// 등록 후 결과 반환
			boolean res = tagDao.insertTag(tag);
			oos.writeBoolean(res);
			oos.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void updateTag() {
		try {
			List<Tag> dbTagList = tagDao.selectAllTag();
			oos.writeObject(dbTagList);
			oos.flush();
			// 수정할 태그번호와 새 태그이름 받기
			int tagNum = ois.readInt();
			String newTagName = ois.readUTF();
			// db에 적용하기
			// 번호에 맞는 Tag 객체 가져오기
			Tag dbTag = tagDao.selectTagByNum(tagNum);
			Tag dbTag2 = tagDao.selectTagByName(newTagName);
			// null 체크 : 객체가 없다면 실패처리
			if (dbTag == null || dbTag2 != null) {
				boolean is_null = false;
				oos.writeBoolean(is_null);
				oos.flush();
				return;
			}
			// Tag 객체에 newTagName로 업데이트 시키기
			boolean res = tagDao.updateTag(dbTag, newTagName);
			oos.writeBoolean(res);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void deleteTag() {
		try {
			List<Tag> dbTagList = tagDao.selectAllTag();
			oos.writeObject(dbTagList);
			oos.flush();
			// 삭제할 번호
			int tagNum = ois.readInt();
			Tag dbTag = tagDao.selectTagByNum(tagNum);
			// null 체크 : 객체가 없다면 실패처리
			boolean res = true;
			if (dbTag == null) {
				res = false;
				oos.writeBoolean(res);
				oos.flush();
				return;
			}

			try {
				// 태그 삭제
				res = tagDao.deleteTag(dbTag);
			} catch (PersistenceException e) {
				// SQLException을 확인하여 외래 키 제약 조건 위반인지 확인
				if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
					System.out.println("[해당 태그는 다른 메뉴와 연결되어 있어 삭제할 수 없습니다.]");
					res = false;
				} else {
					System.out.println("[태그 삭제 중 오류가 발생했습니다]");
				}
			}
			oos.writeBoolean(res);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void runIncomeMenu(int num) {
		switch (num) {
		case 1:
			DayIncome();
			break;
		case 2:
			MonthIncome();
			break;
		case 3:
			YearIncome();
			break;
		case 4:
			TotalIncome();
			break;
		case 5:
			break;
		default:
		}

	}

	private void DayIncome() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String today = sdf.format(new Date());
			int total = 0;

			int dbIncome = incomeDao.incomeDay();
			oos.writeInt(dbIncome);
			oos.flush();

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	private void MonthIncome() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String today = sdf.format(new Date());
			int total = 0;

			int dbIncome = incomeDao.incomeMonth();
			oos.writeInt(dbIncome);
			oos.flush();

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	private void YearIncome() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String today = sdf.format(new Date());
			int total = 0;

			int dbIncome = incomeDao.incomeYear();
			oos.writeInt(dbIncome);
			oos.flush();

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	private void TotalIncome() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String today = sdf.format(new Date());
			int total = 0;

			int dbIncome = incomeDao.totalIncome();
			oos.writeInt(dbIncome);
			oos.flush();

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	// 고객 로그인
	private void runCustomerService(Member member) {
		try {
			int menu = 0;
			do {
				menu = ois.readInt();
				// 회원탈퇴를 위한 추가 코드
				if (menu == 4) {
					boolean res = withdrawMembership(member);
					if (res) {
						menu = 5;
					}
				}
				runCustomerMenu(menu, member);
			} while (menu != 5);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void runCustomerMenu(int menu, Member member) {
		switch (menu) {
		case 1:
			System.out.println("[1. 메뉴 조회]");
//			try {
//				int num = 0;
//				do {
//					num = ois.readInt();
//					runCategoryMenu(num);
//				}while(num != 4);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			break;
		case 2:
			System.out.println("[2. 주문 내역 조회]");
			break;
		case 3:
			updateInfo();
			break;
		case 4:
			break;
		case 5:
			// 로그아웃
			System.out.println("[로그아웃]");
			break;
		default:
			System.out.println("[잘못된 입력]");
		}

	}

	private void updateInfo() {
		
	}

	private boolean withdrawMembership(Member member) {
		boolean res3 = false;
		try {
			// 클라이언트로부터 신호받음
			String answer = ois.readUTF();
			if (answer.equals("N")) {
				return false;
			}
			//
			String id = ois.readUTF();
			String pw = ois.readUTF();
			boolean res1 = false;
			boolean res2 = false;
			res3 = false;
			if (member.getMId().equals(id) && member.getMPw().equals(pw)) {
				res1 = stampDao.deleteMember(id);
				res2 = couponDao.deleteMember(id);
				res3 = memberDao.deleteMember(member);
			}
			oos.writeBoolean(res3);
			oos.flush();
			return res3;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res3;
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
				stampDao.insertStamp(member.getMId());
				couponDao.insertCoupon(member.getMId());

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
