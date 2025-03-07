package cafePro_DB.model.vo;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable{
	private static final long serialVersionUID = 1L;
	private int or_num;      //OR_NUM
	   private int or_ct_num;      //OR_CT_NUM
	   private String or_m_id;      //OR_M_ID
	   private int or_use;      //OR_USE
	   private int or_total;      //OR_TOTAL
	   private Date or_date;      //OR_DATE
	   private int or_final;      //OR_FINAL
}
