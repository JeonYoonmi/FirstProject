package service;

import java.util.List;
import java.util.Map;

import dao.OrderDao;
import util.PrintBlankUtil;
import util.ScanUtil;
import util.View;

public class OrderService {
	//싱글톤 패턴 : (디자인패턴 중 하나)객체 생성을 막는 패턴 
			//private을 붙여 생성자를 호출하지 못하게함 -> 객체 생성도 불가
			private OrderService() {
				
			}
			private static OrderService instance; //instance : 객체를 보관하는 변수
			public static OrderService getInstance() { //getInstance() : 객체를 빌려주는 변수
				if(instance == null) {
					instance = new OrderService();
				}
				return instance;
			}//싱글톤 end
			
			int ordNo; // 주문번호지정
			
			
			private static OrderDao orderDao = OrderDao.getInstance();
			
			//주문하기
			public int order () {
				System.out.println("1.메뉴담기 2.장바구니 0.돌아가기");
				int input = ScanUtil.nextInt();
				switch (input) {
				case 1:
					return View.MENU_READ;//메뉴불러오기 및 담기
				case 2:
					return View.ORDER_READ;//주문서 및 장바구니
				case 0:
					return View.MEMBER_HOME;
				}
				return View.MAIN;
			}

			// 메뉴를 장바구니에 담기
			public int cartInsert() {
				// 주문시작
				Map<String, Object> ordNo = orderDao.selectOrderNo();//주문번호 sql
				this.ordNo = (int)ordNo.get("ORDER_NO");
				while (true) {
					System.out.print("주문할 메뉴번호를 선택해주세요. 다시 돌아가시려면 0번을 눌러주세요. >");
					int menuNo = ScanUtil.nextInt();
					System.out.println();
					if (menuNo > 42 || menuNo == 0) { // 메뉴가 1~42개 있어요
						System.out.println("주문하실 메뉴가 존재하지 않습니다.");
						return View.ORDER;
					} else {// 메인메뉴와 세트메뉴 추가
						if ((menuNo >= 1 && menuNo <= 8) || (menuNo >= 9 && menuNo <= 12)) {
							System.out.print("수량을 입력해주세요 >");
							System.out.println();
							int menuQty = ScanUtil.nextInt();

							int order = orderDao.selectOrderInsert(this.ordNo,
									(String) UserService.loginMember.get("MEM_ID"),
									(String) UserService.loginMember.get("STORE_NAME"));
							int orderMenu = orderDao.selectOrderMenuInsert(this.ordNo, menuNo, menuQty);
							int ordDateTot = orderDao.selectOrdDateTot(this.ordNo);// 메인,세트메뉴 저장 end

							// 토핑추가 시작
							toping : while (true) {
								System.out.println("토핑을 추가를 원하시면 토핑번호를 입력해주세요.\n" + "추가 하지 않으시려면 '0'을 입력해주세요\n>");
								// 추가 가능
								int toppingNo = ScanUtil.nextInt();

								if (toppingNo >= 13 && toppingNo <= 25) {

									System.out.print("수량을 입력해주세요 >");
									System.out.println();
									int toppingQty = ScanUtil.nextInt();

									int ordTopping = orderDao.selectOrderMenuInsert(this.ordNo, toppingNo, toppingQty);
									int ordToppingDateTot = orderDao.selectOrdDateTot(this.ordNo);

									if (0 < orderMenu && 0 < ordDateTot && 0 < order && 0 < ordTopping
											&& 0 < ordToppingDateTot) {
										System.out.println("장바구니에 메뉴가 담겼습니다.");
										System.out.println();
										System.out.println("1.장바구니가기 2.계속 구매하기 0.홈으로");
										int input = ScanUtil.nextInt();
										System.out.println();
										if (input == 1) {
											return View.ORDER_READ;// 장바구니 및 주문서 뷰로
										} else if(input == 2) {
											break toping;// 메뉴 고르는 뷰로
										} else {
											return View.MEMBER_HOME;
										}
									}

									// 토핑 추가하지 않음
								} else if (toppingNo == 0) {
									if (0 < order && 0 < orderMenu && 0 < ordDateTot) {
										System.out.println("장바구니에 메뉴가 담겼습니다.");
										System.out.println("1.장바구니가기 2.계속 구매하기 0.홈으로");
										int input = ScanUtil.nextInt();
										if (input == 1) {
											return View.ORDER_READ;// 장바구니 및 주문서 뷰로
										} else if (input == 2) {
											break toping;// 메뉴 고르는 뷰로
										} else {
											return View.MEMBER_HOME;
										}
									}
								}else {
									System.out.println("다시 입력해주세요.");
								}
							}

						}
					}
					
					// 사이드 메뉴 추가
					if (menuNo >= 26 && menuNo <= 42) {
						System.out.println("수량을 입력해주세요>");
						int sideQty = ScanUtil.nextInt();
						
//						int order = orderDao.selectOrderInsert(this.ordNo,
//								(String) UserService.loginMember.get("MEM_ID"),
//								(String) UserService.loginMember.get("STORE_NAME"));
						int orderMenu = orderDao.selectOrderMenuInsert(this.ordNo, menuNo, sideQty);
						int ordDateTot = orderDao.selectOrdDateTot(this.ordNo);

						if (0 < orderMenu && 0 < ordDateTot) {
							System.out.println("장바구니에 메뉴가 담겼습니다.");
							System.out.println("1.장바구니가기 0.계속 구매하기");
							int input = ScanUtil.nextInt();
							if (input == 1) {
								return View.ORDER_READ;// 장바구니 및 주문서 뷰로
							} else {
//								return View.ORDER_INSERT;// 메뉴 고르는 뷰로
							}
						}
					} else {

					} // 메뉴담기 end
				}//전체 주문 while end
//				return View.ORDER;
			}
			
			
			
			//장바구니 조회
			public int cart() {
				Map<String, Object> myCart = orderDao.selectMyCart((String) UserService.loginMember.get("STORE_NAME"), (String) UserService.loginMember.get("MEM_ID"));
				List<Map<String, Object>> cartMenuList = orderDao.selectCartMenuList((String) UserService.loginMember.get("MEM_ID"));//메뉴조회
				if(myCart == null) {
					System.out.println("===================================================================");
					System.out.println("\t\t\t    장바구니");
					System.out.println("===================================================================");
					System.out.println();
					System.out.println("\t\t\t장바구니가 비어있습니다.");
					System.out.println();
					System.out.println("===================================================================");
				}else {
					System.out.println("===================================================================");
					System.out.println("\t\t\t    장바구니");
					System.out.println("===================================================================");
					System.out.println("선택매장 : " + myCart.get("STORE_NAME"));
					System.out.println("-------------------------------------------------------------------");
					for (int i = 0; i < cartMenuList.size(); i++) {
						System.out.print(i+1 + "\t");
						System.out.print(PrintBlankUtil.printBlank(cartMenuList.get(i).get("MENU_NM") + "[" + cartMenuList.get(i).get("MENU_QTY") + " 개]", 30));						
						System.out.println(PrintBlankUtil.printBlank(cartMenuList.get(i).get("SUM") + " 원" , 15));
					}					
					System.out.println("-------------------------------------------------------------------");
					System.out.print(PrintBlankUtil.printBlank("소계\t ", 35));
					System.out.println(PrintBlankUtil.printBlank(myCart.get("ORD_TOT") + " 원", 15));
					System.out.println();
					System.out.println("최소 주문금액 14000원");
					System.out.println("===================================================================");
					System.out.println();
					System.out.println("1.결제하기 2.수량수정하기 3.메뉴삭제하기 0.돌아가기");
					int input = ScanUtil.nextInt();
					switch (input) {
					case 1:
						return View.PAYMENT;
					case 2:
						return View.ORDER_UPDATE;
					case 3:
						return View.ORDER_DELETE;
					case 0:
						return View.ORDER;
					}
					
				}
				
				return View.ORDER;
			}

			
			//결제
			public int payment() {
				Map<String, Object> myCart = orderDao.selectMyCart((String) UserService.loginMember.get("STORE_NAME"), (String) UserService.loginMember.get("MEM_ID"));
	            if(myCart == null) {
	               System.out.println("장바구니가 비어있습니다.");
	               return View.ORDER;
	            }
				System.out.println("1.카드결제 2.현금결제 3.결제취소 0.돌아가기");
				int payNo = ScanUtil.nextInt();				
				int payment = orderDao.selectpayment(payNo, (String) UserService.loginMember.get("MEM_ID"));
				
				if(payNo == 1 || payNo == 2) {
					if(0 < payment) {
						System.out.println("결제가 완료되었습니다.");
					}else {
						System.out.println("결제가 실패되었습니다.");
						return View.PAYMENT;
					}
				}else if(payNo == 3) {
					if(0 < payment) {
						System.out.println("결제가 취소되었습니다.");
						return View.ORDER_READ;
					}else {
						return View.PAYMENT;
					}
				}else if(payNo == 0) {
					return View.ORDER_READ;
				}
				
				return View.MEMBER_HOME;
			}

			//장바구니 수정
			public int cartUpdate() {
				Map<String, Object> cartOrdNo = orderDao.selectCartOrdNo((String) UserService.loginMember.get("MEM_ID")); //주문번호 불러오려고
				List<Map<String, Object>> cartMenuList = orderDao.selectCartMenuList((String) UserService.loginMember.get("MEM_ID"));//메뉴이름조회
				
				
				System.out.println("수량을 수정할 메뉴의 번호를 입력해주세요. >");
				int input = ScanUtil.nextInt();
				System.out.println("변경할 수량을 입력해주세요. >");
				int qty = ScanUtil.nextInt();
				
				int cartUpdate = orderDao.selectCartUpdate(qty, (int)cartOrdNo.get("ORD_NO"), (String)cartMenuList.get(input-1).get("MENU_NM"));
				int ordTot = orderDao.updateTot((int)cartOrdNo.get("ORD_NO"));//수량이 수정되면 총금액도 수정
				
				if( 0 < cartUpdate && 0 < ordTot) {
					System.out.println("수량 수정이 완료되었습니다.");
				}
							
				
				return View.ORDER_READ;
			}

			//장바구니 메뉴삭제
			public int cartDelete() {
				List<Map<String, Object>> cartMenuList = orderDao.selectCartMenuList((String) UserService.loginMember.get("MEM_ID"));//메뉴이름조회
				Map<String, Object> cartOrdNo = orderDao.selectCartOrdNo((String) UserService.loginMember.get("MEM_ID")); //주문번호 불러오려고
				
				System.out.println("삭제할 메뉴의 번호를 입력해주세요. >");
				int input = ScanUtil.nextInt();
				
				int cartDelete = orderDao.selectCartDelete((int)cartOrdNo.get("ORD_NO"), (String)cartMenuList.get(input-1).get("MENU_NM"));
				int ordTot = orderDao.updateTot((int)cartOrdNo.get("ORD_NO"));//수량이 수정되면 총금액도 수정
				
				
				if( 0 < cartDelete && 0 < ordTot) {
					System.out.println("삭제되었습니다.");
				}
				
				
				return View.ORDER_READ;
			}
}