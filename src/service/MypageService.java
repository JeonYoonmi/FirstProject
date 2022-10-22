package service;

import java.util.List;
import java.util.Map;

import dao.MypageDao;
import dao.ReviewDao;
import dao.UserDao;
import util.ScanUtil;
import util.View;

public class MypageService {

	//싱글톤 패턴 : (디자인패턴 중 하나)객체 생성을 막는 패턴 
		//private을 붙여 생성자를 호출하지 못하게함 -> 객체 생성도 불가
		private MypageService() {
			
		}
		private static MypageService instance; //instance : 객체를 보관하는 변수
		public static MypageService getInstance() { //getInstance() : 객체를 빌려주는 변수
			if(instance == null) {
				instance = new MypageService();
			}
			return instance;
		}//싱글톤 end
	
		
		public int myOrderNo;
		
		private static MypageDao mapageDao = MypageDao.getInstance();
		
		//마이페이지
		public int mypage () {
			System.out.print("1.주문내역보기 2.장바구니 3.내가 쓴 리뷰 4.회원정보수정 5.회원탈퇴 0.홈으로\n>");
			int input = ScanUtil.nextInt();
			
			switch (input) {
			case 1:
				return View.MYORDER_LIST;
			case 2:
				return View.ORDER_READ;
			case 3: 
				return View.MYREVIEW_LIST;
			case 4:
				return View.MYPAGE_UPDATE;
			case 5:
				return View.MYPAGE_DELETE;
			case 0:
				return View.MEMBER_HOME;
			}
			return View.MEMBER_HOME;
		}

		
		//주문내역목록
		public int myOrderList () {
			List<Map<String, Object>> myOrderList = mapageDao.selectMyOrderList(UserService.loginMember.get("MEM_ID"));
			System.out.println("===================================================================");
			System.out.println("\t\t\t\t주문내역");
			System.out.println("-------------------------------------------------------------------");
			for(int i = 0; i < myOrderList.size(); i++) {
				System.out.print("번호\t: " + (i + 1) + "\t");
				System.out.print("주문일\t: " + myOrderList.get(i).get("ORD_DATE") + "\n");
				System.out.print("지점명\t: " + myOrderList.get(i).get("STORE_NAME") + "\t");
				System.out.println("주문액\t: " + myOrderList.get(i).get("ORD_TOT"));
				System.out.println("---------------------------------------------------------------");
			}
			System.out.println("===================================================================");
			
			System.out.println("1.상세보기 2.리뷰쓰기 0.돌아가기");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				System.out.print("조회할 주문서 번호를 입력하세요>");
				this.myOrderNo = ScanUtil.nextInt();
				return View.MYORDER_READ;
			case 2:
				System.out.print("리뷰를 쓰실 주문서 번호를 입력하세요>");
				this.myOrderNo = ScanUtil.nextInt();
				return View.REVIEW_INSERT;
			case 0:
				return View.MYPAGE;
			}
			return View.MYORDER_LIST;
		}	
		
		//회원별주문서
		public int myOrderRead () {
			List<Map<String, Object>> myOrderList = mapageDao.selectMyOrderList(UserService.loginMember.get("MEM_ID"));
			this.myOrderNo = (int) myOrderList.get(this.myOrderNo - 1).get("ORD_NO");
			Map<String, Object> myRead = mapageDao.selectMyOrderRead(this.myOrderNo);//리스트조회
			List<Map<String, Object>> myOrderRead = mapageDao.selectMyMenuRead(this.myOrderNo);//메뉴
			
			System.out.println("===================================================================");
			System.out.println("번호   \t: " + myRead.get("ORD_NO"));
			System.out.println("주문일\t: " + myRead.get("ORD_DATE"));
			
			for (Map<String, Object> myMenuRead : myOrderRead) {		
				
				System.out.println("-\t" + myMenuRead.get("MENU_NM") + "\t" + myMenuRead.get("MENU_QTY") + " 개");
			}//메뉴불러오기
			
			System.out.println("주문액\t: " + myRead.get("ORD_TOT"));
			System.out.println("===================================================================");
			
			return View.MYORDER_LIST;
		}
		
		
		//회원정보수정
		public int myUpdate () {
			System.out.println("1.닉네임수정 2.주소지변경 0.돌아가기");
			
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				return View.MYPAGE_NAME_UPDATE;
			case 2:
				return View.MYPAGE_ADD_UPDATE;
			case 0:
				break;
			}
			return View.MYPAGE;
		}
		
		//닉네임수정
		public int nameUpdate () {
			String memNm;
			while(true) {
				System.out.println("수정할 닉네임을 입력해주세요 >");
				memNm = ScanUtil.nextLine();
				if(memNm.length() > 33) {
					System.out.println("이름(닉네임)이 너무 깁니다. 다시 입력해주세요.");
					System.out.println("-------------------------------------------------------------------");
				}else {
					System.out.println("-------------------------------------------------------------------");
					break;
				}
			}
			
			int nameUpdate = mapageDao.selectMyNameUpdate(memNm, UserService.loginMember.get("MEM_ID"));
			if(0 < nameUpdate) {
		         System.out.println("닉네임 수정이 완료되었습니다.");
		      }else {
		         System.out.println("닉네임 수정에 실패하였습니다.");
		      } 
			
			return View.MYPAGE_UPDATE;
		}
		
		//주소수정
		public int addUpdate () {
			System.out.println("변경할 주소를 입력해주세요. (ex.대전 중구 계룡로 846,3-4층)\\n>");
			String memAdd = ScanUtil.nextLine();
			
			int addUpdate = mapageDao.selectMyAddUpdate(memAdd, UserService.loginMember.get("MEM_ID"));
			if(0 < addUpdate) {
		         System.out.println("주소지 수정이 완료되었습니다.");
		      }else {
		         System.out.println("주소지 수정에 실패하였습니다.");
		      }
			return View.MYPAGE;
		}
		
		
		
		
		//탈퇴
		public int myDelete () {
			System.out.println("===================================================================");
			System.out.println("\t\t\t\t유의사항");
			System.out.println("-------------------------------------------------------------------");
			System.out.println(" 1. 탈퇴 후 재가입 시, 제한을 받을 수 있습니다.");
			System.out.println(" 2. 탈퇴한 계정의 엽기떡볶이 이용기록은 모두 삭제되며,");
			System.out.println("    삭제된 데이터는 복구가 불가능합니다.");
			System.out.println("    ● 단, 작성된 리뷰와 결제 내역은 5년까지 보관");
			System.out.println("    ● 삭제되는 이용 기록 : 아이디, 휴대폰 번호, 주문이력");
			System.out.println(" 3. 탈퇴 후 3개월 이내에 동일한 휴대폰 번호로\r\n" + 
					"       재가입이 불가능하오니, 탈퇴 시 유의하시기 바랍니다.");
			System.out.println("===================================================================\n");
			System.out.println("정말 탈퇴하시겠습니까?");
			System.out.println("1.탈퇴하기 0.돌아가기");
			int input = ScanUtil.nextInt();
			if(input == 1) {
				System.out.println("아이디를 입력하세요>");
				String memId = ScanUtil.nextLine();
				System.out.println("비밀번호를 입력하세요>");
				String memPw = ScanUtil.nextLine();
				
				int myDelete = mapageDao.selectMyDelete(memId, memPw);
				if(0 < myDelete) {
					System.out.println("회원탈퇴가 완료되었습니다.");
				}else {
					System.out.println("회원탈퇴에 실패하였습니다.");
				}			
			}else {
				
			}
			return View.MAIN;
			
		}
}
