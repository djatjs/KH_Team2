package model.vo;

import java.io.Serializable;

import lombok.Data;
@Data
public class Tag implements Serializable{

   private static final long serialVersionUID = 1L;
   private int tagNum;      //TAG_NUM
   private String tagName;      //TAG_NAME   
   
   public Tag() {}
   
   public Tag(String tagName) {
	   this.tagName = tagName;
   }

   
   @Override
   public String toString() {
	   return tagNum + ". " + tagName;
   }
   
}
