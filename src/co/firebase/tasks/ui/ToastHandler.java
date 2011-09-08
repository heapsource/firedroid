package co.firebase.tasks.ui;

import android.widget.Toast;
import co.firebase.tasks.FireTaskResult;
import co.firebase.tasks.FireTaskUIHandler;

public class ToastHandler<Params, Progress, Result extends FireTaskResult<?>> extends FireTaskUIHandler<Params, Progress, Result> {
	private String inProgressMessage;
	private String errorMessage;
	private String successMessage;
	private boolean showInProgressMessage = true;
	private boolean showErrorMessage = true;
	private boolean showSuccessMessage = true;
	private int inProgressMessageDuration = Toast.LENGTH_SHORT;
	private int errorMessageDuration = Toast.LENGTH_LONG;
	private int successMessageDuration = Toast.LENGTH_SHORT;
	

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		if(this.activity() != null && showInProgressMessage) {
			String text = inProgressMessage;
			if(text == null && this.bindedTask().getInProgressDescription() != null) {
				text = this.bindedTask().getInProgressDescription();
			}
			if(text != null) {
				Toast.makeText(this.activity() , text, inProgressMessageDuration).show();
			}
		}
	}
	
	@Override
	public void onPostExecute(Result result) {
		super.onPostExecute(result);
		if(this.activity() != null) {
			if(result.isSuccess() && showSuccessMessage) {
				String successMsg = successMessage;
				if(successMsg == null) {
					successMsg = result.getSuccessMessage();
				}
				if(successMsg != null) {
					Toast.makeText(this.activity() , successMsg, successMessageDuration).show();
				}
			} else if(!result.isSuccess() && showErrorMessage) {
				String errorMsg = errorMessage;
				if(errorMsg == null && result.getErrorMessage() != null) {
					errorMsg = result.getErrorMessage();
				}
				if(errorMsg != null) {
					Toast.makeText(this.activity() , errorMsg, errorMessageDuration).show();
				}
			}
		}
	}

	public ToastHandler<Params, Progress, Result> hideInProgressMessage() {
		showInProgressMessage = false;
		return this;
	}
	
	public ToastHandler<Params, Progress, Result> hideErrorMessage() {
		showErrorMessage = false;
		return this;
	}
	
	public ToastHandler<Params, Progress, Result> hideSuccessMessage() {
		showSuccessMessage = false;
		return this;
	}
	
	public String getInProgressMessage() {
		return inProgressMessage;
	}

	public ToastHandler<Params, Progress, Result> setInProgressMessage(String inProgressMessage) {
		this.inProgressMessage = inProgressMessage;
		return this;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public ToastHandler<Params, Progress, Result> setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public ToastHandler<Params, Progress, Result> setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
		return this;
	}

	public int getInProgressMessageDuration() {
		return inProgressMessageDuration;
	}

	public ToastHandler<Params, Progress, Result> setInProgressMessageDuration(int inProgressMessageDuration) {
		this.inProgressMessageDuration = inProgressMessageDuration;
		return this;
	}

	public int getErrorMessageDuration() {
		return errorMessageDuration;
	}

	public ToastHandler<Params, Progress, Result> setErrorMessageDuration(int errorMessageDuration) {
		this.errorMessageDuration = errorMessageDuration;
		return this;
	}

	public int getSuccessMessageDuration() {
		return successMessageDuration;
	}

	public ToastHandler<Params, Progress, Result> setSuccessMessageDuration(int successMessageDuration) {
		this.successMessageDuration = successMessageDuration;
		return this;
	}
	
	
}
