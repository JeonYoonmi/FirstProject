package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class MypageDao {
	//싱글톤 패턴 : (디자인패턴 중 하나)객체 생성을 막는 패턴 
	//private을 붙여 생성자를 호출하지 못하게함 -> 객체 생성도 불가
	private MypageDao() {
		
	}
	private static MypageDao instance; //instance : 객체를 보관하는 변수
	public static MypageDao getInstance() { //getInstance() : 객체를 빌려주는 변수
		if(instance == null) {
			instance = new MypageDao();
		}
		return instance;
	}


//마이페이지 주문서 리스트
	public List<Map<String, Object>> selectMyOrderList(Object memId) {
		String sql = "SELECT O.ORD_NO ORD_NO" + 
				"    ,  S.STORE_NAME STORE_NAME " + 
				"    ,  P.GUBUN GUBUN" + 
				"    ,  TO_CHAR(O.ORD_DATE, 'MM/DD') ORD_DATE" + 
				"    ,  O.ORD_TOT ORD_TOT" + 
				" FROM   ORD O, PAYMENT_STATE P, ORD_MENU OM, STORE S" + 
				" WHERE  P.PAY_NO = O.PAY_NO" + 
				" AND O.ORD_NO = OM.ORD_NO" + 
				" AND S.STORE_CD = O.STORE_CD" + 
				" AND O.MEM_ID = ?" + 
				" AND P.PAY_NO IN (1,2) " + 
				" GROUP BY O.ORD_NO, S.STORE_NAME, P.GUBUN, TO_CHAR(O.ORD_DATE, 'MM/DD'),O.ORD_TOT"
				+ " ORDER BY TO_CHAR(O.ORD_DATE, 'MM/DD') DESC";	
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		
		return JDBCUtil.selectList(sql, param);
	}

	//마이페이지 주문서 조회
	public Map<String, Object> selectMyOrderRead(Object ordNo) {
		String sql = "SELECT ORD_NO"
				+ "   	,	 TO_CHAR(ORD_DATE, 'MM/DD') ORD_DATE"
				+ "		,	 ORD_TOT"
				+ "   FROM   ORD"
				+ "   WHERE  ORD_NO = ?"
				+ "   ORDER BY ORD_NO DESC";
		List<Object> param = new ArrayList<Object>();
		param.add(ordNo);		
		return JDBCUtil.selectOne(sql, param);
	}

//마이페이지 주문서 메뉴
	public List<Map<String, Object>> selectMyMenuRead(int orderNo) {
		String sql = "SELECT MENU_NM"
				+ "			, MENU_QTY"
				+ "	  FROM ORD_MENU"
				+ "   WHERE ORD_NO = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(orderNo);
		
		return JDBCUtil.selectList(sql, param);
	}


//마이페이지 닉네임 변경
	public int selectMyNameUpdate(String memNm,Object memId) {
		String sql = "UPDATE MEMBER"
				+ "	  SET MEM_NM = ?"
				+ "   WHERE MEM_ID = ?";
		List<Object> param = new ArrayList<Object>();
		param.add(memNm);
		param.add(memId);
				
		return JDBCUtil.update(sql, param);
	}

//마이페이지 주소지 변경
	public int selectMyAddUpdate(String memAdd, Object memId) {
		String sql = "UPDATE MEMBER"
				+ "	  SET MEM_ADD = ?"
				+ "   WHERE MEM_ID = ?";
		List<Object> param = new ArrayList<Object>();
		param.add(memAdd);
		param.add(memId);
		
		return JDBCUtil.update(sql, param);
	}

//회원탈퇴
	public int selectMyDelete(Object memId, Object memPw) {
		String sql = "DELETE FROM MEMBER"
				+ "   WHERE MEM_ID = ?"
				+ "   AND MEM_PW = ?";
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		param.add(memPw);
		return JDBCUtil.update(sql, param);
	}

}
