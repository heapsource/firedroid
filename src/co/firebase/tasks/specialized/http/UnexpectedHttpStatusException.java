package co.firebase.tasks.specialized.http;

public class UnexpectedHttpStatusException extends Exception {
	private static final long serialVersionUID = 1L;
	private int statusCode;
	
	public UnexpectedHttpStatusException(String message, int statusCode) {
		super(message);
		setStatusCode(statusCode);
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
