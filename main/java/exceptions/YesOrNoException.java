package exceptions;

public class YesOrNoException extends RuntimeException{
	
	/**
	 * Y/y나 n/n 중에 입력해야 하는데, 그 외의 값을 입력했을 때 발생하는 예외
	 */
	private static final long serialVersionUID = 1L;
	public YesOrNoException() {
		
	}
	public YesOrNoException(String emessage) {
		super(emessage);
	}
}
