package cafePro_DB.model.vo;

import java.io.Serializable;

public class Cart implements Serializable{
	private static final long serialVersionUID = 1L;
	private int ctNum;
	private String ctMId;
	private int ctPrice;
	private Status status = Status.N;
	
	public enum Status{
		Y, N
	}
}
