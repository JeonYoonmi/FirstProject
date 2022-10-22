package service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import dao.MenuDao;
import dao.UserDao;
import util.ScanUtil;
import util.View;

public class UserService {

	public UserService() {
			
		}
	//객체를 보관해놓을 변수
	private static UserService instance;
	
	//객체를 빌려주는 메소드
	//다른 클래스에서 이 객체가 필요할 때에는 getInstance를 불러서 객체를 생성한다.
	public static UserService getInstance() {
		if(instance == null) {
			instance = new UserService();
		}
		return instance;
	}
	
	private static UserDao userdao = UserDao.getInstance();
	
	public static Map<String, Object> loginMember;
	
	public int join() {
		String memId;
		String memPw;
		String memName;
		String memBir;
		String memPh;
		
		System.out.println("==========================회원가입==========================");
		while(true) {
			System.out.print("아이디(최소 4자, 최대  10자, 특수문자 -, _ 가능)\n>");
			memId = ScanUtil.nextLine();
			String regex_id = "[a-z0-9_-]{4,10}";
			boolean result = Pattern.matches(regex_id, memId);
			if(result == false) {
				System.out.println("부적합한 아이디입니다.");
				System.out.println("---------------------------------------------------------");
			}else{
				System.out.println("---------------------------------------------------------");
				break;
			}
		}
		
		while(true) {
			System.out.print("비밀번호(최소 8자, 최대 10자, 하나 이상의 문자, 하나의 숫자 및 하나의 특수 문자)\n>");
			memPw = ScanUtil.nextLine();
			String regex_pw = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,10}$";
			boolean result = Pattern.matches(regex_pw, memPw);
			if(result == false) {
				System.out.println("부적합한 비밀번호입니다.");
				System.out.println("---------------------------------------------------------");
			}else{
				System.out.println("---------------------------------------------------------");
				break;
			}
		}
		
		while(true) {
			System.out.print("이름(닉네임)\n>");
			memName = ScanUtil.nextLine();
			if(memName.length() > 33) {
				System.out.println("이름(닉네임)이 너무 깁니다. 다시 입력해주세요.");
				System.out.println("---------------------------------------------------------");
			}else {
				System.out.println("---------------------------------------------------------");
				break;
			}
		}
		
		while(true) {
			System.out.print("생년월일(ex.92/12/22)\n>");
			memBir = ScanUtil.nextLine();
			String regex_bir = "[0-9]{2}/[0-9]{2}/[0-9]{2}";
			boolean result = Pattern.matches(regex_bir, memBir);
			if(result == false) {
				System.out.println("생일이 잘못 입력됐습니다. 다시 입력해주세요.");
				System.out.println("---------------------------------------------------------");
			}else{
				System.out.println("---------------------------------------------------------");
				break;
			}
		}
		
		while(true) {
			System.out.print("전화번호(하이픈(-)을 넣어주세요)\n>");
			memPh = ScanUtil.nextLine();
			String regex_ph = "^0\\d{1,3}-\\d{3,4}-\\d{4}";
			boolean result = Pattern.matches(regex_ph, memPh);
			if(result == false) {
				System.out.println("부적합한 전화번호입니다.");
				System.out.println("---------------------------------------------------------");
			}else{
				System.out.println("---------------------------------------------------------");
				break;
			}
		}
		
		

		System.out.print("주소(ex.대전 중구 오류동 175-13 3층)\n>");
		String memAdd = ScanUtil.nextLine();
		System.out.println("===============================================");
		
		int result = userdao.insertMember(memId, memPw, memName, memBir, memPh, memAdd);
		
		if(0 < result) {
			System.out.println("회원가입 성공");
		}else {
			System.out.println("회원가입 실패");
		}
		
		return View.MAIN;
	}

	public int login() {
		System.out.println("=============================로그인=============================");
		System.out.print("아이디>");
		String memId = ScanUtil.nextLine();
		System.out.print("비밀번호>");
		String memPw = ScanUtil.nextLine();
		Map<String, Object> member = userdao.loginMember(memId, memPw);
		
		if(member == null) {
			System.out.println("아이디 혹은 비밀번호를 잘못 입력하였습니다.");
			System.out.println("================================================================");
			return View.MEMBER_LOGIN;
		}else {
			System.out.println("로그인 성공!");
		}
		System.out.println("================================================================");
		
		loginMember = member;
		
		Map<String, Object> homedao = userdao.matchingStore(UserService.loginMember.get("MEM_ID"));
		System.out.println();
		System.out.println("======================================================================================");
		System.out.println(homedao.get("RESULT"));
		System.out.println("주소변경을 원하시면 마이페이지 > 회원정보수정 > 주소지변경 서비스를 이용해주세요.");
		System.out.println("======================================================================================");
		UserService.loginMember.put("STORE_NAME", homedao.get("STORE_NAME"));
		
		return View.MEMBER_HOME;
	}

}
