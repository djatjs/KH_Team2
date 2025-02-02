package cafeprogram;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

@Data
public class Customer implements Serializable {

	private static final long serialVersionUID = 5689322435214970710L;
	private String id;
	private String password;
	private String type;
	private int stamp;
	private int coupon;
	
	//회원가입용 생성자
	public Customer(String id, String password) {
		this.id = id;
		this.password = password;
		type = "회원";
	}
	
	//로그인용 이퀄
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return Objects.equals(id, other.id) && Objects.equals(password, other.password);
	}

	
	
	
}
