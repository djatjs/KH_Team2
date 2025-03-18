package model.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class CartList implements Serializable{

   private static final long serialVersionUID = 1L;
   private int clNum;      		//CL_NUM
   private String clMeCode;      	//CL_ME_CODE
   private int clCtNum;      		//CL_CT_NUM
   private int clAmount = 0;     	//CL_AMOUNT
   Menu menu;


   
   
}