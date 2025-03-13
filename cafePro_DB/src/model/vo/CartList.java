package model.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class CartList implements Serializable{

   private static final long serialVersionUID = 1L;
   private int clNum;      		//CL_NUM
   private int ctMeCode;      	//CT_ME_CODE
   private int ctNum;      		//CT_NUM
   private int ctAmount = 0;     	//CT_AMOUNT
   Menu menu;
}