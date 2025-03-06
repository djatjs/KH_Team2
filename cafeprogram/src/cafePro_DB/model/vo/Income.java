package cafePro_DB.model.vo;

import java.io.Serializable;
import java.sql.Date;

public class Income implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Date date;		//IN_DATE
	private int money;		//IN_MONEY
	
	
	public Income(Date date, int money) {
		this.date = date;
		this.money = money;
	}
	
	
}
