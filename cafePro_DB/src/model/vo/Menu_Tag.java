package model.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Menu_Tag implements Serializable{

   private static final long serialVersionUID = 1L;
   private int mtNum;      		//MT_NUM
   private int mtThgNum;   		//MT_TAG_NUM
   private String mtMeCode;     //MT_ME_CODE
   List<Tag> tags; 
   
}