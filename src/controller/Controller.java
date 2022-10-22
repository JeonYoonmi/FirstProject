package controller;

import service.BossService;
import service.HomeService;
import service.MenuService;
import service.MypageService;
import service.OrderService;
import service.ReviewService;
import service.UserService;
import util.ScanUtil;
import util.View;

public class Controller {
	
	public static void main(String[] args) {
		
		new Controller().start();
		
	}
	
	private static UserService userservice = UserService.getInstance();
	private static MenuService menuservice = MenuService.getInstance();
	private static HomeService homeservice = HomeService.getInstance();
	private static OrderService orderservice = OrderService.getInstance();
	private static ReviewService reviewservice = ReviewService.getInstance();
	private static MypageService mypageservice = MypageService.getInstance();
	private static BossService bossservice = BossService.getInstance();
	
	private void start() {
		int view = View.MAIN;
		
		while(true) {
			switch(view) {
			case View.MAIN: view = main(); break;
			
			case View.JOIN: view = userservice.join(); break;
			
			case View.MEMBER_LOGIN: view = userservice.login(); break;
			case View.OWNER_LOGIN: view = bossservice.loginOwner(); break;
			
			case View.MEMBER_HOME: view = homeservice.home(); break;
			case View.OWNER_HOME: view = bossservice.pageOwner(); break;
			
			case View.MENU_LIST: view = menuservice.listMenu(); break;
			case View.MENU_READ: view = menuservice.read(); break;
			case View.MENU_INSERT: view = menuservice.insertMenu(); break;
	         case View.MENU_UPDATE: view = menuservice.updateMenu(); break;
	         case View.MENU_DELETE: view = menuservice.deleteMenu(); break;
			
			case View.REVIEW_READ: view = reviewservice.read(); break;
			case View.REVIEW_INSERT: view = reviewservice.insertReview(); break;
			
			case View.OWNER_CMT_INSERT: view = reviewservice.insertCmt(); break;
			
			case View.ORDER: view = orderservice.order(); break;
			case View.ORDER_READ: view = orderservice.cart(); break;
			case View.ORDER_INSERT: view = orderservice.cartInsert(); break;
	        case View.ORDER_UPDATE: view = orderservice.cartUpdate(); break;
	        case View.ORDER_DELETE: view = orderservice.cartDelete(); break;     
	        case View.PAYMENT: view = orderservice.payment(); break;
			
	        case View.COST_LIST: view = menuservice.listCmt(); break;
			
			case View.MYPAGE: view = mypageservice.mypage(); break;
	        case View.MYORDER_LIST: view = mypageservice.myOrderList(); break;
	        case View.MYORDER_READ: view = mypageservice.myOrderRead(); break;         
	        case View.MYPAGE_UPDATE: view = mypageservice.myUpdate(); break;
	        case View.MYPAGE_NAME_UPDATE: view = mypageservice.nameUpdate(); break;
	        case View.MYPAGE_ADD_UPDATE: view = mypageservice.addUpdate(); break;
	        case View.MYPAGE_DELETE: view = mypageservice.myDelete(); break;
	        case View.MYREVIEW_LIST: view = reviewservice.myReviewRead(); break;
	        case View.MYREVIEW_DELETE: view = reviewservice.myReviewDelete(); break;
			
	        case View.OWNER_ORDER_LIST: view = menuservice.listOwnerOrder(); break;
	        case View.OWNER_REVIEW_LIST: view = reviewservice.listOwnerReview(); break;
	        
			}
		}	
	}
	
	private int main() {
		System.out.print("1.회원로그인 2.점주 로그인 3.메뉴보기 4.마이페이지 5.회원가입 0.프로그램 종료\n>");
		int input = ScanUtil.nextInt();
		switch(input) {
		case 1: return View.MEMBER_LOGIN;
		case 2: return View.OWNER_LOGIN;
		case 3: return View.MENU_READ;
		case 4:
			System.out.println("로그인을 하신 후에 이용할 수 있는 서비스 입니다.");
			return View.MAIN;
		case 5: return View.JOIN;
		case 0: 
			System.out.println("프로그램이 종료되었습니다.");
			System.exit(0);
		}
		return View.MAIN;
	}

}
