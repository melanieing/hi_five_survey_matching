package service;

import java.util.InputMismatchException;
import java.util.Scanner;

import admin.Admin;
import dao.MemberDAO;
import dao.QuestionDAO;
import exceptions.AnswerOutOfBoundException;
import exceptions.YesOrNoException;
import vo.MemberVO;

public class Main {
	
	private final static int QUESTION_SIZE = 5;
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		Survey qw = new Survey();
		MemberDAO mdao = new MemberDAO();
		QuestionDAO qdao = new QuestionDAO();
		Matching manager = new Matching(qdao);
		Survey sv = new Survey();
		
		// 0. 메인 시작 화면
		new Banner(0);
		System.out.println("▶관리자이신가요?  ☞ 1번 입력");
		System.out.print("▶사용자이신가요?  ☞ 2번 입력\n>> ");
		int mode = 0;
		while (true) {
			try {
				mode = sc.nextInt();
				if (mode == 1 || mode == 2) { break; }
				System.out.print("숫자 1, 2 중에 입력하세요!\n>> ");
			} catch (InputMismatchException e) {
				sc = new Scanner(System.in);
				System.out.print("숫자 1, 2 중에 입력하세요!\n>> ");
				// e.printStackTrace();
			}
		}
		// 1. 모드 선택
		switch(mode) {
		// 1) 관리자 모드 진입
		case 1:
			new Banner(1);
			Admin admin = new Admin(sc, mdao, qdao, sv);
			admin.adminInit();
			break;
			// 2) 사용자 모드 진입	
		case 2:
			// (1) 회원정보 등록 및 로그인
			new Banner(2);
			String memName = null;
			int memAge = 0;
			String memGender = null;
			MemberVO insertVO = null;
			Login : while (true) {
				try {
					System.out.print("☞ 별명을 입력하세요(띄어쓰기 없이 입력) : ");
					memName = sc.next();
					System.out.print("☞ 나이를 입력하세요(숫자만 입력) : ");
					memAge = sc.nextInt();
					System.out.print("☞ 성별을 입력하세요(남자/여자) : ");
					memGender = sc.next();
					if (memGender.equals("남자") || memGender.equals("여자")) {
						insertVO = new MemberVO(0L, memName, memAge, memGender);
						mdao.insertMember(insertVO);
						break;
					} else {
						throw new AnswerOutOfBoundException();
					}
				} catch (InputMismatchException | AnswerOutOfBoundException e) {
					sc = new Scanner(System.in);
					System.out.println("적절한 형식으로 입력하세요! (고유번호, 나이 : 숫자 / 별명 : 띄어쓰기 없는 문자열 / 성별 : 남자/여자");
					e.printStackTrace();
					continue Login;
				}
			}
			
			// (2) 설문조사 참여
			Survey : while (true) {	
				System.out.println("===========================================================");
				System.out.println("설문조사 START!");
				
				for (int i = 1 ; i <= QUESTION_SIZE; i++) {
					sv.doSurvey(qw, sc, i, memName, mdao, qdao);			
				}
				
				System.out.print("모든 설문조사가 끝났습니다! 고생하셨습니다~^ㅇ^\n");
				
				// (3) 설문조사 후 서비스
				Service : while (true) {
					try {
						System.out.println("===========================================================");
						System.out.println("1. 매칭하기 전, 다른 사람들은 어떻게 답했는지 볼까요?");
						System.out.println("2. 관심없고~ 매칭부터 해주세요!");
						System.out.print("3. 그냥 다음에 할게요!\n>> ");
						
						int answer = sc.nextInt();
						switch (answer) {
						// <1> 결과 확인 선택 시 
						case 1:
							sv.showSurveyResult(qdao);					
						// <2> 매칭 선택 시 	
						case 2:
							Matching : while (true) {
								System.out.println("===========================================================");
								System.out.print("원하는 매칭유형을 선택하세요!\n① 1:1 매칭\n② 그룹 매칭(총 4명)\n>> ");
								try {
									int matchType = sc.nextInt();
									// ① 1:1 매칭 선택 시
									if (matchType == 1) {
										System.out.println("1:1 매칭을 선택하셨습니다!");
										System.out.println("저희가 검토해본 결과, 당신과 가장 잘 어울릴 것 같은 친구는..");
										try {
											Thread.sleep(2000); System.out.println("...");
											Thread.sleep(2000); System.out.println("바로....");
										} catch (InterruptedException e) { }
										manager.matchOne(mdao, manager, memName);
										
										// ①-1 1:1 대화 여부 선택
										TalkWithOne : while (true) {
											try {
												System.out.print("1:1로 대화하시겠습니까?(y/n)\n>> ");
												char answer2 = sc.next().charAt(0);
												if (answer2 == 'y' || answer2 == 'Y') {
													System.out.println("좋은 친구가 되길 바라요~ 다음에 또 이용해주세요!^_^");
													System.exit(0);
												} else if (answer2 == 'n' || answer2 == 'N') {
													// ①-2 설문조사 초기화 여부 선택
													Initialization : while (true) {
														try {
															System.out.print("지금까지의 설문조사를 초기화하고 다시 하시겠습니까?(y/n)\n>>");
															char answer3 = sc.next().charAt(0);
															if (answer3 == 'y' || answer3 == 'Y') {
																qdao.deleteByMemId(mdao.searchMemId(memName));
																System.out.println("지금까지의 설문결과가 초기화되었습니다.");
																continue Survey; // 다시 (2) 설문조사 참여로 이동
															} else if (answer3 == 'n' || answer3 == 'N') {
																System.out.println("이용해주셔서 감사합니다. 다음에 또 만나요~^_^");
																System.exit(0);
															}
														} catch (InputMismatchException e) {
															sc = new Scanner(System.in);
															System.out.println("y나 n 중에서 입력해주세요!ㅇ_ㅇ;");
															continue Initialization; // ①-2 설문조사 초기화 여부 선택으로 이동
															// e.printStackTrace();
														} 
													}
												} else {
													throw new YesOrNoException();
												}
											} catch (InputMismatchException | YesOrNoException e) {
												sc = new Scanner(System.in);
												System.out.println("y나 n 중에서 입력해주세요!ㅇ_ㅇ;");
												continue TalkWithOne; // ①-1 1:1 대화 여부 선택으로 이동
												// e.printStackTrace();
											}
										}
									// ② 단체대화방(총 4명) 참여	
									} else if (matchType == 2) {
										System.out.println("그룹 매칭을 선택하셨습니다!\n당신을 포함한 4명이 단체대화방에 초대됩니다.");
										System.out.println("저희가 검토해본 결과, 당신과 가장 잘 어울릴 것 같은 친구들은..");
										try {
											Thread.sleep(2000); System.out.println("...");
											Thread.sleep(2000); System.out.println("바로....");
										} catch (InterruptedException e) { e.printStackTrace();	}
										manager.matchGroup(mdao, manager, memName);
										// ②-1 그룹 대화 여부 선택
										GroupTalk : while (true) {
											System.out.print("단체대화방으로 입장하시겠습니까?(y/n)\n>> ");
											try {
												char answer2 = sc.next().charAt(0);
												if (answer2 == 'y' || answer2 == 'Y') {
													System.out.println("좋은 친구가 되길 바라요~ 다음에 또 이용해주세요!^_^");
													System.exit(0);
												} else if (answer2 == 'n' || answer2 == 'N') {
													// ②-2 설문조사 초기화 여부 선택
													Initialization : while (true) {
														System.out.print("지금까지의 설문조사를 초기화하고 다시 하시겠습니까?(y/n)\n>> ");
														try {
															char answer3 = sc.next().charAt(0);
															if (answer3 == 'y' || answer3 == 'Y') {
																qdao.deleteByMemId(mdao.searchMemId(memName));
																System.out.println("지금까지의 설문결과가 초기화되었습니다.");
																continue Survey; // 다시 (2) 설문조사 참여로 이동
															} else if (answer3 == 'n' || answer3 == 'N') {
																System.out.println("이용해주셔서 감사합니다. 다음에 또 만나요~^_^");
																System.exit(0);
															}
														} catch (InputMismatchException e) {
															sc = new Scanner(System.in);
															System.out.println("y나 n 중에서 입력해주세요!ㅇ_ㅇ;");
															continue Initialization; // ②-2 설문조사 초기화 여부 선택으로 이동
															// e.printStackTrace();
														}
													}
												}
											} catch (InputMismatchException e) {
												sc = new Scanner(System.in);
												System.out.println("y나 n 중에서 입력해주세요!ㅇ_ㅇ;");
												continue GroupTalk; // ②-1 그룹 대화 여부 선택으로 이동
												// e.printStackTrace();
											} 
										}
									}
								} catch (InputMismatchException e) {
									sc = new Scanner(System.in);
									System.out.println("1과 2 중에서 입력하세요!ㅇ_ㅇ;");
									continue Matching; // <2> 매칭 선택 시로 이동 	
									// e.printStackTrace();
								} 
							}
						// <3> 서비스 종료 선택 시 	
						case 3:
							System.out.println("이용해주셔서 감사합니다. 다음에 또 만나요~");
							System.exit(0);
						default:
							throw new AnswerOutOfBoundException();
						}
					} catch (InputMismatchException | AnswerOutOfBoundException e) {
						sc = new Scanner(System.in);
						System.out.println("1, 2, 3 중에서 입력하세요!ㅇ_ㅇ;");
						continue Service; // (3) 설문조사 후 서비스로 이동
						// e.printStackTrace();
					} 
				}
			
		}
		
	
	}
	
	}

}
