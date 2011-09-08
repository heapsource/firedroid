package co.firebase.tasks.ui;

import android.view.View;
import co.firebase.tasks.FireTaskResult;
import co.firebase.tasks.FireTaskUIHandler;

public class VisibilityHandler<Params, Progress, Result  extends FireTaskResult<?>> extends FireTaskUIHandler<Params, Progress, Result> {
	private int invisibleFlag = View.GONE;
	private View view = null;
	
	public VisibilityHandler(View view) {
		this.view = view;
	}
	
	@Override
	public void onPreExecute() {
		super.onPreExecute();
		if(this.view != null) {
			this.view.setVisibility(View.VISIBLE);
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
			view.setVisibility(this.invisibleFlag);
		}
	}

	public void setInvisibleFlag(int invisibleFlag) {
		this.invisibleFlag = invisibleFlag;
	}

	public int getInvisibleFlag() {
		return invisibleFlag;
	}
	
}
