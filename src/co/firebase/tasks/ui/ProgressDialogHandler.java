package co.firebase.tasks.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import co.firebase.tasks.FireTask;
import co.firebase.tasks.FireTaskResult;
import co.firebase.tasks.FireTaskUIHandler;

public class ProgressDialogHandler<Params, Progress, Result extends FireTaskResult<?>> extends FireTaskUIHandler<Params, Progress, Result> {
	private ProgressDialog dialog = null;
	private String message = null;
	private String title = null;
	private int titleRes = 0;
	private boolean cancelable = true;
	private boolean cancelsWithInterruption = true;
	
	public ProgressDialog dialog() {
		return this.dialog;
	}
	@Override
	public void onPreExecute() {
		super.onPreExecute();
		if(activity() != null) {
			dialog = createAndPrepareProgressDialog();
			if(dialog != null) {
				dialog.show();
			}
		}
	}
	
	protected ProgressDialog createProgressDialog() {
		ProgressDialog dialog = new ProgressDialog(this.activity());
		return dialog;
	}
	
	ProgressDialog createAndPrepareProgressDialog() {
		dialog = createProgressDialog();
		if (dialog != null) {
			onPrepareProgressDialog();
		}
		return dialog;
	}

	protected void onPrepareProgressDialog() {
		
		if(this.message != null) {
			dialog.setMessage(this.message);
		} else if(this.bindedTask().getInProgressDescription() != null) {
			dialog.setMessage(this.bindedTask().getInProgressDescription());
		}
		
		if (this.title != null) {
			dialog.setTitle(this.title);
		} else if (this.titleRes != 0) {
			dialog.setTitle(this.titleRes);
		}
		if (cancelable) {
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (bindedTask() != null) {
						bindedTask().cancel(cancelsWithInterruption);
					}
				}
			});
		}
	}
	

	@Override
	public void onPostExecute(Result result) {
		super.onPostExecute(result);
		onTaskUnbinded();
	}

	@Override
	public void onCancelled() {
		super.onCancelled();
		onTaskUnbinded();
	}

	@Override
	public void onTaskBinded(FireTask<Params, Progress, Result> task) {
		super.onTaskBinded(task);
	}

	@Override
	public void onTaskUnbinded() {
		super.onTaskUnbinded();
		if(dialog != null) {
			dialog.dismiss();
		}
		dialog = null;
	}

	public FireTaskUIHandler<Params, Progress, Result> setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public FireTaskUIHandler<Params, Progress, Result> setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public FireTaskUIHandler<Params, Progress, Result> setTitle(int titleRes) {
		this.titleRes = titleRes;
		return this;
	}

	public FireTaskUIHandler<Params, Progress, Result> setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
		return this;
	}

	public boolean isCancelable() {
		return cancelable;
	}

	public void setCancelsWithInterruption(boolean cancelsWithInterruption) {
		this.cancelsWithInterruption = cancelsWithInterruption;
	}

	public boolean cancelsWithInterruption() {
		return cancelsWithInterruption;
	}
}
