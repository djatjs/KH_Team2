package cafeProgram;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

@Data
public class Customer  implements Serializable{

	private static final long serialVersionUID = 1L;
	
		private String id;
	   	private String pw;
	   	private int stamp=0;
	   	private int coupon=0;

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
		
		//쿠폰 사용 : 카페는 매출 0
		public void useCoupon(Cafe menu) {
			this.coupon--;
			menu.getList().add(new Income(0));
		}
		
		//쿠폰 사용 X : 카페는 메뉴의 가격만큼 매출 증가
		public void addStamp(Cafe menu) {
			this.stamp++;
			if(stamp >= 10) {
				stamp-=10;
				this.coupon++;
			}
			
			menu.getList().add(new Income(menu.getPrice()));
			
			}
    
}
