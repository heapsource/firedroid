package co.firebase.tasks;

/**
 * Base class for Filters. Can be added to FireTask instances to pre-process, post-process or even replace requests and results.
 * The other of the calls is as follows: onPreExecute, canRespond, onRespond, onPostExecute.
 * All the members are executed in the UI Thread.
 * @author Johan Hernandez<johan@firebase.co>
 *
 * @param <Params> The Params type of the target FireTask.
 * @param <Result> The Result type of the Target FireTask.
 */
public class FireTaskFilter<Params, Result extends FireTaskResult<?>> {
	boolean responded;
	FireTask<Params,?, Result> targetTask;
	
	public FireTask<Params,?, Result> targetTask() {
		return targetTask;
	}
	
	/**
	 * Invoked when the params for the target rake task has been received and the task is about to be invoked. This is the last change to modify the parameters.
	 * @param params
	 */
	public Params[] onPreExecute(Params[] params) {
		return params;
	}
	
	/**
	 * Invoked to determinate if the Filter can respond to the execution request itself instead letting the scheduler invoke the background operation.
	 * @param params
	 * @return true if the Filter can handler the execution by itself in the onRespond method, otherwise false to allow the scheduler invoke the background operation.
	 */
	public boolean canRespond(Params[] params) {
		return false;
	}
	
	/**
	 * Invoked to get the results of the task(only in case canRespond returned true).
	 * @param params
	 * @return The response for the Task execution.
	 */
	public Result onRespond(Params[] params) {
		return null;
	}
	
	/**
	 * Invoked when the results for the task are available. This method is only executed when the results comes from the execution of the background operation.
	 * @param result
	 * @param params
	 * @return
	 */
	public Result onPostExecute(Result result, Params[] params) {
		return result;
	}
}
