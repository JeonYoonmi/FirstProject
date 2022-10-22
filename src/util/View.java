package util;

public interface View {
	
	int MAIN = 1;
	int JOIN = 2;

	int MEMBER_LOGIN = 3;
	int OWNER_LOGIN = 4;

	//로그인 후에 화면
	int MEMBER_HOME=5;
	int OWNER_HOME=6;

	//메뉴
	int MENU_LIST = 7;
	int MENU_READ = 8;
	int MENU_INSERT = 9;
	int MENU_UPDATE = 10;
	int MENU_DELETE = 11;

	//리뷰
	int REVIEW_READ = 12;
	int REVIEW_INSERT = 13;

	//댓글
	int OWNER_CMT_INSERT = 14;

	//주문서/장바구니
	int ORDER = 15;
	int ORDER_READ = 16;
	int ORDER_UPDATE = 17;
	int ORDER_INSERT = 18; 
	int ORDER_DELETE = 19;

	//매출
	int COST_LIST = 20;

	//마이페이지
	int MYPAGE = 21;
	int MYORDER_LIST = 22;
	int MYORDER_READ = 23;
	int MYPAGE_UPDATE = 24;
	int MYPAGE_NAME_UPDATE = 25;
	int MYPAGE_ADD_UPDATE = 26;
	int MYPAGE_DELETE = 27;
	int MYREVIEW_LIST = 28;
	int MYREVIEW_DELETE = 29;

	//점주
	int OWNER_ORDER_LIST = 30;
	int OWNER_REVIEW_LIST = 31;

	int PAYMENT = 32;

}