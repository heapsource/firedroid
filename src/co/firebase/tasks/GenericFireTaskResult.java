package co.firebase.tasks;

public class GenericFireTaskResult<T> implements FireTaskResult<T> {

	private FireTaskResult<?> innerResult;

	private T returnedValue;
	private Exception error;
	private String successMessage;
	private String errorMessage;

	public GenericFireTaskResult() {
		this(null, (Exception) null, null);
	}
	
	public GenericFireTaskResult(T value) {
		this(value, (Exception) null, null);
	}

	public GenericFireTaskResult(T value, Exception error) {
		this(value, error, null);
	}

	public GenericFireTaskResult(T value, String successMessage) {
		this(value, null, successMessage);
	}

	public GenericFireTaskResult(T value, Exception error, String successMessage) {
		this.setValue(value);
		setError(error);
		this.setSuccessMessage(successMessage);
	}

	@Override
	public void setError(Exception error) {
		this.error = error;
		if (error != null) {
			this.setErrorMessage(error.getMessage());
		}
		this.setSuccessMessage(successMessage);
	}

	/**
	 * @return the returnedValue
	 */
	@Override
	public T getValue() {
		return returnedValue;
	}

	/**
	 * @return the error that caused the operation to fail.
	 */
	@Override
	public Exception getError() {
		return error;
	}

	/**
	 * Returns a value that indicates whether the operation succeeded or failed.
	 * 
	 * @return
	 */
	@Override
	public boolean isSuccess() {
		return this.getError() == null;
	}

	/**
	 * @param successMessage
	 *            the successMessage to set
	 */
	@Override
	public GenericFireTaskResult<T> setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
		return this;
	}

	/**
	 * @return the successMessage
	 */
	@Override
	public String getSuccessMessage() {
		return successMessage;
	}

	@Override
	public GenericFireTaskResult<T> setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public void setInnerResult(FireTaskResult<?> inner) {
		if (this.getError() == null) {
			this.setError(inner.getError());
		}
		if (this.getErrorMessage() == null) {
			this.setErrorMessage(inner.getErrorMessage());
		}
		if (this.getSuccessMessage() == null) {
			this.setSuccessMessage(inner.getSuccessMessage());
		}
		if (this.getError() == null) {
			this.setError(inner.getError());
		}
		if (this.getErrorMessage() == null) {
			this.setErrorMessage(inner.getErrorMessage());
		}
		this.innerResult = inner;
	}

	@Override
	public FireTaskResult<?> getInnerResult() {
		return innerResult;
	}

	@Override
	public <TOuter extends FireTaskResult<?>> TOuter asInnerOf(TOuter parent) {
		parent.setInnerResult(this);
		return parent;
	}

	@Override
	public GenericFireTaskResult<T> setValue(T value) {
		this.returnedValue = value;
		return this;
	}
}
