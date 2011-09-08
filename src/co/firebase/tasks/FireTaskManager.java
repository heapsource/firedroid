package co.firebase.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.util.Log;

@SuppressWarnings("rawtypes")
public final class FireTaskManager {
	public static final String LOG_TAG = "FireTaskManager";
	HashMap<String, Object> objects;
	ArrayList<FireTask> tasks;
	BindTaskUIHandler uiBinder;
	Activity hostActivity;

	private boolean nonConfigurationInstanceRequested = false;
	
	public FireTaskManager() {
		objects = new HashMap<String, Object>();
		tasks = new ArrayList<FireTask>();
	}

	public HashMap<String, Object> getObjects() {
		return objects;
	}
	
	private void add(FireTask task) {
		task.setManager(this);
		this.tasks.add(task);
	}

	void remove(FireTask task) {
		task.setManager(null);
		this.tasks.remove(task);
	}
	
	public List<FireTask> getTasks() {
		return tasks;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends FireTask> T getExecutingTask(Class<T> taskClass) {
		return (T)getExecutingTask(new ClassTypeTaskPredicate(taskClass));
	}
	
	public FireTask getExecutingTask(ExecutingTaskPredicate predicate) {
		for(FireTask task : tasks) {
			boolean thisIsTheGuy = predicate.isTask(task);
			if(thisIsTheGuy) {
				return task;	
			}
		}
		return null;
	}
	
	public boolean isExecutingTask(Class<? extends FireTask> taskClass) {
		return getExecutingTask(taskClass) != null;
	}
	
	public boolean isExecutingTask(ExecutingTaskPredicate predicate) {
		return getExecutingTask(predicate) != null;
	}
	
	public interface ExecutingTaskPredicate {
		boolean isTask(FireTask task);
	}
	
	public class ClassTypeTaskPredicate implements ExecutingTaskPredicate {
		Class taskClass;
		
		public ClassTypeTaskPredicate(Class taskClass) {
			this.taskClass = taskClass;
		}
		
		@Override
		public boolean isTask(FireTask task) {
			return taskClass.equals(task.getClass());
		}
		
	}

	public List<FireTask> getUIUnbindedTasks() {
		ArrayList<FireTask> unbindedTasks = new ArrayList<FireTask>();
		for(FireTask task : this.getTasks()) {
			if(!task.isUIBinded()) {
				unbindedTasks.add(task);
			}
		}
		return unbindedTasks;
	}

	public void cancelAllTasks() {
		FireTask[] toCancelTasks = new FireTask[ this.getTasks().size()];
		this.tasks.toArray(toCancelTasks);
		for(FireTask task : toCancelTasks) {
			task.cancel(true);
		}
	}
	
	public void cancelAllInstanceTasks() {
		FireTask[] toCancelTasks = new FireTask[ this.getTasks().size()];
		this.tasks.toArray(toCancelTasks);
		for(FireTask task : toCancelTasks) {
			if(task.isInstanceTask()) {
				task.cancel(true);
			}
		}
	}
	

	
	protected boolean shouldCancelAsyncTasksBeforeDestroy() {
		return !nonConfigurationInstanceRequested;
	}
	
	public Object onActivityRetainNonConfigurationInstance() {
		nonConfigurationInstanceRequested = true;
		return this;
	}
	
	public void onActivityDestroy() {
		this.hostActivity = null;
		if(shouldCancelAsyncTasksBeforeDestroy()) {
			// Cancel all Tasks
			this.cancelAllTasks();
		} else {
			// Only cancel Instance tasks.
			this.cancelAllInstanceTasks();
		}
	}
	
	public void onActivityResume() {
		Log.d(LOG_TAG,String.format("onActivityResume in %s", this.toString()));
		bindCurrentTasks();
	}
	
	public void onActivityPause() {
		Log.d(LOG_TAG,String.format("onActivityPause in %s", this.toString()));
		unbindCurrentTasks();
	}

	private void unbindCurrentTasks() {
		Log.d(LOG_TAG,String.format("Unbinding %d async tasks in %s", getTasks().size() , this.toString()));
		for( FireTask<?,?,?> task : getTasks()) {
			if(!task.isInstanceTask()) {
				task.setUITask(null);
			}
		}
	}
	
	void bindCurrentTasks() {

		Log.d(LOG_TAG,String.format("Binding async tasks in %s",this.toString()));
		for( FireTask<?,?,?> task : getUIUnbindedTasks()) {
			if(!task.isInstanceTask()) {
				try {
					bindUITask(task);
				} catch (UnbindedUIException e) {
					Log.w(LOG_TAG, e.getMessage());
				}
			}
		}
	}
	
	private FireTaskUIHandler<?, ?, ?> bindUITask(FireTask<?,?,?> task) throws UnbindedUIException {
		Log.d(LOG_TAG,String.format("Activity %s is requesting UI binding for %s", this.toString(), task.toString()));
		FireTaskUIHandler<?, ?, ?> ui = this.uiBinder.onBindPersistentTaskUI(task);
		return bindUITask(task,ui);
	}
	private FireTaskUIHandler<?, ?, ?> bindUITask(FireTask<?,?,?> task, FireTaskUIHandler<?, ?, ?> handler){
		Log.d(LOG_TAG, String.format("FireTask task is being binded to UI %s", task.toString()));
		handler.setOwnerActivity(this.hostActivity);
		task.setUITask(handler);
		return handler;
	}
	
	public static <TUIBinderActivity extends Activity & BindTaskUIHandler> FireTaskManager getTaskManager(TUIBinderActivity hostActivity) {
		 return getTaskManager(hostActivity, hostActivity);
	}
	
	public static FireTaskManager getTaskManager(Activity hostActivity, BindTaskUIHandler uiBinder) {
		FireTaskManager taskManager = (FireTaskManager)hostActivity.getLastNonConfigurationInstance();
		if(taskManager == null) {
			taskManager = new FireTaskManager();
		}
		taskManager.uiBinder = uiBinder;
		taskManager.hostActivity = hostActivity;
		taskManager.nonConfigurationInstanceRequested = false;
		return taskManager;
	}
	
	public interface BindTaskUIHandler {
		public abstract FireTaskUIHandler<?, ?, ?> onBindPersistentTaskUI(FireTask<?,?,?> task);
	}
	

	/**
	 * Announces the creation of a Task that is persistent across Configuration changes and beyond Activity instance lifetime.
	 * @param <T>
	 * @param task
	 * @return
	 * @throws UnbindedUIException
	 */
	public <T extends FireTask<?,?,?>> T issuePersistentTask(T task) throws UnbindedUIException {
		task.setIsInstanceTask(false);
		bindUITask(task);
		hostTaskInManager(task);
		return task;
	}

	public void hostTaskInManager(FireTask<?, ?, ?> task){
		this.add(task);
		task.setHostActivity(this.hostActivity);
	}
	
	/**
	 * Announces the creation of a Task that if it still running by the time the activity is destroyed the task will be canceled.
	 * @param task The task you want to be running on this activity instance.
	 * @param handler The UI handler for the task.
	 * @throws UnbindedUIException the handler argument was null.
	 */
	public void issueInstanceTask(FireTask<?,?,?> task, FireTaskUIHandler<?, ?, ?> handler)  throws UnbindedUIException {
		if(handler == null) {
			throw new UnbindedUIException("handler argument is required", task);
		}
		task.setIsInstanceTask(true);
		bindUITask(task, handler);
		hostTaskInManager(task);
	}
}