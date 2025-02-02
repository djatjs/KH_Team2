package cafeprogram;

import java.io.Serializable;

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
	
	
}
