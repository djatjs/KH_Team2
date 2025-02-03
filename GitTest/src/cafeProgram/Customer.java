package cafeProgram;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

@Data
public class Customer  implements Serializable{

	  	private String id;
	   	private String pw;
	   	private String authority;//사용자,관리자

	    public Customer(String id, String pw) {
	        this.id = id;
	        this.pw = pw;
	    }

	    public String getId() {
	        return id;
	    }

	    public String getPw() {
	        return pw;
	    }

		@Override
		public String toString() {
			return "[" + "아이디 : " + id + ", " +  "비밀번호 : " + pw + "]";
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Customer other = (Customer) obj;
			return Objects.equals(id, other.id);
		}	
	    
}
