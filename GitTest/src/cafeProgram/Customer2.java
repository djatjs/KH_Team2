package cafeProgram;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

@Data
public class Customer2  implements Serializable{

	    private static final long serialVersionUID = 1L; 
	    private String id;
	    private String pw;

	    public Customer2(String id, String pw) {
	        this.id = id;
	        this.pw = pw;
	    }

	    public String getName() {
	        return id;
	    }

	    public String getPassWord() {
	        return pw;
	    }
	}
