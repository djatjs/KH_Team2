package cafePro_DB.model.vo;

import java.io.Serializable;

import lombok.Data;
@Data
public class Tag implements Serializable{

   private static final long serialVersionUID = 1L;
   private int tagNum;      //TAG-NUM
   private int tagName;      //TAG-NAME   
}
