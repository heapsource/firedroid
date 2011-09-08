package co.firebase.tasks;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public abstract class FireTask<Params, Progress, Result extends FireTaskResult<?>> {
	public static final String LOG_TAG = "FireTask";

	/**
	 * It can be null if the task is running in synchronous mode.
	 */
	private FireTaskManager taskManager;
	private Result result;
	private FireTaskUIHandler<Params, Progress, Result> ui;

	private Progress[] lastProgress;
	private int tag;
	private operationPhase onPreExecuteOp;
	private operationPhase onPostExecuteOp;
	private operationPhase onProgressUpdateOp;
	private Activity hostActivity;
	private InnerTask innerTask;
	private ArrayList<FireTaskFilter<Params, Result>> filters = null;
	private Params[] callParams;
	private String inProgressDescription;
	private int inProgressDescriptionResource;
	private boolean isSynchronous = false;
	private Context appContext;
	private int requestCode;
	private boolean isInstanceTask = false;
	
	public FireTask() {

	}
	
	public Params[] getParams() {
		return callParams;
	}
	
	public int getRequestCode() {
		return requestCode;
	}



	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}



	public Context applicationContext() {
		return appContext;
	}
	
	public boolean hasFilters() {
		return filters != null && filters.size() > 0;
	}

	public List<FireTaskFilter<Params, Result>> filters() {
		if (filters == null)
			filters = new ArrayList<FireTaskFilter<Params, Result>>();
		return filters;
	}

	private enum operationPhase {
		Pending, Given, Executed
	}

	public void execute(Params... params) {
		this.callParams = params;
		if (this.hasFilters()) {
			for (FireTaskFilter<Params, Result> filter : filters) {
				filter.targetTask = this;
				this.callParams = filter.onPreExecute(this.callParams);
				if (filter.canRespond(this.callParams)) {
					filter.responded = true;
					onPostExecute(filter.onRespond(this.callParams));
					return;
				}
			}
		}

		innerTask = new InnerTask();
		innerTask.execute(this.callParams);
	}
	
	public Result executeSynchronous(Params... params) {
		isSynchronous = true;
		this.callParams = params;
		if (this.hasFilters()) {
			for (FireTaskFilter<Params, Result> filter : filters) {
				filter.targetTask = this;
				this.callParams = filter.onPreExecute(this.callParams);
				if (filter.canRespond(this.callParams)) {
					filter.responded = true;
					Result result = filter.onRespond(this.callParams);
					onPostExecute(result);
					return result;
				}
			}
		}
		Result result = this.doInBackground(params);
		onPostExecute(result);
		return result;
	}

	private class InnerTask extends AsyncTask<Params, Progress, Result> {

		@Override
		protected final void onPreExecute() {
			super.onPreExecute();
			FireTask.this.onPreExecute();
		}

		@Override
		protected Result doInBackground(Params... params) {
			return FireTask.this.doInBackground(params);
		}

		@Override
		protected final void onPostExecute(Result result) {
			super.onPostExecute(result);
			FireTask.this.onPostExecute(result);
		}

		@Override
		protected final void onProgressUpdate(Progress... values) {
			super.onProgressUpdate(values);
			FireTask.this.onProgressUpdate(values);
		}

		@Override
		protected final void onCancelled() {
			super.onCancelled();
			FireTask.this.onCancelled();
		}
	}

	protected abstract Result doInBackground(Params... params);

	public boolean isUIBinded() {
		return ui != null;
	}

	void setManager(FireTaskManager taskManager) {
		this.taskManager = taskManager;
	}
	
	public FireTaskManager getManager() {
		return this.taskManager;
	}

	public Result getResult() {
		return result;
	}

	public Activity hostActivity() {
		return this.hostActivity;
	}

	public void setHostActivity(Activity host) {
		this.hostActivity = host;
		if(host != null) {
			this.appContext = host.getApplicationContext();
			onApplicationContextAvailable();
		}
	}
	
	protected void onApplicationContextAvailable() {
		
		// Initialize the inProgressDescription based on the resource setted(if any).
		if(inProgressDescriptionResource != 0) {
			this.setInProgressDescription(this.applicationContext().getString(this.inProgressDescriptionResource));
		}
	}

	protected final void onPostExecute(Result resultx) {
		Result finalResult = resultx;
		if (this.hasFilters()) {
			for (FireTaskFilter<Params, Result> filter : filters) {
				if(!filter.responded) { //onPostExecute is ommited on the filter that responded the request (if any).
					finalResult = filter.onPostExecute(finalResult, callParams);
				}
				filter.targetTask = null;
			}
		}
		this.result = finalResult;
		if (ui != null) {
			Log.d(LOG_TAG, String.format(
					"onPostExecute operation executed by %s directly on %s",
					this.toString(), ui.toString()));
			sendOnPostExecuteAndFinish();
		} else if(isSynchronous) {
			Log.d(LOG_TAG, String.format(
					"onPreExecute operation executed by %s as synchronous execution",this.toString()));
			sendOnPostExecuteAndFinish();
		}
		else {
			onPostExecuteOp = operationPhase.Given;
			Log.d(LOG_TAG,
					String.format(
							"onPostExecute queued by %s since there is no UI task attached",
							this.toString()));

		}
	}

	protected final void onPreExecute() {
		if (ui != null) {
			ui.onPreExecute();
			onPreExecuteOp = operationPhase.Executed;
			Log.d(LOG_TAG, String.format(
					"onPreExecute operation executed by %s directly on %s",
					this.toString(), ui.toString()));
		} else if(isSynchronous) {
			Log.d(LOG_TAG, String.format(
					"onPreExecute operation executed by %s as synchronous execution",this.toString()));
		}
		else {
			onPreExecuteOp = operationPhase.Given;
			Log.d(LOG_TAG,
					String.format(
							"onPreExecute queued by %s since there is no UI task attached",
							this.toString()));

		}
	}

	protected final void onProgressUpdate(Progress... values) {
		lastProgress = values;
		if (ui != null) {
			ui.onProgressUpdate(values);
			onProgressUpdateOp = operationPhase.Executed;
			Log.d(LOG_TAG, String.format(
					"onProgressUpdate operation executed by %s directly on %s",
					this.toString(), ui.toString()));
		} else if(isSynchronous) {
			Log.d(LOG_TAG, String.format(
					"onProgressUpdate operation executed by %s as synchronous execution",this.toString()));
		}
		else {
			onProgressUpdateOp = operationPhase.Given;
			Log.d(LOG_TAG,
					String.format(
							"onProgressUpdate queued by %s since there is no UI task attached",
							this.toString()));
		}
	}

	protected final void onCancelled() {
		if (this.ui != null) {
			this.ui.onCancelled();
			Log.d(LOG_TAG,
					String.format("onCancelled executed on %s and UI %s ",
							this.toString(), ui.toString()));
		} else {
			Log.d(LOG_TAG, String.format(
					"onCancelled executed on %s while it was unbinded from UI",
					this.toString()));
		}
		finishTask();
	}

	@SuppressWarnings("unchecked")
	void setUITask(FireTaskUIHandler<?, ?, ?> incomingTask) {

		FireTaskUIHandler<Params, Progress, Result> task = (FireTaskUIHandler<Params, Progress, Result>) incomingTask;

		if (task == null) {
			onUIUnbinded();
			if (ui != null) {
				ui.setOwnerActivity(null);
			}
			Log.d(LOG_TAG,
					String.format(
							"%s was unbinded from UI task, all operations will be queued",
							this.toString()));
			ui = null;
		} else {
			ui = task;
			onUIBinded();
			Log.d(LOG_TAG, String.format(
					"%s is now the new UI for Async Task %s", task.toString(),
					this.toString()));
			if (onPreExecuteOp == operationPhase.Given
					|| onPreExecuteOp == operationPhase.Executed) {
				ui.onPreExecute();
				Log.d(LOG_TAG, String.format(
						"onPreExecute post-executed by %s in UI %s",
						this.toString(), ui.toString()));
			}
			if (onProgressUpdateOp == operationPhase.Given) {
				ui.onProgressUpdate(this.lastProgress);
				onProgressUpdateOp = operationPhase.Pending;
				Log.d(LOG_TAG, String.format(
						"onProgressUpdate post-executed by %s in UI %s",
						this.toString(), ui.toString()));
			}
			if (onPostExecuteOp == operationPhase.Given) {
				Log.d(LOG_TAG, String.format(
						"onPostExecute post-executed by %s in UI %s",
						this.toString(), ui.toString()));
				sendOnPostExecuteAndFinish();
			}
		}
	}

	private void sendOnPostExecuteAndFinish() {
		if(ui != null ) {
			ui.onPostExecute(this.result);
		}
		onProgressUpdateOp = operationPhase.Pending;

		finishTask();
	}
	private void finishTask() {
		if(this.taskManager != null) {
			this.taskManager.remove(this);
		}
		this.setUITask(null);
		this.setHostActivity(null);

		Log.d(LOG_TAG,
				String.format("%s reached the end of it's lifetime",
						this.toString()));
	}

	protected void onUIUnbinded() {
		if (ui != null) {
			ui.onTaskUnbinded();
		}
	}

	protected void onUIBinded() {
		if (ui != null) {
			ui.onTaskBinded(this);
		}
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getTag() {
		return tag;
	}

	public void cancel(boolean interrupt) {
		if (this.innerTask != null) {
			this.innerTask.cancel(interrupt);
		}
	}
	
	public FireTask<Params, Progress, Result> setInProgressDescription(String inProgressDescription) {
		this.inProgressDescription = inProgressDescription;
		return this;
	}

	public String getInProgressDescription() {
		return inProgressDescription;
	}

	public FireTask<Params, Progress, Result> setInProgressDescription(
			int inProgressDescriptionResource) {
		this.inProgressDescriptionResource = inProgressDescriptionResource;
		return this;
	}
	public int getInProgressDescriptionResource() {
		return inProgressDescriptionResource;
	}

	public class UIHandler extends FireTaskUIHandler<Params, Progress, Result> {
		
	}
	
	void setIsInstanceTask(boolean isInstanceTask) {
		this.isInstanceTask = isInstanceTask;
	}
	public boolean isInstanceTask() {
		return this.isInstanceTask;
	}
}
