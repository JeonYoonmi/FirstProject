package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class ReviewDao {
	public ReviewDao() {

	}

	private static ReviewDao instance;

	public static ReviewDao getInstance() {
		if (instance == null) {
			instance = new ReviewDao();
		}
		return instance;
	}
	
	//지점별 리뷰조회(회원)
	public List<Map<String, Object>> readReviewMember(Object storeNm) {
		String sql = "SELECT B.REV_NO REV_NO" + 
				"    , (SELECT NVL(MEM_NM, ' ') FROM MEMBER WHERE MEM_ID = B.MEM_ID) MEM_NM" + 
				"    , B.REV_SCORE" + 
				"    , B.REV_CONTENT" + 
				"    , TO_CHAR(B.REV_DATE, 'MM/DD') REV_DATE" + 
				" FROM ORD A" + 
				"    RIGHT OUTER JOIN REVIEW B ON B.ORD_NO = A.ORD_NO AND NOT B.REV_CONTENT IS NULL" + 
				" WHERE STORE_CD = (SELECT STORE_CD " + 
				"                    FROM STORE " + 
				"                    WHERE STORE_NAME = ?)"
				+ " ORDER BY TO_CHAR(B.REV_DATE, 'MM/DD') DESC";
		
		List<Object> param = new ArrayList<Object>();
		param.add(storeNm);
		
		return JDBCUtil.selectList(sql, param);
	}

	//지점별 댓글조회(회원)
	public Map<String, Object> readComentMember(Object revNo) {
		String sql ="SELECT CMT_CONTENT || '     (' || CMT_DATE || ')' COMENT" + 
				" FROM COMENT" + 
				" WHERE REV_NO = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(revNo);
		
		return JDBCUtil.selectOne(sql, param);
	}
	
	//회원의 리뷰 불러오기
	public List<Map<String, Object>> selectmyReviewList(Object memId) {
		String sql = "SELECT REV_NO" + 
				"    , (SELECT MEM_NM FROM MEMBER WHERE MEM_ID = ?) MEM_NM" + 
				"    , REV_SCORE" + 
				"    , REV_CONTENT" + 
				"    , TO_CHAR(REV_DATE, 'MM/DD') REV_DATE" + 
				" FROM REVIEW " + 
				" WHERE MEM_ID = ?" + 
				" ORDER BY REV_DATE DESC";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		param.add(memId);
		
		return JDBCUtil.selectList(sql, param);
	}
	
	//(회원) 리뷰작성
	public int insertReview(String memId, int myOrderNo, String content, String star) {
		String sql = "INSERT INTO REVIEW" + 
				" VALUES((SELECT NVL(MAX(REV_NO), 0) + 1 FROM REVIEW)" + 
				"        , ?" + 
				"        , ?" +
				"        , ?" + 
				"        , SYSDATE" +  
				"        , ?" + 
				")";
		
		List<Object> param = new ArrayList<Object>();
		param.add(memId);
		param.add(myOrderNo);
		param.add(content);
		param.add(star);
		
		return JDBCUtil.update(sql, param);
	}
	
	//리뷰수정
//	public int myReviewUpdate(String star, String reviewContent, int reviewNo) {
//		String sql = "update review" + 
//				" set rev_score = ?, rev_content = ?" + 
//				" where rev_no = ?";
//		
//		List<Object> param = new ArrayList<Object>();
//		param.add(star);
//		param.add(reviewContent);
//		param.add(reviewNo);
//		
//		return JDBCUtil.update(sql, param);
//	}
	
	//회원의 리뷰 삭제
	public int myReviewDelete(int reviewNo) {
		String sql = "DELETE FROM REVIEW" + 
				" WHERE REV_NO = ?";
		
		List<Object> param = new ArrayList<Object>();
		param.add(reviewNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	//회원의 리뷰 삭제에 대한 점장의 댓글 삭제	
	public int cmtDelete(int reviewNo) {
		String sql = "DELETE FROM COMENT" + 
				" WHERE CMT_NO = (SELECT CMT_NO FROM COMENT WHERE REV_NO = ?)";
		
		List<Object> param = new ArrayList<Object>();
		param.add(reviewNo);
		
		return JDBCUtil.update(sql, param);
	}
	
	//리뷰조회(점장)
		public List<Map<String, Object>> selectListReview(String ownerId) {
			String sql = "SELECT R.REV_NO"
					 + ", R.REV_SCORE"
					 + ", R.REV_CONTENT"
					 + ", TO_CHAR(R.REV_DATE, 'MM/DD') AS REV_DATE"
					 + ", OW.OWNER_ID"
					 + ", M.MEM_NM"
					 + " FROM REVIEW R, STORE S, ORD O, OWNER OW, MEMBER M"
					 + " WHERE S.STORE_CD = O.STORE_CD"
					 + " AND O.ORD_NO(+) = R.ORD_NO"
					 + " AND S.OWNER_ID = OW.OWNER_ID"
					 + " AND O.MEM_ID = M.MEM_ID"
					 + " AND OW.OWNER_ID = ?"
					 + " ORDER BY R.REV_NO";
			
			List<Object> param = new  ArrayList<Object>();
			param.add(ownerId);
		    
			return JDBCUtil.selectList(sql, param);
		}
		
		//댓글조회(점장)
		public List<Map<String, Object>> selectListComent(String ownerId) {
			String sql = "SELECT C.CMT_NO"
	                   + " , R.REV_NO"
	                   + ", C.CMT_CONTENT"
	                   + ", TO_CHAR(C.CMT_DATE, 'MM/DD') AS CMT_DATE"
	                   + ",  OW.OWNER_ID"
	                   + ", M.MEM_NM"
	                   + " FROM COMENT C, REVIEW R, ORD O, STORE S, OWNER OW, MEMBER M"
	                   + " WHERE C.REV_NO = R.REV_NO"
	                   + " AND O.STORE_CD = S.STORE_CD"
	                   + " AND O.ORD_NO = R.ORD_NO(+)"
	                   + " AND S.OWNER_ID = OW.OWNER_ID"
	                   + " AND M.MEM_ID = R.MEM_ID"
	                   + " AND OW.OWNER_ID = ?"
	                   + " ORDER BY R.REV_NO";

			List<Object> param = new  ArrayList<Object>();
			param.add(ownerId);
		    
			return JDBCUtil.selectList(sql, param);
		}
			
		//댓글남기기(점장)
		public int insCmt(int reviewNo, String content) {
			String sql = "INSERT INTO COMENT"
					+ " VALUES((SELECT NVL(MAX(CMT_NO), 0) + 1 FROM COMENT) "
					+ "        , (SELECT REV_NO FROM REVIEW WHERE REV_NO = ?)"
					+ "       , ? , SYSDATE)";

			List<Object> param = new  ArrayList<Object>();
			param.add(reviewNo);
			param.add(content);
		    
			return JDBCUtil.update(sql, param);
		}

		public List<Map<String, Object>> checkReview() {
			String sql = "SELECT ORD_NO FROM REVIEW";
			
			return JDBCUtil.selectList(sql);
		}
}
