package cafePro_DB.model.vo;

import java.io.Serializable;

public class Menu implements Serializable{
	private static final long serialVersionUID = 1L;
	private String code;			//ME_CODE
	private int num;				//ME_CA_NUM
	private int price;				//ME_PRICE
	private String content;			//ME_CONTENT
	private String thumb;			//ME_THUMB
	private OrderAble orderAble;	//ME_ABLE
	private HotOrIce hotOrIce;		//ME_HOT_ICE
	
	public enum OrderAble {
        Y, N
    }
	
	public enum HotOrIce {
        Y, N
    }
}
