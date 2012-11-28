package ds02.server.exception;

public class PriceStepException extends Exception {

	private static final long serialVersionUID = 1L;

	public PriceStepException() {
		super();
	}

	public PriceStepException(String s, Throwable cause) {
		super(s, cause);
	}

	public PriceStepException(String s) {
		super(s);
	}
}
