package model.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class Category implements Serializable{

   private static final long serialVersionUID = 1L;
   private int caNum;      //CA_NUM
   private String caName;   //CA_NAME
   private String caCode;      //CA_CODE
   
   public Category(String caName, String caCode) {
		this.caName = caName;
		this.caCode = caCode;
	}

	@Override
	public String toString() {
		return caNum + caName + " : " + caCode;
	}
}