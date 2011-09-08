package co.firebase.tasks.specialized.json;

import org.json.JSONObject;

import co.firebase.tasks.GenericFireTaskResult;

public class JSONResult extends GenericFireTaskResult<JSONObject>{

	private static final long serialVersionUID = 1L;

	public JSONResult(JSONObject value) {
		this(value, null);
	}
	
	public JSONResult(JSONObject value, Exception error) {
		super(value, error);
		
	}

}
