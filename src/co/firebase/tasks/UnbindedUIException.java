package co.firebase.tasks;

/**
 * Occurs when the method AsyncActivity.onBindUITask does not returns an instance of the Issued task.
 * @author Johan Hernandez<johan@firebase.co>
 *
 */
@SuppressWarnings("rawtypes")
public class UnbindedUIException extends Exception {

	FireTask issuedTask;
	
	private static final long serialVersionUID = 1L;
	
	public UnbindedUIException(String detailMessage, FireTask issuedTask) {
		super(detailMessage);
		this.issuedTask = issuedTask;
	}
	
	public FireTask getIssuedTask() {
		return issuedTask;
	}
}
