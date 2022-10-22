package service;

import java.util.Map;

import dao.BossDao;
import dao.MenuDao;
import util.ScanUtil;
import util.View;

public class BossService {
	
	public Map<String, Object> loginOwner;
    private BossService() {
		
	}
	private static BossService instance; //객체 보관 메서드
	public static BossService getInstance() { //객체 빌려주는 메서드
		if(instance == null) {
			instance = new BossService();
		}
		return instance;
	}
	
	//점주 로그인 후 관리 페이지
	private static BossDao bossDao = BossDao.getInstance();


	public int loginOwner() {
		System.out.println("=============로그인=============");
		System.out.print("아이디>");
		String ownerId = ScanUtil.nextLine();
		System.out.print("비밀번호>");
		String ownerPw = ScanUtil.nextLine();

		Map<String, Object> owner = bossDao.selectLoginOwner(ownerId, ownerPw);

		if (owner == null) {
			System.out.println("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
		} else {
			System.out.println("로그인 성공");

			loginOwner = owner;

			return View.OWNER_HOME;
		}
	
	return View.OWNER_LOGIN;
}
	
	private static MenuDao menuDao = MenuDao.getInstance();
	
	public int pageOwner() {
		System.out.print("1.메뉴관리 2.주문관리 3.리뷰관리 4.매출관리 0.로그아웃>");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			if(this.loginOwner.get("OWNER_ID").equals("tjdnf001")) {
	            return View.MENU_LIST;
	         }else{
	            System.out.println("메뉴 접근 권한이 없습니다.");
	         };
			return View.OWNER_HOME;
		case 2:
			return View.OWNER_ORDER_LIST;
		case 3:
			return View.OWNER_REVIEW_LIST;
		case 4:
			return View.COST_LIST;
		case 0:
			return View.MAIN;
		}
		return View.OWNER_LOGIN;
	}
    
    
}
