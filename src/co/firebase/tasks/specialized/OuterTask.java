package co.firebase.tasks.specialized;

import android.app.Activity;
import co.firebase.tasks.FireTask;
import co.firebase.tasks.FireTaskResult;

public abstract class OuterTask<OuterParams, OuterProgress, OuterResult extends FireTaskResult<?>, InnerParams, InnerProgress, InnerResult extends FireTaskResult<?>>
		extends FireTask<OuterParams, OuterProgress, OuterResult> {
	
	private FireTask<InnerParams, InnerProgress, InnerResult> inner = null;

	public OuterTask(FireTask<InnerParams, InnerProgress, InnerResult> inner) {
		this.inner = inner;
	}
	
	public FireTask<InnerParams, InnerProgress, InnerResult> inner() {
		return inner;
	}

	@Override
	public void setHostActivity(Activity host) {
		super.setHostActivity(host);
		inner.setHostActivity(host);
	}
	
	protected InnerResult executeInner(InnerParams... params) {
		return this.inner().executeSynchronous(params);
	}
}
