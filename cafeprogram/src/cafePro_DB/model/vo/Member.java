package cafePro_DB.model.vo;

import java.io.Serializable;
import java.util.Date;

public class Member implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;								//M_ID
	private String pw;								//M_PW
	private String number;							//M_NUMBER
	private String nickname;						//M_NICKNAME
	private UserType userType = UserType.CUSTOMER;  //M_AUTHORITY
	private DelType delType = DelType.N;			//M_DEL
	private Date delTime = null;					//M_DEL_TIME
	
	public enum DelType {
        Y, N
    }
	
	public enum UserType {
        ADMIN, CUSTOMER
    }

	public Member(String id, String pw, String number, String nickname) {
		this.id = id;
		this.pw = pw;
		this.number = number;
		this.nickname = nickname;
	}
	
	
	
}
