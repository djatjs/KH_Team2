package cafeProgram;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

@Data
public class Customer2  implements Serializable{

	    private static final long serialVersionUID = 1L; // 직렬화 버전 ID 추가
	    private String name;
	    private String passWord;

	    public Customer2(String name, String passWord) {
	        this.name = name;
	        this.passWord = passWord;
	    }

	    public String getName() {
	        return name;
	    }

	    public String getPassWord() {
	        return passWord;
	    }
	}
