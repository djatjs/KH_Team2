package model.vo;

import java.io.Serializable;
import java.sql.Date;

import lombok.Data;

@Data
public class Income implements Serializable {
	private static final long serialVersionUID = 1L;

	private Date inDate; // IN_DATE
	private int inMoney = 0; // IN_MONEY

	
	public Income(Date inDate, int inMoney) {
		this.inDate = inDate;
		this.inMoney = inMoney;
	}

	@Override
	public String toString() {
		return "Income [inDate=" + inDate + ", inMoney=" + inMoney + "]";
	}

	
}
