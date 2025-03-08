package cafePro_DB.model.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class Category implements Serializable{

   private static final long serialVersionUID = 1L;
   private int caNum;      //CA_NUM
   private String caName;   //CA_NAME
   private String caCode;      //CA_CODE

}