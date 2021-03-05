package br.com.rmc;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 8695447326970408833L;
	
	public BaseException(String message) {
		super(message);
	}
	
	public BaseException(Throwable throwable) {
		super(throwable);
	}
	
	public BaseException(String message, Throwable throwable) {
		super(message, throwable);
	}	

}
