package cafeProgram;

import lombok.Data;

@Data
public class Cafe {
	// 메뉴, 가격, 매출
	private String menu;
	private int price;
	private static int income;
	
	//인기순 
	

	public Cafe(String menu, int price) {
		this.menu = menu;
		this.price = price;
	}

	public int getIncome() {
		return income;
	}
	public static void addIncome(int price) {
        income += price; // 수익 추가
        //count ++;
    }
	//고객이 주문할려고 할 때 리스트로 보이는데
	//그 리스트를 정렬(인기순으로)
	
	//주문단계 갈 때
	// 메뉴 확인 - 리스트 정렬
	// 결제- 스탬프 적립 함수, 매출 증가 함수 사용 - 오준호
	
	//관리자
	//-메뉴 추가, 수정, 삭제, 매출 확인(총매출만 할건지, 날짜 아무거나 샘플데이터 넣어서 확인하는식으로 일 월 매출 확인을 추가할건지)
	
}
