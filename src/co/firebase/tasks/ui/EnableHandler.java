package co.firebase.tasks.ui;

import android.view.View;
import co.firebase.tasks.FireTaskResult;
import co.firebase.tasks.FireTaskUIHandler;

public class EnableHandler<Params, Progress, Result  extends FireTaskResult<?>> extends FireTaskUIHandler<Params, Progress, Result> {
	private boolean executingEnableState = false;
	private View view = null;
	
	public EnableHandler(View view) {
		this.view = view;
	}
	
	@Override
	public void onPreExecute() {
		super.onPreExecute();
		if(this.view != null) {
			this.view.setEnabled(executingEnableState);
		}
	}

	@Override
	public void onCancelled() {
		super.onCancelled();
		onTaskUnbinded();
	}


	@Override
	public void onTaskUnbinded() {
		if(view != null) {
			view.setEnabled(!executingEnableState);
		}
	}

	public boolean isExecutingEnableState() {
		return executingEnableState;
	}

	public void setExecutingEnableState(boolean executingEnableState) {
		this.executingEnableState = executingEnableState;
	}
	
	
}
