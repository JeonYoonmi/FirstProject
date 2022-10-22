package service;

import java.util.List;
import java.util.Map;

import dao.MypageDao;
import dao.ReviewDao;
import util.PrintBlankUtil;
import util.ScanUtil;
import util.View;

public class ReviewService {

	public ReviewService() {

	}

//객체를 보관해놓을 변수
	private static ReviewService instance;

//객체를 빌려주는 메소드
//다른 클래스에서 이 객체가 필요할 때에는 getInstance를 불러서 객체를 생성한다.
	public static ReviewService getInstance() {
		if (instance == null) {
			instance = new ReviewService();
		}
		return instance;
	}
	
	private static MypageService mypageService = MypageService.getInstance();
	private static MypageDao mypageDao = MypageDao.getInstance();
	
	private static ReviewDao reviewDao = ReviewDao.getInstance();
	private static BossService bossService = BossService.getInstance();
	
	int reviewNo;
	
	   //지점별 리뷰보기(회원)
	   public int read() {
	      List<Map<String, Object>> reviewList = reviewDao.readReviewMember(UserService.loginMember.get("STORE_NAME"));
	      System.out.println("============================================================");
	      System.out.println("\t   <"+ UserService.loginMember.get("STORE_NAME") +"리뷰>   ");
	      System.out.println("------------------------------------------------------------");
	      System.out.println("번호\t작성자\t\t별점\t\t내용\t       작성일");
	      System.out.println("------------------------------------------------------------");
	      for(int i = 0; i < reviewList.size(); i++) {
	         System.out.print(i+1 + "\t");
	         if(reviewList.get(i).get("MEM_NM") == null) {
	            System.out.print(PrintBlankUtil.printBlank("(알수없음)", 15));
	         }else {            
	            System.out.print(PrintBlankUtil.printBlank((String)reviewList.get(i).get("MEM_NM"), 15));
	         }
	         System.out.print(PrintBlankUtil.printBlank((String)reviewList.get(i).get("REV_SCORE"), 14));
	         System.out.print(PrintBlankUtil.printBlank((String)reviewList.get(i).get("REV_CONTENT"), 20));
	         System.out.println(PrintBlankUtil.printBlank((String)reviewList.get(i).get("REV_DATE"), 20));
	         Map<String, Object> commentList = reviewDao.readComentMember(reviewList.get(i).get("REV_NO"));
	         if(commentList != null) {
	            System.out.println("   > " + commentList.get("COMENT"));
	            System.out.println("------------------------------------------------------------");
	         }else {
	            System.out.println("------------------------------------------------------------");
	         }
	      }
	      System.out.println("============================================================");
	      return View.MEMBER_HOME;
	   }

	   public int insertReview() {
	      List<Map<String, Object>> myOrderList = mypageDao.selectMyOrderList(UserService.loginMember.get("MEM_ID"));
	      int myOrderNo = (int) myOrderList.get(mypageService.myOrderNo - 1).get("ORD_NO");
	      List<Map<String, Object>> check = reviewDao.checkReview();
	      for (int i = 0; i < check.size(); i++) {
	         if((int)check.get(i).get("ORD_NO") == myOrderNo) {
	            System.out.println("이미 리뷰를 작성하셨습니다.");
	            return View.MYORDER_LIST;
	         }
	      }
	      System.out.println("=====================리뷰=====================");
	      System.out.println("음식은 어떠셨나요?");
	      System.out.print("1.★☆☆☆☆ 2.★★☆☆☆ 3.★★★☆☆ 4.★★★★☆ 5.★★★★★\n>");
	      int scores = ScanUtil.nextInt();
	      String star = "";
	      for(int i = 0; i < scores ; i++) {
	         star += "★";
	      }
	      for(int i = 0; i < 5 - scores ; i++) {
	         star += "☆";
	      }
	      System.out.println("-------------------------------------");
	      System.out.println(star);
	      System.out.print("음식에 대한 리뷰를 남겨주세요.\n>");
	      String content = ScanUtil.nextLine();
	      System.out.println("-------------------------------------");
	      int insertReview = reviewDao.insertReview((String)UserService.loginMember.get("MEM_ID"), myOrderNo, content, star);
	      if(insertReview > 0) {
	         System.out.println("리뷰가 등록되었습니다.");
	         System.out.println("==============================================");
	      }else {
	         System.out.println("리뷰 등록에 실패하셨습니다.");
	         System.out.println("==============================================");
	      }
	      return View.MYORDER_LIST;
	   }
	   
	   //내 리뷰 보기
	   public int myReviewRead() {
	      List<Map<String, Object>> myReviewRead = reviewDao.selectmyReviewList(UserService.loginMember.get("MEM_ID"));
	      if(myReviewRead.isEmpty()) {
	         System.out.println("리뷰가 존재하지 않습니다.");
	         return View.MYPAGE;
	      }else {
	         System.out.println("============================================================");
	         System.out.println("\t\t<내가 쓴 리뷰>");
	         System.out.println("------------------------------------------------------------");
	         System.out.println("번호\t작성자\t  별점\t\t내용\t\t   작성일");
	         System.out.println("------------------------------------------------------------");
	         for(int i = 0; i < myReviewRead.size(); i++) {
	            System.out.print(i+1 + "\t");
	            System.out.print(PrintBlankUtil.printBlank((String)myReviewRead.get(i).get("MEM_NM"), 10));
	            System.out.print(PrintBlankUtil.printBlank((String)myReviewRead.get(i).get("REV_SCORE"), 14));
	            System.out.print(PrintBlankUtil.printBlank((String)myReviewRead.get(i).get("REV_CONTENT"), 20));
	            System.out.println(PrintBlankUtil.printBlank((String)myReviewRead.get(i).get("REV_DATE"), 20));
	         }
	         System.out.println("============================================================");
	         System.out.print("1. 리뷰삭제 0. 홈으로\n>");
	         int input = ScanUtil.nextInt();
	         switch(input) {
	         case 1: return View.MYREVIEW_DELETE;
	         case 0: return View.MEMBER_HOME;
	         }
	      }
	      return View.MEMBER_HOME;
	   }
	   
	   //리뷰 삭제
	   public int myReviewDelete() {
	      List<Map<String, Object>> myReviewRead = reviewDao.selectmyReviewList(UserService.loginMember.get("MEM_ID"));
	      System.out.print("삭제하실 리뷰의 번호를 입력해주세요.\n>"); 
	      this.reviewNo = ScanUtil.nextInt();
	      this.reviewNo = (int) myReviewRead.get(this.reviewNo - 1).get("REV_NO");
	      int commentDelete = reviewDao.cmtDelete(this.reviewNo);
	      int myReviewDelete = reviewDao.myReviewDelete(this.reviewNo);
	      
	      if(myReviewDelete > 0) {
	         System.out.println("리뷰가 삭제 되었습니다.");
	         System.out.println("============================================================");
	      }else {
	         System.out.println("리뷰 삭제에 실패하셨습니다.");
	         System.out.println("============================================================");
	      }
	      return View.MYREVIEW_LIST;
	   }
	   
	   
	   //리뷰 보기(점주)
	      public int listOwnerReview() {
	         Map<String,Object> ownerId  = bossService.loginOwner;
	         List<Map<String, Object>> listReview = reviewDao.selectListReview((String)ownerId.get("OWNER_ID"));

	         List<Map<String, Object>> listComent = reviewDao.selectListComent((String)ownerId.get("OWNER_ID"));
	         System.out.println("=====================================");
	         System.out.println("                <리뷰>                 ");
	         System.out.println("--------------------------------------");
	         System.out.println("번호\t닉네임\t평점\t내용\t날짜");
	         System.out.println("--------------------------------------");
	         for (Map<String, Object> review : listReview) {
	            System.out.print(review.get("REV_NO") + "\t");
	            System.out.print(review.get("MEM_NM") + "\t");
	            System.out.print(review.get("REV_SCORE") + "\t");
	            System.out.print(review.get("REV_CONTENT") + "\t");
	            System.out.print(review.get("REV_DATE") + "\n");
	         }
	         System.out.println("--------------------------------------");
	         for(Map<String, Object> coment : listComent){
	               System.out.print(coment.get("REV_NO") +"번 " + coment.get("MEM_NM") + "에 대한 답변 : " + coment.get("CMT_CONTENT") + "\t");
	               System.out.print("(" + coment.get("CMT_DATE") + ")" + "\n");
	            }
	         System.out.println("=====================================");
	         
	         System.out.print("1.답글달기  0.점주 페이지>");
	         int input = ScanUtil.nextInt();
	         switch (input) {
	         case 1:
	            return View.OWNER_CMT_INSERT;
	         case 0:
	            return View.OWNER_HOME;
	         }
	         return View.OWNER_HOME;
	      }
	      
	      // 댓글달기
	      public int insertCmt() {
	         System.out.print("답할 리뷰 번호>");
	         int reviewNo = ScanUtil.nextInt();
	         
	         Map<String,Object> ownerId  = bossService.loginOwner;
	         List<Map<String, Object>> coment = reviewDao.selectListComent((String)ownerId.get("OWNER_ID"));
	         for (int i = 0; i < coment.size(); i++) {
	            if((int)coment.get(i).get("REV_NO") == reviewNo) {
	               System.out.println("작성된 댓글입니다.");
	               return View.OWNER_CMT_INSERT;
	            }
	         }
	         
	         System.out.print("답변>");
	         String content = ScanUtil.nextLine();

	         int result = reviewDao.insCmt(reviewNo,content);

	         if (0 < result) {
	            System.out.println("댓글을 남겼습니다.");
	         } else {
	            System.out.println("댓글 등록에 실패하였습니다.");
	         }
	         return View.OWNER_REVIEW_LIST;
	      }


	   
	   
}
