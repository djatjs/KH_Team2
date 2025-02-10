package cafeProgram;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class Income {
	
	private static final long serialVersionUID = 1L;
	
	private Date date;//구매 날짜
	private int money;//금액
	
	public Income(int money) {
		this.money = money;
		this.date = new Date();
	}
	public Date getDateStr() {
		return date;
	}
	public int getAmount() {
        return money;
    }
	@Override
	public String toString() {
		return getDateStr() +  " / " + money +"원";
	}
}