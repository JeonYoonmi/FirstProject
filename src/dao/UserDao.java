package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class UserDao {
	
	public UserDao() {

	}

	private static UserDao instance;

	public static UserDao getInstance() {
		if (instance == null) {
			instance = new UserDao();
		}
		return instance;
	}

	public int insertMember(String memId, String memPw, String memName, String memBir, String memPh, String memAdd) {
		String sql = "INSERT INTO MEMBER VALUES(?, ?, ?, ?, ?, ?)";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		param.add(memName);
		param.add(memPw);
		param.add(memBir);
		param.add(memPh);
		param.add(memAdd);
		
		return JDBCUtil.update(sql, param);
	}

	public Map<String, Object> loginMember(String memId, String memPw) {
		String sql = "SELECT MEM_ID, MEM_PW, MEM_NM"
				+ " FROM MEMBER"
				+ " WHERE MEM_ID = ?"
				+ " AND MEM_PW = ?";

		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		param.add(memPw);
		
		return JDBCUtil.selectOne(sql, param);
	}
	
	public Map<String, Object> matchingStore(Object memId) {
		String sql = "SELECT  MEM_NM ||' 님과 가까운 매장은 ' "
				+ "|| (SELECT STORE_NAME FROM STORE WHERE SUBSTR(M.MEM_ADD,1,6) = SUBSTR(STORE_ADD,1,6))"
				+ "|| ' 입니다.' RESULT"
				+ " , (SELECT STORE_NAME FROM STORE WHERE SUBSTR(M.MEM_ADD,1,6) = SUBSTR(STORE_ADD,1,6)) STORE_NAME"
				+ " FROM MEMBER M"
				+ " WHERE MEM_ID = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		
		return JDBCUtil.selectOne(sql, param);
	}
	
}
