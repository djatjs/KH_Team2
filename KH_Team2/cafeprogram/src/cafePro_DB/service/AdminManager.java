package cafePro_DB.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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

	public void updateCategory() {
		try {
			List<Category> dbCa = (List<Category>) ois.readObject();

			List<Integer> categoryNumList = new ArrayList<>();

			for (int i = 0; i < dbCa.size(); i++) {
				Category category = dbCa.get(i);
				categoryNumList.add(category.getCaNum());
				System.out.println((i + 1) + ". " + category.getCaName());
			}

			int categoryIndex = 0;
			while (true) {
				// 사용자 입력 받기
				System.out.print("수정할 카테고리의 번호를 입력하세요 : ");
				categoryIndex = scan.nextInt();

				// 입력값 검증 (리스트 범위를 벗어나면 오류)
				if (categoryIndex >= 1 && categoryIndex <= categoryNumList.size()) {
					break; // 유효한 입력이면 루프 종료
				} else {
					System.out.println("[잘못된 번호입니다. 다시 입력하세요.]");
					scan.nextLine();
				}
			}
			System.out.println("------------------");
			System.out.println("새 카테고리 명을 입력하세요 : ");
			String caName = scan.next();
			System.out.println("수정할 카테고리 코드를 입력하세요 : ");
			System.out.println("------------------");
			String caCode = scan.next();
			scan.nextLine();
			System.out.println("------------------");

			int caNum = categoryNumList.get(categoryIndex - 1);

			oos.writeInt(caNum);
			oos.writeUTF(caName);
			oos.writeUTF(caCode);
			oos.flush();

			boolean res = ois.readBoolean();

			if (res == true) {
				System.out.println("수정이 완료 되었습니다.");
			} else {
				System.out.println("수정이 실패했습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteCategory() {
		try {
			// 카테고리 리스트를 읽어옵니다
			List<Category> dbCa = (List<Category>) ois.readObject();
			List<Integer> categoryNumList = new ArrayList<>();

			// 카테고리 번호와 이름을 출력합니다
			for (int i = 0; i < dbCa.size(); i++) {
				Category category = dbCa.get(i);
				categoryNumList.add(category.getCaNum());
				System.out.println((i + 1) + ". " + category.getCaName());
			}

			int categoryIndex = 0;
			while (true) {
				// 삭제할 카테고리 번호를 입력받습니다
				System.out.print("삭제할 카테고리의 번호를 입력하세요 : ");
				categoryIndex = scan.nextInt();
				scan.nextLine();
				// 번호가 유효한지 확인합니다
				if (categoryIndex >= 1 && categoryIndex <= categoryNumList.size()) {
					break;
				} else {
					System.out.println("[잘못된 번호입니다. 다시 입력하세요.]");
					 // 버퍼 비우기
				}
			}

			// 삭제할 카테고리 번호를 가져옵니다
			int caNum = categoryNumList.get(categoryIndex - 1);

			// 삭제할 카테고리 번호를 전송합니다
			oos.writeInt(caNum);
			oos.flush();

			// 삭제 결과를 받습니다
			boolean res = ois.readBoolean();

			// 삭제 결과 출력
			if (res= true) {
				System.out.println("삭제가 완료 되었습니다.");
			} else {
				System.out.println("삭제가 실패했습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}