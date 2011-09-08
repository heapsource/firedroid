package co.firebase.tasks;

public interface FireTaskResult<T> {
	public void setError(Exception error);
	public Exception getError();
	public boolean isSuccess();
	public void setSuccessMessage(String successMessage);
	public String getSuccessMessage();
	public void setErrorMessage(String errorMessage);
	public String getErrorMessage();
	public void setInnerResult(FireTaskResult<?> target);
	public FireTaskResult<?> getInnerResult(); 
	public <TOuter extends FireTaskResult<?>> TOuter asInnerOf(TOuter parent);
	public T getValue();
	public void setValue(T value);
}
