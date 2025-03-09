package cafePro_DB.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Scanner;

import cafePro_DB.model.vo.Category;

public class AdminManager {
	private static Scanner scan = new Scanner(System.in);
	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;

	public AdminManager(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
	}

	public void insertCategory() {
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

	public void editCategory() {
		try {
			List<Category> list = (List<Category>) ois.readObject();

			for (Category cat : list) {
				System.out.println(list);
			}
			System.out.println("------------------");
			System.out.println("수정할 항목의 번호를 고르세요.");
			int caNum= scan.nextInt();			
			System.out.println("수정할 카테고리 이름");
			String caName = scan.next();
			System.out.println("수정할 카테고리 코드");
			String caCode= scan.next();
			scan.nextLine();
			System.out.println("------------------");
			
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

	public void deleteCategory() {
	}

}