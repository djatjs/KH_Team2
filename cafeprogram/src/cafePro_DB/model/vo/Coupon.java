package cafePro_DB.model.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class Coupon implements Serializable {
	private static final long serialVersionUID = 1L;

	private int coKey; 		// CO_KEY
	private String coMId; 		// CO_M_ID
	private int coCount = 0; 		// CO_COUNT

}
