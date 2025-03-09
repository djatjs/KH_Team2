package cafePro_DB.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Scanner;

import cafePro_DB.model.vo.Tag;

public class MenuManager {
	private static Scanner scan = new Scanner(System.in);
	private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
	public MenuManager(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
	}

	public void category() {
		System.out.println("[카테고리]");
		printMenu();
		
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
			for(Tag tmp : dbTag) {
				System.out.println(tmp);
			}
			// 바꿀 태그 번호와 새 태그명 입력받기
			System.out.print("수정할 태그의 번호를 입력하세요 : ");
			int tagNum = scan.nextInt();
			System.out.print("새 태그명을 입력하세요 : ");
			String newTagName = scan.next();
	        scan.nextLine();
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
			// 태그 목록받기
			List<Tag> dbTag = (List<Tag>) ois.readObject();
			for(Tag tmp : dbTag) {
				System.out.println(tmp);
			}
			// 삭제할 태그 번호 입력받기
			System.out.print("수정할 태그의 번호를 입력하세요 : ");
			int tagNum = scan.nextInt();
	        scan.nextLine();
			// 서버로 보냄
	        oos.writeInt(tagNum);
	        oos.flush();
	        // db처리 결과 받음
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
		System.out.println("[메뉴]");
		printMenu();
		
	}

	public void income() {
		System.out.println("[매출 확인]");
		
	}
	
	
}