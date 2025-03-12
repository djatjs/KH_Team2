package model.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class Menu_Tag implements Serializable{

   private static final long serialVersionUID = 1L;
   private int mtNum;      		//MT_NUM
   private int mtThgNum;   		//MT-TAG-NUM
   private String mtMeCode;     //MT-ME-CODE

}