package service;

import util.View;

import util.ScanUtil;

public class HomeService {
	public HomeService() {

	}

//객체를 보관해놓을 변수
	private static HomeService instance;

//객체를 빌려주는 메소드
//다른 클래스에서 이 객체가 필요할 때에는 getInstance를 불러서 객체를 생성한다.
	public static HomeService getInstance() {
		if (instance == null) {
			instance = new HomeService();
		}
		return instance;
	}
	
	public int home() {
		System.out.print("1.주문하기 2.마이페이지 3.지점리뷰 0.로그아웃\n>");
		int input = ScanUtil.nextInt();
		switch(input) {
		case 1: return View.ORDER;
		case 2: return View.MYPAGE;
		case 3: return View.REVIEW_READ;
		case 0: 
			UserService.loginMember = null;
			System.out.println("로그아웃되었습니다.");
			return View.MAIN;
		}
		return View.MEMBER_HOME;
	}
}
