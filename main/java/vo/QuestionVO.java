package vo;

public class QuestionVO {

	private long questId;
	private long memId;
	private int questType;
	private int questAns;
	
	public QuestionVO() { }
	public QuestionVO(long questId, long memId, int questType, int questAns) {
		super();
		this.questId = questId;
		this.memId = memId;
		this.questType = questType;
		this.questAns = questAns;
	}

	public long getQuestId() {
		return questId;
	}
	public void setQuestId(long questId) {
		this.questId = questId;
	}
	public long getMemId() {
		return memId;
	}
	public void setMemId(long memId) {
		this.memId = memId;
	}
	public int getQuestType() {
		return questType;
	}
	public void setQuestType(int questType) {
		this.questType = questType;
	}
	public int getQuestAns() {
		return questAns;
	}
	public void setQuestAns(int questAns) {
		this.questAns = questAns;
	}
	
	@Override
	public String toString() {
		return "[설문정보]\n▶설문고유번호(questId) : " + questId 
				+ "\n▶회원고유번호(memId) : " + memId 
				+ "\n▶설문유형(questType) : " + questType 
				+ "\n▶설문응답(questAns) : " + questAns;
	}
	
	

}
