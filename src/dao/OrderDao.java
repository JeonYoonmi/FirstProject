package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class OrderDao {
	//싱글톤 패턴 : (디자인패턴 중 하나)객체 생성을 막는 패턴 
		//private을 붙여 생성자를 호출하지 못하게함 -> 객체 생성도 불가
		private OrderDao() {
			
		}
		private static OrderDao instance; //instance : 객체를 보관하는 변수
		public static OrderDao getInstance() { //getInstance() : 객체를 빌려주는 변수
			if(instance == null) {
				instance = new OrderDao();
			}
			return instance;
		}
		
		//주문서번호만들기
		public Map<String, Object> selectOrderNo() {
			String sql = "SELECT NVL(MAX(ORD_NO), 0)+ 1 ORDER_NO FROM ORD";
			return JDBCUtil.selectOne(sql);
		}
		
		
		//ORD테이블에 insert하기(부모테이블이므로 먼저 값이 들어가야함)
		public int selectOrderInsert(int ordNo, String memId, String storeNm) {
			String sql = "INSERT INTO ORD(ORD_NO, MEM_ID, STORE_CD, PAY_NO, ORD_DATE, ORD_TOT)"
					+ "   VALUES( ?"
					+ " , ? ,(SELECT STORE_CD FROM STORE WHERE STORE_NAME = ?) , 4, SYSDATE ,NULL)"; //4 : 결제대기상태
			List<Object> param = new ArrayList<Object>();
			param.add(ordNo);
			param.add(memId);
			param.add(storeNm);
			return JDBCUtil.update(sql, param);
		}
		
		//ord_menu테이블에 insert하기
		public int selectOrderMenuInsert(int ordNo, int menuNo, int menuQty) {
			String sql = "INSERT INTO ORD_MENU(ORD_NO, MENU_CD, MENU_QTY, MENU_NM, MENU_PRICE)" 
					+ "SELECT ?, MENU_CD, ?, MENU_NM, MENU_PRICE FROM MENU WHERE MENU_NO = ?";
			
			List<Object> param = new ArrayList<Object>();
			param.add(ordNo);
			param.add(menuQty);
			param.add(menuNo);
			return JDBCUtil.update(sql, param);
		}

		//ord에 주문일 총금액 insert
		public int selectOrdDateTot(int ordNo) {
			String sql = "UPDATE ORD" 
					+ "   SET    ORD_DATE = SYSDATE" 
					+ "        , ORD_TOT = ("
					+ "          SELECT SUM(MENU_PRICE * MENU_QTY)"
					+ "          FROM   ORD_MENU"
					+ "          WHERE  ORD_NO = ?"
					+ "        )"
					+ "   WHERE  ORD_NO = ?";
			List<Object> param = new ArrayList<Object>();
			param.add(ordNo);
			param.add(ordNo);
			return JDBCUtil.update(sql, param);
		}

		//장바구니조회
		public Map<String, Object> selectMyCart(String storeNm, String memId) {
			String sql = "SELECT ? STORE_NAME"
				    + "  ,  ORD_TOT"
				    + " FROM    ORD"
				    + " WHERE   MEM_ID = ?"
				    + " AND     PAY_NO = 4";
			List<Object> param = new ArrayList<Object>();
			param.add(storeNm);
			param.add(memId);
			return JDBCUtil.selectOne(sql, param);
		}

		//장바구니 메뉴조회
		public List<Map<String, Object>> selectCartMenuList(String memId) {
			String sql = "SELECT OM.MENU_NM"
					+ "     ,    OM.MENU_QTY "
					+ "		, OM.MENU_QTY * OM.MENU_PRICE SUM"
					+ "		,	(SELECT MENU_NO FROM MENU WHERE MENU_NM = OM.MENU_NM)"
					+ "   FROM ORD_MENU OM"
					+ "   WHERE ORD_NO = (SELECT ORD_NO FROM ORD WHERE PAY_NO = 4 AND MEM_ID = ?)"
					+ " ORDER BY 3";
			List<Object> param = new ArrayList<Object>();
			param.add(memId);
			return JDBCUtil.selectList(sql, param);
		}

		//결제하기
		public int selectpayment(int payNo, String memNm) {
			String sql = "UPDATE ORD"
					+ "   SET    PAY_NO = ?"
					+ "   WHERE  MEM_ID = ?"
					+ "   AND 	 PAY_NO = 4";
			List<Object> param = new ArrayList<Object>();
			param.add(payNo);
			param.add(memNm);
			return JDBCUtil.update(sql, param);
		}

		//장바구니 메뉴삭제
		public int selectCartDelete(int ordNo, String menuNm) {
			String sql = "DELETE FROM ORD_MENU"
					+ "   WHERE  ORD_NO = ?"
					+ "   AND    MENU_NM = ?";
			List<Object> param = new ArrayList<Object>();
			param.add(ordNo);
			param.add(menuNm);
			
			return JDBCUtil.update(sql, param);
		}

		//주문서번호받기
		public Map<String, Object> selectCartOrdNo(String memId) {
			String sql = "SELECT ORD_NO"
					+ "   FROM ORD"
					+ "   WHERE PAY_NO = 4"
					+ "   AND MEM_ID = ?";
			List<Object> param = new ArrayList<Object>();
			param.add(memId);
			return JDBCUtil.selectOne(sql, param);
		}

		//장바구니 메뉴수량 수정
		public int selectCartUpdate(int menuQty, int ordNo, String menuNm) {
			String sql = "UPDATE ORD_MENU"
					+ "   SET MENU_QTY = ?"
					+ "   WHERE ORD_NO = ?"
					+ "   AND MENU_NM = ?";
			List<Object> param = new ArrayList<Object>();
			param.add(menuQty);
			param.add(ordNo);
			param.add(menuNm);
			
			return JDBCUtil.update(sql, param);
		}

		//장바구니 수량 수정 후 총금액 update
		public int updateTot(int ordNo) {
			String sql = "UPDATE ORD" + 
					" SET ORD_TOT = (SELECT NVL(sum(MENU_QTY * MENU_PRICE),0) FROM ORD_MENU WHERE ORD_NO = ?)" + 
					" WHERE ORD_NO = ?";
			List<Object> param = new ArrayList<Object>();
			param.add(ordNo);
			param.add(ordNo);
			
			return JDBCUtil.update(sql, param);
		}
}