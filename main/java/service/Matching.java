package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import dao.MemberDAO;
import dao.QuestionDAO;

public class Matching {
	
	private QuestionDAO qdao;
	final static int questSize = 5;
	
	public Matching() {	}
	
	public Matching(QuestionDAO qdao) {
		super();
		this.qdao = qdao;
	}

	public int[] getMatchingChecklist(long targetMemId) {
		// memId와 일치하는 설문유형 별 설문응답을 배열에 담기 (ex. targetAns[0] : memId의 1번 설문응답)
		// SELECT QUEST_ANS FROM QUESTION WHERE MEM_ID=?
		int[] targetAns = new int[questSize];
		for (int i = 0; i < questSize; i++) {
			targetAns[i] = qdao.searchQuestAnswer(targetMemId, i+1);
		}
		
		// memId의 설문정보와 일치하는 회원고유번호 리스트 합치기
		// SELECT MEM_ID FROM QUESTION WHERE QUEST_TYPE=? AND QUEST_ANS=?
		ArrayList<Long> memIds = new ArrayList<>();
		for (int i = 0; i < questSize; i++) { 
			if (qdao.searchMemId(i+1, targetAns[i]) != null) {
				memIds.addAll(qdao.searchMemId(i+1, targetAns[i]));
			}
		}
		// memId 안에 있는 회원고유번호 중 가장 큰 수 뽑기
		int memIdMax = 0;
		Iterator<Long> it2 = memIds.iterator();
		while (it2.hasNext()) {
			int temp = it2.next().intValue();
			if (temp >= memIdMax) {
				memIdMax = temp;
			}
		}
		// memId 안에 있는 회원고유번호 중 가장 큰 수만큼 배열 만들고 memId의 설문응답과 일치할 때마다 count올리기 
		int[] memberScores = new int[memIdMax];
		
		if (!memIds.isEmpty()) {
			try {
				Iterator<Long> it = memIds.iterator();
				while (it.hasNext()) {
					int temp = it.next().intValue();
					memberScores[temp - 1]++;
				}
				for (int ms : memberScores) {
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			try {
				memberScores[(int)targetMemId-1] = 0; // 본인(타겟회원고유번호)이 본인에게 매칭되면 안되므로 0으로 초기화
			} catch (ArrayIndexOutOfBoundsException e) { /* e.printStackTrace(); */ }
		} else {
			System.out.println("getMatchingChecklist() else 아직 매칭되는 회원이 없습니다!ㅠ_ㅠ");
			System.out.println("더 많은 사람들이 Hi-Five!에 참여할 수 있도록 공유해주세요!>_<");
			return null;
		}
		return memberScores;
	}
	
	public HashMap<Long, Integer> getOneMatchingMemId(long targetMemId) {
		int[] memberScores = getMatchingChecklist(targetMemId);
		
		int max = 0;
		long matchingMemId = 0;
		int similarity = 0;
		
		// 예상친밀도 60% 이상만 매칭가능
		for (int i = 0; i < memberScores.length; i++) {
			if ((memberScores[i] >= max) && (memberScores[i] >= 3)) {
				max = memberScores[i];
				matchingMemId = i+1;
				similarity = max*20;
			}
		}
		HashMap<Long, Integer> matchingInfo = new HashMap<>();
		matchingInfo.put(matchingMemId, similarity);
		
		return matchingInfo;
	}
	
	public LinkedHashMap<Long, Integer> getGroupMatchingMemId(long targetMemId) {
		int[] memberScores = getMatchingChecklist(targetMemId);
		// 순서가 있는 맵에 (회원고유번호, 예상친밀도)를 넣기
		LinkedHashMap<Long, Integer> matchingInfo = new LinkedHashMap<>();
		
		// 예상친밀도 60% 이상만 매칭가능
		for (int i = 0; i < memberScores.length; i++) {
			if (memberScores[i] == 5) {
				matchingInfo.put((long)(i+1), memberScores[i]*20);
			} else if (memberScores[i] == 4){
				matchingInfo.put((long)(i+1), memberScores[i]*20);
			} else if (memberScores[i] == 3) {
				matchingInfo.put((long)(i+1), memberScores[i]*20);
			} else {
				continue;
			}
		}
		
		if (matchingInfo.size() < 3) {
			System.out.println("아직 매칭되는 회원이 없습니다!ㅠ_ㅠ");
		}
		return (matchingInfo.size() >= 3) ? matchingInfo : null;
	}
	
	public void matchGroup(MemberDAO mdao, Matching manager, String memName) {
		try {
			long matchingMemId = 0L;
			int matchingSimilarity = 0;
			int countLimit = 1;
			Set<Entry<Long, Integer>> entrySet = manager.getGroupMatchingMemId(mdao.searchMemId(memName)).entrySet();
			for (Entry<Long, Integer> entry : entrySet) {
				if (countLimit == 4) { break; }
				matchingMemId = entry.getKey();
				matchingSimilarity = entry.getValue();
				System.out.printf("%d 번째, %s 님입니다!>_< (예상친밀도 %d%%) (짝짝짝)\n", countLimit,
						mdao.searchByMemId(matchingMemId).getMemName(), matchingSimilarity); // 1 : memName's index
				++countLimit;
			}
		} catch (NullPointerException e) { System.exit(0); }
	}

	public void matchOne(MemberDAO mdao, Matching manager, String memName) {
		try {
			long matchingMemId = 0L;
			int matchingSimilarity = 0;
			Set<Entry<Long, Integer>> entrySet = manager.getOneMatchingMemId(mdao.searchMemId(memName)).entrySet();
			for (Entry<Long, Integer> entry : entrySet) {
				matchingMemId = entry.getKey();
				matchingSimilarity = entry.getValue();	
			}
			System.out.printf("%s 님입니다!>_< (예상친밀도 %d%%) (짝짝짝)\n", mdao.searchByMemId(matchingMemId).getMemName(), matchingSimilarity);
		} catch (NullPointerException e) { 
			System.out.println("matchone()error");
			System.out.println("아직 매칭되는 회원이 없습니다!ㅠ_ㅠ");
			System.out.println("더 많은 사람들이 Hi-Five!에 참여할 수 있도록 공유해주세요!>_<");
			e.printStackTrace();
			System.exit(0); 
		}
	}

}
