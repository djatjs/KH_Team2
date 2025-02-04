package cafeprogram;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

@Data
public class Cafe implements Serializable{
	private static final long serialVersionUID = 3052890581774008663L;
	private String menu;
	private int price;
	private static int income;
	
	
	public Cafe(String menu, int price) {
		this.menu = menu;
		this.price = price;
	}

	//메뉴 이름만 비교해서 같으면 서로 같은 객체로 취급
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

	
	
}
