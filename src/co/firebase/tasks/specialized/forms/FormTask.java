package co.firebase.tasks.specialized.forms;

import co.firebase.tasks.FireTask;
import co.firebase.tasks.FireTaskResult;
import co.firebase.tasks.GenericFireTaskResult;
import co.firebase.tasks.specialized.OuterTask;

public class FormTask<OuterParams, OuterProgress>
		extends
		OuterTask<OuterParams, OuterProgress, FireTaskResult<FormResult>, OuterParams, OuterProgress, FireTaskResult<FormResult>> {

	public FormTask(
			FireTask<OuterParams, OuterProgress, FireTaskResult<FormResult>> inner) {
		super(inner);
	}

	@Override
	protected FireTaskResult<FormResult> doInBackground(OuterParams... params) {
		FireTaskResult<FormResult> innerResult = this.executeInner(params);
		FireTaskResult<FormResult> result = new GenericFireTaskResult<FormResult>();
		if (innerResult.isSuccess()) {
			try {
				if (innerResult.getValue().hasFieldErrors()) {
					FormFieldError fieldError = innerResult.getValue()
							.fieldErrors().get(0);
					String fieldErrorMessage = fieldError.getMessages() != null
							&& fieldError.getMessages().length > 0 ? fieldError
							.getMessages()[0] : null;
					throw new FormValidationException(fieldErrorMessage,
							fieldError);
				}
			} catch (Exception ex) {
				result.setError(ex);
			}
			return innerResult.asInnerOf(result);
		}
		return result;
	}
}
