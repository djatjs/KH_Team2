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

import dao.CartDAO;
import dao.CartListDAO;
import dao.CategoryDAO;
import dao.CouponDAO;
import dao.IncomeDAO;
import dao.MemberDAO;
import dao.MenuDAO;
import dao.OrderDAO;
import dao.StampDAO;
import dao.TagDAO;
import model.vo.Cart;
import model.vo.CartList;
import model.vo.Category;
import model.vo.Member;
import model.vo.Menu;
import model.vo.Order;
import model.vo.Tag;

public class ServerManager {
	private MemberDAO memberDao;
	private StampDAO stampDao;
	private CouponDAO couponDao;
	private TagDAO tagDao;
	private CategoryDAO categoryDao;
	private IncomeDAO incomeDao;
	private MenuDAO menuDao;
	private OrderDAO orderDao;
	private CartDAO cartDao;
	private CartListDAO cartListDao;
	
	
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
			orderDao = session.getMapper(OrderDAO.class);
			cartDao = session.getMapper(CartDAO.class);
			cartListDao = session.getMapper(CartListDAO.class);
			
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

	//관리자 로그인
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
		case 1: //카테고리
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
		case 2: //태그
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
		case 3: //메뉴
			try {
				int num = 0;
				do {
					num = ois.readInt();
					runMenuMenu(num);
				}while(num != 6);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 4: //매출
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
	
	//관리자_카테고리
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
			
			// 클라이언트로 부터 전송받은 번호대로 저장되어있는지 확인
			Category dbCategory = categoryDao.seletCategoryByNum(caNum);
			boolean res = true;

			if (dbCategory == null) {
				System.out.println("[업데이트 실패 : 이미 등록중인 카테고리.]");
				res = false;
				oos.writeBoolean(res);
				oos.flush();
				return;
			}
			// 입력 받은 이름이 다른 카테고리 이름과 같은지 확인
			String currentCaName = dbCategory.getCaName();
			if (!currentCaName.equals(caName)) {
				boolean exists = categoryDao.checkExistsByName(caName);
				if (exists) {
					System.out.println("[업데이트 실패 : 이미 사용 중인 카테고리 이름.]");
					res = false;
					oos.writeBoolean(res);
					oos.flush();
					return;
				}
			}
			// 입력 받은 코드명이 다른 카테고리 코드명과 같은지 확인
			String currentCaCode = dbCategory.getCaCode();
			if (!currentCaCode.equals(caCode)) {
				boolean exists = categoryDao.checkExistsByCode(caCode);
				if (exists) {
					System.out.println("[업데이트 실패 : 이미 사용 중인 카테고리 코드.]");
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
	
	//관리자_태그
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
	
	//관리자_메뉴
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
			insertMenuTag();
			break;
		case 5:
			deleteMenuTag();
			break;
		case 6:
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
			String caCode = categoryDao.seletCategoryByNum(caNum).getCaCode();
			boolean res = menuDao.insertMenu(caCode ,menu);
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
			// 수정할 정보
			Menu menu = (Menu) ois.readObject();
			
			// db안에 메뉴코드가 meCode인 제품 가져오기 (저장되어 있는 정보. 수정대상)
			Menu dbmenu = menuDao.selectMenuByCode(menu.getMeCode());
			boolean res = true;
			
			System.out.println(dbmenu);
			
			// db안에 메뉴코드가 meCode인 제품가 있는지
			if(dbmenu == null ) {
				System.out.println("[업데이트 실패 : 존재하지 않는 메뉴.]");
				res = false;
				oos.writeBoolean(res);
				oos.flush();
				return;
			}
			System.out.println("확인");
			// 수정할 메뉴 이름과 입력받은 메뉴 이름이 같고 온도가 다른 경우 -> 	
			if (dbmenu.getMeName().equals(menu.getMeName()) && !dbmenu.getMeHotIce().equals(menu.getMeHotIce())) {
				boolean exists = menuDao.menuExists(menu.getMeName(), menu.getMeHotIce());
				if (exists) {
					System.out.println("[업데이트 실패 : 온도가 같은 메뉴가 존재합니다.]");
					res = false;
					oos.writeBoolean(res);
					oos.flush();
					return;
				}    
			}
			// 이름이 다른 경우 
			else if(!dbmenu.getMeName().equals(menu.getMeName())){
				// 입력받은 메뉴 이름이 이미 존재하는지 확인
				boolean exists = menuDao.menuExists(menu.getMeName(), menu.getMeHotIce());
				if (exists) {
					System.out.println("[업데이트 실패 : 이미 존재하는 메뉴입니다.]");
					res = false;
					oos.writeBoolean(res);
					oos.flush();
					return;
				}
			}
			//근데 입력받은 메뉴 이름이 이미 등록되어있는 경우에 H or I 상태도 똑같으면
			System.out.println("확인2");
			// 이름도 같고 온도도 같은 경우
			res = menuDao.updateMenu(menu);
			oos.writeBoolean(res);
			oos.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
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
	private void insertMenuTag() {
		//db에 등록된 메뉴들과 태그들을 클라이언트로 전송
		
		//null 체크 : 둘다 있으면 true값과 함께 리스트들 전송
		
		//int 자료 2개 받아옴
		
		//db에서 해당 제품에 해당 태그 추가하기.(menu_tagDao)
		
		//db작업 결과 반환
		
	}
	
	private void deleteMenuTag() {
		//db에 등록된 메뉴들과 태그들을 클라이언트로 전송
		
		//null 체크 : 둘다 있으면 true값과 함께 리스트들 전송
		
		//int 자료 2개 받아옴
		
		//db에서 해당 제품에 해당 태그 삭제하기.(menu_tagDao)
		
		//db작업 결과 반환
		
	}
	
	//관리자_매출확인
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
				//탈퇴한 회원을 위한 추가 코드
				if (menu == 4) {
					boolean res = withdrawMembership(member);
					if (res) {menu = 5;}
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
			viewMenuList(member);
			break;
		case 2:
			viewHistory(member);
			break;
		case 3:
			updateInfo(member);
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

	//고객_메뉴조회
	private void viewMenuList(Member member) {
		try {
			int menu = 0;
			do {
				menu = ois.readInt();
				runCartListMenu(menu ,member);
			} while (menu != 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void runCartListMenu(int menu, Member member) {
		switch (menu) {
		case 1:
			insertCart(member);
			break;
		case 2:
			updateCart(member);
			break;
		case 3:
			deleteCart(member);
			break;
		case 4:
			orderCart(member);
			break;
		case 5:
			break;
		default:
			System.out.println("[잘못된 입력]");
		}
	}
	
	//구입을 위해 메뉴로 등록된 항목들을 클라이언트로 전송
	//메뉴 태그 적용 후 여기 먼저 수정해보기
	private void printListMenu() {
		List<Menu> list = menuDao.selectAllMenu();
		try {
			oos.writeObject(list);
			oos.flush();		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//고객_메뉴조회_장바구니담기
	private void insertCart(Member member) {
		try {
			printListMenu();
			// 구매할 제품과 수량 전달받음
			Menu menu = (Menu) ois.readObject();
			int amount = ois.readInt();
			System.out.println(menu+ " " +amount);
			
			// 해당 사용자의 장바구니가 있는지 확인(member.mId, CT_STATUS)
			Cart dbCart = cartDao.selectCart(member);
			//없다면
			if(dbCart == null) {
				// 카트 생성(유저 아이디 사용)
				Boolean createCart = cartDao.insertCart(member);
				Cart dbCart2 = cartDao.selectCart(member);
				Boolean dbCartList = cartListDao.insertMenuToCartList(dbCart2.getCtNum(),menu,amount);
			}
			//있다면
			else {
				// 카트 번호와 제품 번호, 수량을 카트리스트에 담기
				Boolean dbCartList = cartListDao.insertMenuToCartList(dbCart.getCtNum(),menu,amount);
				System.out.println(dbCartList);
			}
			System.out.println(dbCart);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//고객_메뉴조회_장바구니수정
	private void updateCart(Member member) {
		try {
			sendCartLists(member);
			int clAmount = ois.readInt();
			System.out.println(clAmount);
			int clNum = ois.readInt();
			System.out.println(clNum);
			
			boolean isNull = cartListDao.selectCartList(clNum);
			if(!isNull) {
				oos.writeBoolean(isNull);
				oos.flush();
				return;
			}
			
			oos.writeBoolean(isNull);
			oos.flush();
			
			boolean res = cartListDao.updateCartList(clNum, clAmount);
			
			oos.writeBoolean(res);
			oos.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	//고객_메뉴조회_장바구니삭제
	private void deleteCart(Member member) {
		sendCartLists(member);
		// 삭제할 장바구니리스트 번호 받기
		// 번호를 통해 해당 장바구니 리스트 삭제하기
	}
	
	//고객_메뉴조회_장바구니 구매
	private void orderCart(Member member) {
		try {
			boolean is_ready = sendCartLists(member);
			if(!is_ready) {
				return;
			}
			// 고객이 보유하고 있는 쿠폰                               니 리스트 전송 
			int haveCoupon = couponDao.selectCoupon(member.getMId());
			oos.writeInt(haveCoupon);
			oos.flush();
			
			// 구매할건지 응답 받기
			boolean answer = ois.readBoolean();
			if(!answer) {
				return;
			}
			// 구매할 경우 -> 사용할 쿠폰 개수, 원금액, 최종금액 반환받기
			int useCoupon = ois.readInt();
			int totalAmount = ois.readInt();
			int resMoney = ois.readInt();
			String mId = member.getMId();
			// order객체 저장, 쿠폰 사용량만큼 쿠폰 차감, 스탬프 1적립
			Cart cart = cartDao.selectCart(member);
			orderDao.insertOrder(cart.getCtNum(), mId, useCoupon, totalAmount, resMoney);
			stampDao.plusStamp(member.getMId());
			// 스탬프가 10개가되면 0으로 초기화 및 쿠폰 1 추가
			int haveStamp = stampDao.selectStamp(mId);
			if(haveStamp>=10) {
				stampDao.resetStamp(mId);
				couponDao.plusCoupon(mId);
			}
			// 구매상태로 전환 및 매출에 기록
			cartDao.setStatus(mId);
			// 장바구니 내역들 삭제도 잊지 않기
			cartListDao.deleteList(cart.getCtNum());
			boolean resIncome = incomeDao.insertIncome(resMoney);
			oos.writeBoolean(resIncome);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//장바구니 리스트 가져오기
	private boolean sendCartLists(Member member) {
	    try {
	        Cart cart = cartDao.selectCart(member);
	        if (cart == null) {
	        	oos.writeBoolean(false);
	        	oos.flush();
	            return false;
	        }
	        List<CartList> cartLists = cartDao.selectCartList(cart.getCtNum());
	        if (cartLists == null) {
	        	oos.writeBoolean(false);
	        	oos.flush();
	        	return false;
	        }
	        oos.writeBoolean(true);
	        oos.writeObject(cartLists);
	        oos.flush();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false; // 예외 발생 시 false 반환
	    }
	    return true; // 모든 조건을 만족할 때만 true 반환
	}
	
	//고객_주문 내역 조회
	private void viewHistory(Member member) {
		try {
			String mId = member.getMId();
			
			List<Order> dbHistory = orderDao.orderView(mId);
			
			oos.writeObject(dbHistory);  // dbHistory가 null일 수도 있으므로 체크 후 전송
	        oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	//고객_회원 정보 수정
	private void updateInfo(Member member) {
		try {
			String id = ois.readUTF();
			String pw = ois.readUTF();

			if (member.getMId().equals(id) && member.getMPw().equals(pw)) {
				oos.writeBoolean(true);
				oos.flush();

				String newNic = ois.readUTF();
				String newNum = ois.readUTF();
				String newPw = ois.readUTF();

				member.setMNickname(newNic);
				member.setMNumber(newNum);
				member.setMPw(newPw);

				boolean upRes = memberDao.updateMember(member);

				// 업데이트 결과를 클라이언트에 전송`
				if (upRes) {
					oos.writeBoolean(true); // 성공
					oos.flush();
					System.out.println("회원정보 수정 완료");
				} else {
					oos.writeBoolean(false); // 실패
					oos.flush();
					System.out.println("회원정보 수정 실패");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//고객_회원 탈퇴
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
	
	//회원가입
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
	
	//비밀번호 찾기
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
	
	//아이디 중복 체크 : 매퍼 아이디만 쓰도록 수정해야함
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
