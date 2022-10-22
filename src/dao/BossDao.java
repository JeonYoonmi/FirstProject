package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class BossDao {
     
	private BossDao() {
		
	}

	private static BossDao instance; // 객체 보관 메서드

	public static BossDao getInstance() { // 객체 빌려주는 메서드                                                   
		if (instance == null) {
			instance = new BossDao();
		}
		return instance;
	}

	//지점 로그인 코드
	public Map<String, Object> selectLoginOwner(String ownerId, String ownerPw) {
		String sql = "SELECT O.OWNER_ID, O.OWNER_PW"
				+" FROM OWNER O, STORE S"
				+" WHERE S.OWNER_ID = O.OWNER_ID"
				+" AND O.OWNER_ID = ?"                                      
				+" AND O.OWNER_PW = ?";
		List<Object> param = new ArrayList<Object>();
		param.add(ownerId);
		param.add(ownerPw);
		
		return JDBCUtil.selectOne(sql, param);
	}

	

	
}
