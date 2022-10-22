package util;

import java.util.Scanner;

public class ScanUtil {
	
	//접근제어자: private, public
	private static Scanner s = new Scanner(System.in);
	
	public static String nextLine() {
		return s.nextLine();
	}
	
	public static int nextInt() { //반드시 숫자만 입력해야하는데 문자를 입력하면 오류가 난다.
		int result = 0;
		
		try {
			 result = Integer.parseInt(s.nextLine());
		 }catch(Exception e) {
			 System.out.print("다시 입력해주세요.\n>");
			 result = nextInt(); //재귀호출
		 }
		return result;
	}
	
	
	
	
	

}
