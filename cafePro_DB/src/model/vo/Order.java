package model.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Order implements Serializable{
	private static final long serialVersionUID = 1L;
	private int orNum;      //OR_NUM
	   private int orCtNum;      //OR_CT_NUM
	   private String orMId;      //OR_M_ID
	   private int orUse;      //OR_USE
	   private int orTotal;      //OR_TOTAL
	   private Date orDate;      //OR_DATE
	   private int orFinal;      //OR_FINAL
}
