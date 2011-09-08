package co.firebase.tasks;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;


/**
 * Base class for Task UI handlers. Derived classes will create specialized behavior for the UI according the signals sent by the binded task.
 * @author Johan Hernandez<johan@firebase.co>
 *
 */
public abstract class FireTaskUIHandler<Params, Progress, Result extends FireTaskResult<?>> {
	FireTask<Params, Progress, Result> currentTaskContext;
	private ArrayList<FireTaskUIHandler<Params, Progress, Result>> children;
	private Activity ownerActivity;
	
	public FireTaskUIHandler() {
		onCollectChildren();
	}
	public Activity activity() {
		return ownerActivity;
	}
	
	/**
	 * Occurs when the Handler is hosted by a Activity. The value can be null when the Handler is no longer necessary.
	 * @param owner Can be null
	 */
	public void setOwnerActivity(Activity owner) {
		ownerActivity = owner;
		if(this.hasChildren()) {
			for(FireTaskUIHandler<Params, Progress, Result> child : children) {
				child.setOwnerActivity(owner);
			}
		}
	}
	
	/**
	 * Gets an intance to the Task to which this Handler is responding.
	 * @return
	 */
	public FireTask<Params, Progress, Result> bindedTask() {
		return currentTaskContext;
	}
	
	/**
	 * Indicates whether the Handler is binded to some task.
	 * @return true if the Handler is binded to a task, otherwise false.
	 */
	public boolean isBinded() {
		return this.currentTaskContext != null;
	}
	
	/**
	 * Occurs when some progress is made in the background operation. Not all the background operations supports Progress notification. Derived class should ALWAYS call the base method, otherwise the children will never be executed.
	 * @param values
	 */
	public void onProgressUpdate(Progress... values) {
		if(this.hasChildren()) {
			for(FireTaskUIHandler<Params ,Progress, Result> child : children) {
				child.onProgressUpdate(values);
			}
		}
	}

	/**
	 * Occurs when the background operation finished and the result are available to be shown in the Views. Any behavior or modification of the View to inform the user that the task has finished it's execution should be done at this point. Derived class should ALWAYS call the base method, otherwise the children will never be executed.
	 * @param result Result of the Background Operation.
	 */
	public void onPostExecute(Result result) {
		if(this.hasChildren()) {
			for(FireTaskUIHandler<Params, Progress, Result> handler : children) {
				handler.onPostExecute(result);
			}
		}
	}
	
	/**
	 * Occurs when the Task is about to be executed. Any behavior or modification of the View to inform the user that the task will be executed should be done at this point. Derived class should ALWAYS call the base method, otherwise the children will never be executed.
	 */
	public void onPreExecute() {
		if(this.hasChildren()) {
			for(FireTaskUIHandler<Params, Progress, Result> handler : children) {
				handler.onPreExecute();
			}
		}
	}
	
	/**
	 * Occurs when the task is cancelled. Derived class should ALWAYS call the base method, otherwise the children will never be executed.
	 */
	public void onCancelled() {
		if(this.hasChildren()) {
			for(FireTaskUIHandler<Params, Progress, Result> handler : children) {
				handler.onCancelled();
			}
		}
	}
	
	/**
	 * Occurs when the Handler is binded to some task. Derived class should ALWAYS call the base method, otherwise the children will never be executed.
	 * @param task
	 */
	public void onTaskBinded(FireTask<Params,Progress, Result> task) {
		this.currentTaskContext = (FireTask<Params, Progress, Result>) task;
		if(this.hasChildren()) {
			for(FireTaskUIHandler<Params, Progress, Result> handler : children) {
				handler.onTaskBinded(task);
			}
		}
	}
	
	/**
	 * Occurs when the Handle is unbinded from some task, also means that this Task will not be used anymore and any reference to Views or Widgets should be released. Derived class should ALWAYS call the base method, otherwise the children will never be executed.
	 */
	public void onTaskUnbinded() {
		if(this.hasChildren()) {
			for(FireTaskUIHandler<Params, Progress, Result> handler : children) {
				handler.onTaskUnbinded();
			}
		}
	}
	
	/**
	 * Use this method to determinate whether the task has children that should be executed.
	 * @return true if the task contains executable child handlers.
	 */
	public boolean hasChildren() {
		return this.children != null;
	}
	
	/**
	 * Returns a list of children. This method always returns a List instance, use the hasChildren method first when possible.
	 * @return
	 */
	public List<FireTaskUIHandler<Params, Progress, Result>> children() {
		if(children == null) {
			children = new ArrayList<FireTaskUIHandler<Params, Progress, Result>>();
		}
		return children;
	}
	
	/**
	 * Override this method to populate the children list.
	 */
	protected void onCollectChildren() {
		
	}
	
	public FireTaskUIHandler<Params, Progress, Result> addChild(FireTaskUIHandler<Params, Progress, Result> child) {
		this.children().add(child);
		return this;
	}
}
