package vo;

public class MemberVO {
	
	private long memId;
	private String memName;
	private int memAge;
	private String memGender;
	
	public MemberVO() {	}
	public MemberVO(long memId, String memName, int memAge, String memGender) {
		super();
		this.memId = memId;
		this.memName = memName;
		this.memAge = memAge;
		this.memGender = memGender;
	}

	public long getMemId() {
		return this.memId;
	}
	public void setMemId(long memId) {
		this.memId = memId;
	}
	public String getMemName() {
		return this.memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	public int getMemAge() {
		return this.memAge;
	}
	public void setMemAge(int memAge) {
		this.memAge = memAge;
	}
	public String getMemGender() {
		return this.memGender;
	}
	public void setMemGender(String memGender) {
		this.memGender = memGender;
	}
	
	@Override
	public String toString() {
		return "[회원정보]\n▶회원고유번호(memId) : " + memId 
				+ "\n▶회원별명(memName) : " + memName 
				+ "\n▶회원나이(memAge) : " + memAge 
				+ "\n▶회원성별(memGender) : "	+ memGender;
	}
	
}
