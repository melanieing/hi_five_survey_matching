package admin;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

import dao.MemberDAO;
import dao.QuestionDAO;
import exceptions.AnswerOutOfBoundException;
import service.Survey;
import vo.QuestionVO;

public class Admin {

	private final static int ADMIN_PWD = 1234;
	private Scanner sc;
	private MemberDAO mdao;
	private QuestionDAO qdao;
	private Survey sv;
	
	public Admin(Scanner sc, MemberDAO mdao, QuestionDAO qdao, Survey sv) {
		super();
		this.sc = sc;
		this.mdao = mdao;
		this.qdao = qdao;
		this.sv = sv;
	}

	public void adminInit() {
		System.out.println("관리자 모드입니다. 비밀번호를 입력하세요 : ");
		int i = 0;
		while (true) {
			int pwd = 0;
			try {
				pwd = sc.nextInt();
			} catch (Exception e) {
				sc = new Scanner(System.in);
				// e.printStackTrace();
			}
			if (pwd == ADMIN_PWD) { // 비밀번호 일치 시 관리자 로그인 성공
				System.out.println("관리자님 반갑습니다!^^");
				break;
			} else if (i < 3){
				System.out.printf("비밀번호가 틀렸습니다! %d번의 기회가 남았습니다.\n>> ", 3-i);
				i++;
			} else { // 비밀번호 입력오류가 3회 초과 시 프로그램 종료
				System.out.println("비밀번호가 맞지 않아 프로그램을 종료합니다.");
				System.exit(0);
			}
		}
		
		while (true) {
			System.out.println("\n===========================================================");
			System.out.println("☞ 관리자 메뉴를 선택하세요.");
			System.out.println("1. 회원정보 수정");
			System.out.println("2. 회원정보 검색");
			System.out.println("3. 회원정보 삭제");
			System.out.println("4. 설문응답결과 확인");
			System.out.print("5. 프로그램 종료\n>> ");
			int ans = 0;
			try {
				ans = sc.nextInt();
			} catch (Exception e) {
				sc = new Scanner(System.in);
				System.out.println("1~4 중에서 입력하세요!");
				// e.printStackTrace();
			}
			switch (ans) {
			case 1:
				updateMember();
				break;
			case 2:
				searchMember();
				break;
			case 3:
				deleteMember();
				break;
			case 4:
				checkSurveyResult();
				break;
			case 5:
				System.exit(0);
			}
		}
	}
	
	// 1. 회원정보 수정
	public void updateMember() {
		long targetMemId = 0;
		String newMemName = "";
		int newMemAge = 0;
		String newMemGender = "";
		try {
			System.out.print("☞ 정보를 수정할 회원의 고유번호를 입력하세요 : ");
			targetMemId = sc.nextLong();
			System.out.print("☞ 새로운 별명, 나이, 성별을 입력하세요 : ");
			newMemName = sc.next();
			newMemAge = sc.nextInt();
			newMemGender = sc.next();
		} catch (Exception e) {
			sc = new Scanner(System.in);
			System.out.println("적절한 형식으로 입력하세요! (고유번호, 나이 : 숫자 / 별명 : 띄어쓰기 없는 문자열 / 성별 : 남자/여자");
			// e.printStackTrace();
		}
		if (mdao.updateByMemId(targetMemId, newMemName, newMemAge, newMemGender)) {
			// System.out.println("해당 회원정보가 수정되었습니다.");
		} else {
			System.out.println("회원정보 수정에 실패했습니다.");
		}
	}
	
	// 2. 회원정보 검색
	public void searchMember() {
		while (true) {
			System.out.print("☞ 검색할 회원의 고유번호를 입력하세요 : ");
			long targetMemId = 0;
			try {
				targetMemId = sc.nextLong();
			} catch (Exception e) {
				sc = new Scanner(System.in);
				System.out.println("숫자를 입력하세요!");
				continue;
				// e.printStackTrace();
			}
			if (mdao.searchByMemId(targetMemId) != null) {
				System.out.println(mdao.searchByMemId(targetMemId).toString());
				break;
			} else {
				System.out.println("검색되는 정보가 없습니다!");
				System.exit(0);
			} 
		}
	}
	
	// 3. 회원정보 삭제
	public void deleteMember() {
		while (true) {
			System.out.print("☞ 정보를 삭제할 회원의 고유번호를 입력하세요 : ");
			long targetMemId = 0;
			try {
				targetMemId = sc.nextLong();
			} catch (InputMismatchException e) {
				sc = new Scanner(System.in);
				System.out.println("숫자를 입력하세요!");
				continue;
				// e.printStackTrace();
			} catch (Exception e) {
				// e.printStackTrace();
			} 
			if (mdao.deleteByMemId(targetMemId)) {
				System.out.println("해당 회원정보를 삭제했습니다.");
				break;
			} else {
				System.out.println("존재하지 않는 회원고유번호입니다!");
				System.exit(0);
			}
		}
	}
	
	// 4. 설문응답결과 확인
	public void checkSurveyResult() {
		while (true) {
			System.out.println("\n===========================================================");
			System.out.println("아래 설문데이터 검색 메뉴 중에서 선택하세요.");
			System.out.println("1. 회원고유번호로 설문데이터 검색");
			System.out.println("2. 설문유형+설문응답으로 설문데이터 검색");
			System.out.println("3. 설문유형으로 설문데이터 검색");
			System.out.println("4. 전체 설문유형의 응답 비율 확인");
			System.out.print("5. 프로그램 종료\n>> ");
			
			try {
				int ans2 = sc.nextInt();
				if (ans2 == 1) {
					searchDataByMemId();
				} else if (ans2 == 2) {
					searchDataByQuestTypeAndAnswer();
				} else if (ans2 == 3) {
					searchDataByQuestType();
				} else if (ans2 == 4) {
					sv.showSurveyResult(qdao);
				} else if (ans2 == 5) {
					System.out.println("다음에 또 이용해주세요!^^");
					System.exit(0);
				} else {
					throw new AnswerOutOfBoundException();
				}
			} catch (InputMismatchException | AnswerOutOfBoundException e) {
				sc = new Scanner(System.in);
				System.out.println("1~4 중에서 입력하세요!");
				continue;
				// e.printStackTrace();
			}
		}
	}

	// 4-1. 회원고유번호로 설문데이터 검색
	public void searchDataByMemId() {
		while (true) {
			System.out.print("☞ 정보를 확인할 회원의 고유번호를 입력하세요 : ");
			long targetMemId = 0;
			try {
				targetMemId = sc.nextLong();
			} catch (Exception e) {
				sc = new Scanner(System.in);
				System.out.println("숫자를 입력하세요!");
				continue;
				// e.printStackTrace();
			}
			System.out.printf("%s님의 설문응답 결과\n", mdao.searchByMemId(targetMemId).getMemName());
			for (int questType = 1; questType <= 5; questType++) {
				System.out.print(
						"▶ " + questType + "번 질문에 대한 응답 : " + qdao.searchQuestAnswer(targetMemId, questType) + "\n");
			} 
			break;
		}
	}
	
	// 4-2.설문유형+설문응답으로 설문데이터 검색
	public void searchDataByQuestTypeAndAnswer() {
		int questType = 0;
		int questAns = 0;
		try {
			System.out.print("☞ 확인할 설문유형을 입력하세요(1~5) : ");
			questType = sc.nextInt();
			System.out.print("☞ 확인할 설문응답을 입력하세요(1~5) : ");
			questAns = sc.nextInt();
		} catch (Exception e) {
			sc = new Scanner(System.in);
			System.out.println("숫자를 입력하세요!");
			// e.printStackTrace();
		}
		try {
			Iterator<Long> it = qdao.searchMemId(questType, questAns).iterator();
			System.out.printf("%d번 질문에 %d번을 선택한 회원 정보 목록\n", questType, questAns);
			int i = 1;
			while (it.hasNext()) {
				long tempId = (long)it.next();
				System.out.println(i + "번째" + mdao.searchByMemId(tempId).toString() + "\n");
				i++;
				break;
			}
		} catch (NullPointerException e) {
			System.out.println("검색되는 정보가 없습니다!");
			System.exit(0);
			// e.printStackTrace();
		}
	}
	
	// 4-3. 설문유형으로 설문데이터 검색
	public void searchDataByQuestType() {
		while (true) {
			System.out.println("☞ 데이터를 확인할 설문유형을 입력하세요(1~5) : ");
			try {
				int questType = sc.nextInt();
				int i = 1;
				Iterator<QuestionVO> it = qdao.searchQuestByQuestType(questType).iterator();
				while (it.hasNext()) {
					System.out.println(i + "번째" + it.next().toString());
					i++;
					break;
				}
			} catch (InputMismatchException e) {
				sc = new Scanner(System.in);
				System.out.println("1~5 중에서 입력하세요!");
				continue;
				// e.printStackTrace();
			} catch (NullPointerException e) {
				System.out.println("검색되는 정보가 없습니다!");
				System.exit(0);
				// e.printStackTrace();
			} 
		}
	}


	






	
}
