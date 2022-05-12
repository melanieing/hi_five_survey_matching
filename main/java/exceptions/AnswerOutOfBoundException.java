package exceptions;

public class AnswerOutOfBoundException extends RuntimeException{
	
	/**
	 * 분기점(branch)에서 입력해야 할 값이 올바르지 않으면 발생하는 예외
	 */
	private static final long serialVersionUID = 1L;
	public AnswerOutOfBoundException() {
		
	}
	public AnswerOutOfBoundException(String emessage) {
		super(emessage);
	}
}
