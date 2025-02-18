package cafeProgram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Cafe implements Serializable {
	private static final long serialVersionUID = 1L;
	private String menu;
	private int price;

	List<Income> list;// 입출금 내역

	public Cafe(String menu, int price) {
		this.menu = menu;
		this.price = price;
		list = new ArrayList<Income>();
	}

	// 메뉴 이름만 비교해서 같으면 서로 같은 객체로 취급
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cafe other = (Cafe) obj;
		return Objects.equals(menu, other.menu);
	}

	@Override
	public String toString() {
		return menu + " : " + price + "원";
	}

	public boolean update(Cafe tmp) {
		this.menu = tmp.menu;
		this.price = tmp.price;
		return true;
	}

	public void buyDrink(int money) {
		// 구매 내역 저장까지
		list.add(new Income(money));
	}

}