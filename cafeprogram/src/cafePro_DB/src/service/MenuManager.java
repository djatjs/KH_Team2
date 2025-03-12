package service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dao.CategoryDAO;
import model.vo.Category;
import model.vo.Menu;
import model.vo.Tag;

public class MenuManager {
	private static Scanner scan = new Scanner(System.in);
	private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
	public MenuManager(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
	}

	public void category() {
		try {
			int num = 0;
			do {
				printMenu();
				num = scan.nextInt();
				scan.nextLine();
				oos.writeInt(num);
				oos.flush();
				runCategoryMenu(num); 
			} while (num != 4); // 뒤로 가기 메뉴 추가
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void runCategoryMenu(int num) {
		switch(num) {
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
		Category category = inputCategory();
		try {
			oos.writeObject(category);
			oos.flush();
			boolean res = ois.readBoolean();
			if (res) {
				System.out.println("[카테고리 등록 완료]");
			} else {
				System.out.println("[카테고리 등록 실패]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateCategory() {
		try {
			List<Category> dbCategory = (List<Category>) ois.readObject();
			List<Integer> categoryNumList = new ArrayList<>();
			for (int i = 0; i < dbCategory.size(); i++) {
				Category category = dbCategory.get(i);
				categoryNumList.add(category.getCaNum()); // 실제 DB tagNum 저장
				System.out.println((i + 1) + ". " + category.getCaName()); // 1부터 출력
			}
			System.out.println("------------------");
			int categoryIndex = 0;
			while (true) {
				System.out.print("수정할 카테고리의 번호를 입력하세요 : ");
				categoryIndex = scan.nextInt();
				if (categoryIndex >= 1 && categoryIndex <= categoryNumList.size()) {
					break;
				} else {
					System.out.println("[잘못된 번호입니다. 다시 입력하세요.]");
					scan.nextLine();
				}
			}	
			System.out.print("수정할 카테고리 이름 : ");
			String caName = scan.next();
			System.out.print("수정할 카테고리 코드 : ");
			String caCode= scan.next();
			scan.nextLine();
			System.out.println("------------------");
			int caNum = categoryNumList.get(categoryIndex - 1);
			oos.writeInt(caNum);
			oos.writeUTF(caName);
			oos.writeUTF(caCode);
			oos.flush();
			
			boolean res = ois.readBoolean();
			if(res == true) {
				System.out.println("수정이 완료 되었습니다.");
			}else {
				System.out.println("수정이 실패했습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteCategory() {
		try {
			List<Category> dbCategory = (List<Category>) ois.readObject();
			List<Integer> categoryNumList = new ArrayList<>();
			for (int i = 0; i < dbCategory.size(); i++) {
				Category category = dbCategory.get(i);
				categoryNumList.add(category.getCaNum()); // 실제 DB tagNum 저장
				System.out.println((i + 1) + ". " + category.getCaName()); // 1부터 출력
			}
			System.out.println("------------------");
			int categoryIndex = 0;
			while (true) {
				System.out.print("삭제할 카테고리의 번호를 입력하세요 : ");
				categoryIndex = scan.nextInt();
				if (categoryIndex >= 1 && categoryIndex <= categoryNumList.size()) {
					break;
				} else {
					System.out.println("[잘못된 번호입니다. 다시 입력하세요.]");
					scan.nextLine();
				}
			}	
			scan.nextLine();
			System.out.println("------------------");
			int caNum = categoryNumList.get(categoryIndex - 1);
			oos.writeInt(caNum);
			oos.flush();
			
			boolean res = ois.readBoolean();
			if(res == true) {
				System.out.println("[카테고리 삭제 성공!]");
			}else {
				System.out.println("[이 카테고리는 다른 데이터와 연결되어 있어 삭제할 수 없습니다.]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private Category inputCategory() {
		System.out.println("------------------");
		System.out.print("카테고리 이름 : ");
		String caName = scan.next();
		System.out.print("카테고리 코드 : ");
		String caCode = scan.next();
		scan.nextLine();
		System.out.println("------------------");
		return new Category(caName, caCode);
	}

	public void tag() {
		try {
			int num =0;
			do {				
				printMenu();
				num = scan.nextInt();
				scan.nextLine();
				oos.writeInt(num);
				oos.flush();
				runTagMenu(num); 
			}while(num!=4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void printMenu() {
		System.out.println("------------------");
		System.out.println("1. 등록");
		System.out.println("2. 수정");
		System.out.println("3. 삭제");
		System.out.println("4. 뒤로가기");
		System.out.println("------------------");
		System.out.print("메뉴 선택: ");
	}
		
	private void runTagMenu(int num) {
		switch(num) {
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
        	System.out.print("태그명 : ");
        	String tagname = scan.next();
        	scan.nextLine();
        	Tag tag = new Tag(tagname);
			oos.writeObject(tag);
			oos.flush();
			
			boolean res = ois.readBoolean();
			if(res) {
				System.out.println("[태그 등록 완료]");
			}
			else {
				System.out.println("[태그 등록 실패]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateTag() {
		try {
			// 태그 목록받기
			List<Tag> dbTag = (List<Tag>) ois.readObject();
			// 사용자에게 보이는 번호와 실제 tagNum을 매핑하기 위해 리스트 사용
	        List<Integer> tagNumList = new ArrayList<>();
	        // 태그 출력
	        for (int i = 0; i < dbTag.size(); i++) {
	            Tag tag = dbTag.get(i);
	            tagNumList.add(tag.getTagNum()); // 실제 DB tagNum 저장
	            System.out.println((i + 1) + ". " + tag.getTagName()); // 1부터 출력
	        }
			// 바꿀 태그 번호와 새 태그명 입력받기
			int userIndex = 0;
	        while (true) {
	            // 사용자 입력 받기
	            System.out.print("수정할 태그의 번호를 입력하세요 : ");
	            userIndex = scan.nextInt();
	            // 입력값 검증 (리스트 범위를 벗어나면 오류)
	            if (userIndex >= 1 && userIndex <= tagNumList.size()) {
	                break; // 유효한 입력이면 루프 종료
	            } else {
	                System.out.println("[잘못된 번호입니다. 다시 입력하세요.]");
	                scan.nextLine();	            
	            }
	        }
			System.out.print("새 태그명을 입력하세요 : ");
			String newTagName = scan.next();
	        scan.nextLine();
	        int tagNum = tagNumList.get(userIndex - 1);	        
			// 서버로 보냄
	        oos.writeInt(tagNum);
	        oos.writeUTF(newTagName);
	        oos.flush();
	        // db처리 결과 받음
	        boolean res = ois.readBoolean();
	        if(res) {
	        	System.out.println("[태그 수정 완료]");
	        }else {
	        	System.out.println("[태그 수정 실패]");
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteTag() {
		try {
			List<Tag> dbTag = (List<Tag>) ois.readObject();
	        List<Integer> tagNumList = new ArrayList<>();

	        for (int i = 0; i < dbTag.size(); i++) {
	            Tag tag = dbTag.get(i);
	            tagNumList.add(tag.getTagNum()); 
	            System.out.println((i + 1) + ". " + tag.getTagName()); 
	        }
	        int userIndex = 0;
	        while (true) {
	        	System.out.print("수정할 태그의 번호를 입력하세요 : ");
	        	userIndex = scan.nextInt();
	        	if (userIndex >= 1 && userIndex <= tagNumList.size()) {
	        		break;
	        	} else {
	        		System.out.println("[잘못된 번호입니다. 다시 입력하세요.]");
	        		scan.nextLine();	            
	        	}
	        }
	        int tagNum = tagNumList.get(userIndex - 1);
	        oos.writeInt(tagNum);
	        oos.flush();
	        boolean res = ois.readBoolean();
	        if(res) {
	        	System.out.println("[태그 삭제 완료]");
	        }else {
	        	System.out.println("[태그 삭제 실패]");
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void menu() {
		try {
			int num =0;
			do {				
				printMenu();
				num = scan.nextInt();
				scan.nextLine();
				oos.writeInt(num);
				oos.flush();
				runMenuMenu(num); 
			}while(num!=4);
		} catch (Exception e) {
			e.printStackTrace();
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
			 	showCategories();
			 	System.out.print("카테고리를 선택하세요 : ");
			 	int caNum = scan.nextInt();
			 	
			 	
			 	System.out.print("메뉴코드 : ");
	        	String meCode = scan.next();
	        	System.out.print("코드넘버 : ");
	        	int meCaNum = scan.nextInt();
			 	System.out.print("메뉴명 : ");
	        	String meName = scan.next();
	        	System.out.print("메뉴 가격 : ");
	        	int mePrice = scan.nextInt();
	        	System.out.print("따뜻한/차가운(H/I) : ");
	        	String meHotIce = scan.next();
	        	System.out.print("메뉴 설명 : ");
	        	String meContent = scan.next();
	        	
	        	scan.nextLine();
	        	
	        	Menu menu = new Menu(meCode, meCaNum, meName, mePrice, meHotIce, meContent);
	        	
	        	oos.writeInt(caNum);
				oos.writeObject(menu);
				oos.flush();
				
				boolean res = ois.readBoolean();
				if(res) {
					System.out.println("[메뉴 등록 완료]");
				}
				else {
					System.out.println("[메뉴 등록 실패]");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}

	private void showCategories() {
		List<Category> list;
		try {
			List<Category> dbCategory = (List<Category>) ois.readObject();
			List<Integer> categoryNumList = new ArrayList<>();
			System.out.println("=== 현재 등록된 카테고리 목록 ===");
			if(dbCategory.isEmpty()) {
				System.out.println("등록된 카테고리가 없습니다.");
				return;
			}
			for (int i = 0; i < dbCategory.size(); i++) {
				Category category = dbCategory.get(i);
				categoryNumList.add(category.getCaNum()); // 실제 DB tagNum 저장
				System.out.println((i + 1) + ". " + category.getCaCode()); // 1부터 출력
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateMenu() {
		try {
			List<Menu> dblist = (List<Menu>) ois.readObject();
			List<String> menuNumList = new ArrayList<>();
			for (int i = 0; i < dblist.size(); i++) {
				Menu menu = dblist.get(i);
				menuNumList.add(menu.getMeCode()); // 실제 DB meCode 저장
				System.out.println((i + 1) + ". " + menu.getMeName()); // 1부터 출력
			}
			System.out.println("------------------");
			int menuIndex = 0;
			while (true) {
				System.out.print("수정할 메뉴의 번호를 입력하세요 : ");
				menuIndex = scan.nextInt();
				if (menuIndex >= 1 && menuIndex <= menuNumList.size()) {
					break;
				} else {
					System.out.println("[잘못된 번호입니다. 다시 입력하세요.]");
					scan.nextLine();
				}
			}	
			
			System.out.print("수정할 메뉴 코드 : ");
			String meCode = scan.next();
			System.out.print("수정할 메뉴 이름 : ");
			String meName = scan.next();
			System.out.print("수정할 메뉴 가격 : ");
			int mePrice= scan.nextInt();
			scan.nextLine();
			System.out.println("------------------");
			meCode = menuNumList.get(menuIndex - 1);
			
			oos.writeUTF(meCode);
			oos.writeUTF(meName);
			oos.writeInt(mePrice);
			oos.flush();
			
			boolean res = ois.readBoolean();
			if(res == true) {
				System.out.println("수정이 완료 되었습니다.");
			}else {
				System.out.println("수정이 실패했습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void deleteMenu() {
		try {
			List<Menu> dbMenu = (List<Menu>) ois.readObject();
			List<String> menuNumList = new ArrayList<>();
			for (int i = 0; i < dbMenu.size(); i++) {
				Menu menu = dbMenu.get(i);
				menuNumList.add(menu.getMeCode()); // 실제 DB meCode 저장
				System.out.println((i + 1) + ". " + menu.getMeName()); // 1부터 출력
			}
			System.out.println("------------------");
			int menuIndex = 0;
			while (true) {
				System.out.print("삭제할 메뉴의 번호를 입력하세요 : ");
				menuIndex = scan.nextInt();
				if (menuIndex >= 1 && menuIndex <= menuNumList.size()) {
					break;
				} else {
					System.out.println("[잘못된 번호입니다. 다시 입력하세요.]");
					scan.nextLine();
				}
			}	
			
			scan.nextLine();
			System.out.println("------------------");
			String meCode = menuNumList.get(menuIndex - 1);
			oos.writeUTF(meCode);
			oos.flush();
			
			boolean res = ois.readBoolean();
			if(res == true) {
				System.out.println("삭제가 완료 되었습니다.");
			}else {
				System.out.println("삭제가 실패했습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void income() {
		System.out.println("[매출 확인]");
		
	}
	
	
}