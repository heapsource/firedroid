package co.firebase.tasks.specialized.forms;

public class FormValidationException extends Exception {
	private static final long serialVersionUID = 1L;
	private FormFieldError field;
	public FormValidationException(String message, FormFieldError field) {
		super(message);
		this.field = field;
	}
	
	public FormFieldError getField() {
		return field;
	}
}
