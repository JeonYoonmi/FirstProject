package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import service.UserService;
import util.JDBCUtil;

public class MenuDao {
	
	public MenuDao() {
		
	}

//객체를 보관해놓을 변수
	private static MenuDao instance;

//객체를 빌려주는 메소드
//다른 클래스에서 이 객체가 필요할 때에는 getInstance를 불러서 객체를 생성한다.
	public static MenuDao getInstance() {
		if (instance == null) {
			instance = new MenuDao();
		}
		return instance;
}
		
	//메인메뉴 조회
	public static List<Map<String, Object>> mainRead() {
		String sql = "SELECT MENU_NO"
				+ ", MENU_NM"
				+ ", MENU_PRICE"
				+ ", MENU_EX"
				+ " FROM MENU"
				+ " WHERE MENU_LGU = '메인메뉴'"
				+ " ORDER BY MENU_CD";

		return  JDBCUtil.selectList(sql);
	}

	//사이드 메뉴 조회
	public static List<Map<String, Object>> sideRead() {
		String sql = "SELECT MENU_NO"
				+ ", MENU_NM"
				+ ", MENU_PRICE"
				+ ", MENU_EX"
				+ " FROM MENU"
				+ " WHERE MENU_LGU = '사이드'"
				+ " ORDER BY MENU_CD";

		return  JDBCUtil.selectList(sql);
	}

	//세트메뉴 조회
	public static List<Map<String, Object>> setRead() {
		String sql = "SELECT MENU_NO"
				+ ", MENU_NM"
				+ ", MENU_PRICE"
				+ ", MENU_EX"
				+ " FROM MENU"
				+ " WHERE MENU_LGU = '세트메뉴'"
				+ " ORDER BY MENU_CD";

		return  JDBCUtil.selectList(sql);
	}

	
	//토핑메뉴 조회
	public static List<Map<String, Object>> topRead() {
		String sql = "SELECT MENU_NO"
				+ ", MENU_NM"
				+ ", MENU_PRICE"
				+ ", MENU_EX"
				+ " FROM MENU"
				+ " WHERE MENU_LGU = '토핑'"
				+ " ORDER BY MENU_CD";

		return  JDBCUtil.selectList(sql);
	}

	//전체 메뉴 조회
		public List<Map<String, Object>> selectListMenu() {
			String sql = "SELECT MENU_NO"
				   + " , MENU_NM"
				   + " , MENU_PRICE"
				   + " , NVL(MENU_EX, ' ') MENU_EX"
				   + " FROM MENU "
				   + " ORDER BY MENU_NO";
			return JDBCUtil.selectList(sql);
		}
		
		//전체 메뉴 수정
		public int upMenu(String price, String ex, int menuNo) {
			String sql = "UPDATE MENU"
					+ " SET MENU_PRICE = ?"
					+ "	 , MENU_EX = ?"
					+ "	WHERE MENU_NO = ?";
			
			List<Object> param = new  ArrayList<Object>();
		    param.add(price);
		    param.add(ex);
		    param.add(menuNo);
		    
		    return JDBCUtil.update(sql, param);
		}
		
		//전체 메뉴 등록
		public int insMenu(String menuCode, String name, String price, String menuLgu, String ex) {
			String sql = "INSERT INTO MENU"
					   + " VALUES ("
					   + "    ?, ?, ?, ?, ?    "
					   + "    ,(SELECT NVL(MAX(MENU_NO), 0) + 1 FROM MENU))";
			List<Object> param = new ArrayList<Object>();
			param.add(menuCode);
			param.add(name);
			param.add(price);
			param.add(menuLgu);
			param.add(ex);
			
			return JDBCUtil.update(sql, param);
		}
		
		//전체 메뉴 삭제
		public int delMenu(int menuNo){
			String sql = "DELETE FROM MENU" 
		               + "  WHERE MENU_NO = ?";
			List<Object> param = new ArrayList<Object>();
			param.add(menuNo);
			
			return JDBCUtil.update(sql, param);
		}
		
		//주문서조회
	      public List<Map<String, Object>> selectListOwnerOrder(String ownerId) {
	         String sql = "SELECT O.ORD_NO "
	                  + " , O.MEM_ID"
	                  + " , OM.MENU_NM" 
	                  + " , OM.MENU_QTY" 
	                  + " , P.GUBUN"  
	                  + " , TO_CHAR(O.ORD_DATE, 'MM/DD') AS ORD_DATE"  
	                  + " , O.ORD_TOT"
	                  + " , M.MENU_PRICE"
	                  + " FROM ORD O, PAYMENT_STATE P, ORD_MENU OM, OWNER OW, STORE S, MENU M"
	                  + " WHERE O.PAY_NO = P.PAY_NO"
	                  + "   AND S.OWNER_ID = OW.OWNER_ID"
	                  + " AND M.MENU_CD = OM.MENU_CD"
	                  + " AND O.ORD_NO = OM.ORD_NO"
	                  + " AND S.STORE_CD = O.STORE_CD"
	                  + " AND OW.OWNER_ID = ?";
	         
	         List<Object> param = new ArrayList<Object>();
	         param.add(ownerId);

	         return JDBCUtil.selectList(sql,param);
	      }
		
		//매출조회
		public List<Map<String, Object>> selectCostList(String ownerId) {
			String sql = "SELECT S.STORE_NAME"
				    + ", NVL(SUM(O.ORD_TOT),0) ORD_TOT "
				    + ", OW.OWNER_ID"
				    + " FROM STORE S, ORD O, OWNER OW"
				    + " WHERE S.STORE_CD = O.STORE_CD"
				    + " AND S.OWNER_ID = OW.OWNER_ID"
				    + " AND OW.OWNER_ID = ?"
				    + " GROUP BY S.STORE_NAME, OW.OWNER_ID";
			
			List<Object> param = new ArrayList<Object>();
			param.add(ownerId);

			return JDBCUtil.selectList(sql,param);
		}
	
}
