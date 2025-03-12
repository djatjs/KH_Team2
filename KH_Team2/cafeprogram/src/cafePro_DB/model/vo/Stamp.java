package cafePro_DB.model.vo;

import java.io.Serializable;

import lombok.Data;
@Data
public class Stamp implements Serializable {
	private static final long serialVersionUID = 1L;
	private int stKey; 			// ST_KEY
	private String stMId; 		// ST_M_ID
	private int stCount; 		// ST_COUNT

}
