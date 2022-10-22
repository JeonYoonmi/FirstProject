package service;

import java.util.List;
import java.util.Map;

import dao.MenuDao;
import util.View;

import util.PrintBlankUtil;
import util.ScanUtil;

public class MenuService {
public MenuService() {
		
	}

//객체를 보관해놓을 변수
	private static MenuService instance;

//객체를 빌려주는 메소드
//다른 클래스에서 이 객체가 필요할 때에는 getInstance를 불러서 객체를 생성한다.
	public static MenuService getInstance() {
		if (instance == null) {
			instance = new MenuService();
		}
		return instance;
	}
	
	private static BossService bossservice = BossService.getInstance(); 
	private static MenuDao menudao = MenuDao.getInstance();

	public int read() {
		System.out.println("=====================================================================메뉴판=============================================================================");
		//메인메뉴 보기
		List<Map<String, Object>> mainMenuList = menudao.mainRead();
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("\t\t\t\t\t\t\t메인메뉴");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("메뉴번호  메뉴이름            메뉴가격            설명");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");

		for (Map<String, Object> mainMenu : mainMenuList) {
			System.out.print(PrintBlankUtil.printBlank(String.valueOf(mainMenu.get("MENU_NO")), 10));
			System.out.print(PrintBlankUtil.printBlank((String) mainMenu.get("MENU_NM"), 20));
			System.out.print(PrintBlankUtil.printBlank(((String) mainMenu.get("MENU_PRICE") + "원" ), 20));
			if(mainMenu.get("MENU_EX") == null) {
				
			}else {				
				System.out.print(PrintBlankUtil.printBlank((String) mainMenu.get("MENU_EX"), 100));
			}
			System.out.println();
		}
		
		//세트메뉴 보기
		List<Map<String, Object>> setMenuList = menudao.setRead();
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("\t\t\t\t\t\t\t세트메뉴");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("메뉴번호  메뉴이름            메뉴가격            설명");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
		for (Map<String, Object> setMenu : setMenuList) {
			System.out.print(PrintBlankUtil.printBlank(String.valueOf(setMenu.get("MENU_NO")), 10));
			System.out.print(PrintBlankUtil.printBlank((String) setMenu.get("MENU_NM"), 20));
			System.out.print(PrintBlankUtil.printBlank(((String) setMenu.get("MENU_PRICE") + "원" ), 20));
			if(setMenu.get("MENU_EX") == null) {
				
			}else {
				System.out.print(PrintBlankUtil.printBlank((String) setMenu.get("MENU_EX"), 100));
			}
			System.out.println();
		}
		
		//토핑메뉴 보기
		List<Map<String, Object>> topMenuList = menudao.topRead();
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("\t\t\t\t\t\t\t토핑");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("메뉴번호  메뉴이름            메뉴가격            설명");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
		for (Map<String, Object> topMenu : topMenuList) {
			System.out.print(PrintBlankUtil.printBlank(String.valueOf(topMenu.get("MENU_NO")), 10));
			System.out.print(PrintBlankUtil.printBlank((String) topMenu.get("MENU_NM"), 20));
			System.out.print(PrintBlankUtil.printBlank(((String) topMenu.get("MENU_PRICE") + "원" ), 20));
			if(topMenu.get("MENU_EX") == null) {
				
			}else {
				System.out.print(PrintBlankUtil.printBlank((String) topMenu.get("MENU_EX"), 100));
			}
			System.out.println();
		}
		
		//사이드메뉴 보기
		List<Map<String, Object>> sideMenuList = menudao.sideRead();
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("\t\t\t\t\t\t\t사이드메뉴");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("메뉴번호  메뉴이름            메뉴가격            설명");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
		for (Map<String, Object> sideMenu : sideMenuList) {
			System.out.print(PrintBlankUtil.printBlank(String.valueOf(sideMenu.get("MENU_NO")), 10));
			System.out.print(PrintBlankUtil.printBlank((String) sideMenu.get("MENU_NM"), 20));
			System.out.print(PrintBlankUtil.printBlank(((String) sideMenu.get("MENU_PRICE") + "원" ), 20));
			if(sideMenu.get("MENU_EX") == null) {
				
			}else {
				System.out.print(PrintBlankUtil.printBlank((String) sideMenu.get("MENU_EX"), 100));
			}
			System.out.println();
		}
		System.out.println("========================================================================================================================================================");
		if(UserService.loginMember == null) {
			return View.MAIN;
		}else {
			return View.ORDER_INSERT;			
		}
	}
	
	//메뉴 수정, 등록, 삭제
	public int listMenu() {

		List<Map<String, Object>> listMenu = menudao.selectListMenu();

		System.out.println("=====================================");
		System.out.println("번호\t메뉴명\t가격\t설명");
		System.out.println("--------------------------------------");
		for (Map<String, Object> menu : listMenu) {
			System.out.print(menu.get("MENU_NO") + "\t");
			System.out.print(menu.get("MENU_NM") + "\t");
			System.out.print(menu.get("MENU_PRICE") + "\t");
			System.out.print(menu.get("MENU_EX") + "\n");
		}
		System.out.println("=====================================");

		System.out.print("1.메뉴수정  2.메뉴등록 3.메뉴삭제 0.점주 페이지>");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			return View.MENU_UPDATE;
		case 2:
			return View.MENU_INSERT;
		case 3:
			return View.MENU_DELETE;
		case 0:
			return View.OWNER_HOME;
		}
		return View.MENU_LIST;
	}

	// 메뉴수정
	public int updateMenu() {
		System.out.print("수정할 메뉴 번호 >");
		int menuNo = ScanUtil.nextInt();

		System.out.print("가격>");
		String price = ScanUtil.nextLine();
		System.out.print("설명>");
		String ex = ScanUtil.nextLine();

		int result = menudao.upMenu(price, ex, menuNo);

		if (0 < result) {
			System.out.println("메뉴가 수정되었습니다.");
		} else {
			System.out.println("메뉴 수정에 실패하였습니다.");
		}

		return View.MENU_LIST;
	}

	// 메뉴등록
	public int insertMenu() {
		System.out.print("메뉴코드>");
		String menuCode = ScanUtil.nextLine();
		System.out.print("메뉴명>");
		String name = ScanUtil.nextLine();
		System.out.print("가격>");
		String price = ScanUtil.nextLine();
		System.out.print("분류>");
		String menuLgu = ScanUtil.nextLine();
		System.out.print("설명>");
		String ex = ScanUtil.nextLine();

		int result = menudao.insMenu(menuCode, name, price, menuLgu, ex);

		if (0 < result) {
			System.out.println("신메뉴가 등록되었습니다.");
		} else {
			System.out.println("메뉴 등록에 실패하였습니다.");
		}
		return View.MENU_LIST;
	}

	// 메뉴삭제
	public int deleteMenu() {
		System.out.print("삭제할 메뉴 번호>");
		int menuNo = ScanUtil.nextInt();

		System.out.print("정말 삭제하시겠습니까?");
		if (ScanUtil.nextLine().equals("y")) {

			int result = menudao.delMenu(menuNo);

			if (0 < result) {
				System.out.println("메뉴를 더 이상 판매하지 않습니다.");
			} else {
				System.out.println("메뉴 삭제 실패하였습니다.");
			}
		}
		return View.MENU_LIST;
	}

	 // 주문 목록보기
	   public int listOwnerOrder() {

	      Map<String, Object> ownerId = bossservice.loginOwner;
	      List<Map<String, Object>> listOwnerOrder = menudao.selectListOwnerOrder((String) ownerId.get("OWNER_ID"));

	      System.out.println("=======================================================");
	      System.out.println("                          주문서                               ");
	      System.out.println("-------------------------------------------------------");

	      if (ownerId.get("ORD_NO") == ownerId.get("ORD_NO")) {
	         System.out.println("주문번호\t주문자\t메뉴\t수량\t결제상태\t주문날짜\t가격");
	         System.out.println("-------------------------------------------------------");
	      for (Map<String, Object> ownerOrder : listOwnerOrder) {
	            System.out.print(ownerOrder.get("ORD_NO") + "\t");
	            System.out.print(ownerOrder.get("MEM_ID") + "\t");
	            System.out.print(ownerOrder.get("MENU_NM") + "\t");
	            System.out.print(ownerOrder.get("MENU_QTY") + "\t");
	            System.out.print(ownerOrder.get("GUBUN") + "\t");
	            System.out.print(ownerOrder.get("ORD_DATE") + "\t");
	            System.out.print(ownerOrder.get("MENU_PRICE") + "원" + "\n");
	            System.out.println("-------------------------------------------------------");
	         }

	      }

	      System.out.println("=======================================================");

	      return View.OWNER_HOME;

	   }

	// 매출조회
	public int listCmt() {

		Map<String, Object> ownerId = bossservice.loginOwner;
		List<Map<String, Object>> listCmt = menudao.selectCostList((String) ownerId.get("OWNER_ID"));

		System.out.println("==================================");
		System.out.println("                매출                              ");
		System.out.println("----------------------------------");
		for (Map<String, Object> cmt : listCmt) {
			System.out.print(cmt.get("STORE_NAME") + "\t\t");
			System.out.print(cmt.get("ORD_TOT") + "원" + "\n");
		}
		System.out.println("==================================");

		return View.OWNER_HOME;
	}

}
