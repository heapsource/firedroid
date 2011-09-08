package co.firebase.tasks.specialized.forms;

import java.util.ArrayList;
import java.util.List;

public class FormResult {
	private ArrayList<FormFieldError> errors = null;
	public FormResult() {
		
	}
	
	public List<FormFieldError> fieldErrors() {
		if(errors == null) {
			errors = new ArrayList<FormFieldError>();
		}
		return errors;
	}

	public boolean hasFieldErrors() {
		return errors != null && errors.size() > 0;
	}
}
