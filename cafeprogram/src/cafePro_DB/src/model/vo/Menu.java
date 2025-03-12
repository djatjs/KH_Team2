package model.vo;

import java.io.Serializable;

import lombok.Data;
@Data
public class Menu implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String meCode;			//ME_CODE
	private int meCaNum;				//ME_CA_NUM
	private String meName;			//ME_NAME
	private int mePrice;				//ME_PRICE
	private String meContent;			//ME_CONTENT
	private String meThumb;			//ME_THUMB
	private String meAble = "Y";	//ME_ABLE
	private String meHotIce = "H";		//ME_HOT_ICE
	
	
	public Menu(String meCode, int meCaNum, String meName, int mePrice, String meContent) {
		
		this.meCode = meCode;
		this.meCaNum = meCaNum;
		this.meName = meName;
		this.mePrice = mePrice;
		this.meContent = meContent;
	}
	
	
	

}
