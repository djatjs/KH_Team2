package model.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Cart implements Serializable{
	private static final long serialVersionUID = 1L;
	private int ctNum;				//CT_NUM
	private String ctMId; 			//CT_M_ID
	private int ctPrice;			//CT_PRICE
	private String ctstatus;			//CT_STATUS 		ENUM("Y","N") DEFAULT "N"
	List<CartList> list;
	
	
}
