package cafeprogram;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

@Data
public class Customer  implements Serializable{

	private static final long serialVersionUID = 761357306724153826L;
	
		private String id;
	   	private String pw;

	    public Customer(String id, String pw) {
	        this.id = id;
	        this.pw = pw;
	    }

		@Override
		public String toString() {
			return "[" + "아이디 : " + id + ", " +  "비밀번호 : " + pw + "]";
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
			return Objects.equals(id, other.id) && Objects.equals(pw, other.pw);
		}
	    
}