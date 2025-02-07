package cafeprogram;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Income implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7668830909274243298L;
	private Date date;//구매 날짜
	private int money;//금액
	
	public Income(int money) {
		this.money = money;
		this.date = new Date();
	}
	public String getDateStr() {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return f.format(date);
	}
	@Override
	public String toString() {
		return getDateStr() +  " / " + money +"원";
	}
}
