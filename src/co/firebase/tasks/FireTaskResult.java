package co.firebase.tasks;

public interface FireTaskResult<T> {
	public void setError(Exception error);
	public Exception getError();
	public boolean isSuccess();
	public FireTaskResult<T> setSuccessMessage(String successMessage);
	public String getSuccessMessage();
	public FireTaskResult<T> setErrorMessage(String errorMessage);
	public String getErrorMessage();
	public void setInnerResult(FireTaskResult<?> target);
	public FireTaskResult<?> getInnerResult(); 
	public <TOuter extends FireTaskResult<?>> TOuter asInnerOf(TOuter parent);
	public T getValue();
	public FireTaskResult<T> setValue(T value);
}
