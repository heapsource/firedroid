package co.firebase.tasks.specialized.json;

import org.json.JSONArray;
import org.json.JSONObject;

import co.firebase.tasks.GenericFireTaskResult;

public class JSONResult extends GenericFireTaskResult<Object>{

	private static final long serialVersionUID = 1L;

	public JSONResult(Object value) {
		this(value, null);
	}
	
	public JSONResult(Object value, Exception error) {
		super(value, error);
		
	}
	public JSONObject getValue() {
		return (JSONObject)super.getValue();
	}
	
	public JSONArray getArrayValue() {
		return (JSONArray)super.getValue();
	}
}
