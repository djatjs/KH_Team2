package model.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class Order implements Serializable{
	private static final long serialVersionUID = 1L;
	private int orNum; // OR_NUM
	private int orCtNum; // OR_CT_NUM
	private String orMId; // OR_M_ID
	private int orUse; // OR_USE
	private int orTotal; // OR_TOTAL
	private Date orDate; // OR_DATE
	private int orFinal; // OR_FINAL
	private String meName;  // 메뉴 이름 추가
	private int clAmount;
	
	@Override
	public String toString() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateOr = sdf.format(orDate);
		 
		return 
		   "구매날짜 : " + dateOr + ", "+
		   "메뉴 : " + meName + " " + clAmount + "개"  + ", "+
		   "구매금액 : " + orFinal + "원";

	}
	   
	   
	   
}

