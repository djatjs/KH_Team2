package cafeProgram;

public enum Menu {
	에스프레소, 아메리카노, 카페라떼, 카푸치노, 카페모카, 카라멜마끼아또;
	
	public boolean isMenu(String str) {
		try {
			return Menu.valueOf(str) != null;			
		} catch (Exception e) {
			return false;
		}
	}
}