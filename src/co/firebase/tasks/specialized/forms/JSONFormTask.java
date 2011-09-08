package co.firebase.tasks.specialized.forms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.firebase.tasks.FireTask;
import co.firebase.tasks.FireTaskResult;
import co.firebase.tasks.GenericFireTaskResult;
import co.firebase.tasks.specialized.OuterTask;
import co.firebase.tasks.specialized.json.JSONResult;

public class JSONFormTask<InnerParams, InnerProgress> 
extends OuterTask<InnerParams, InnerProgress, FireTaskResult<FormResult>,InnerParams, InnerProgress, JSONResult> {

	public JSONFormTask(FireTask<InnerParams, InnerProgress, JSONResult> inner) {
		super(inner);
	}

	@Override
	protected FireTaskResult<FormResult> doInBackground(InnerParams... params) {
		JSONResult jResult = this.executeInner(params);
		GenericFireTaskResult<FormResult> result = new GenericFireTaskResult<FormResult>();
		FormResult form = new FormResult();
		result.setValue(form);
		if(jResult.isSuccess()) {
			JSONObject obj = jResult.getValue();
			if(obj != null) {
				if(obj.has("errors")) {
					try {
						JSONArray errorsList = obj.getJSONArray("errors");
						for(int i = 0;i < errorsList.length(); i++) {
							JSONObject errorField = (JSONObject)errorsList.get(i);
							FormFieldError field = new FormFieldError();
							field.setFieldName(errorField.getString("field"));
							JSONArray message = (JSONArray) errorField.get("message");
							if(message != null) {
								String[] fieldMessages = new String[message.length()];
								for(int j = 0;j < message.length();j++) {
									fieldMessages[j] = (String)message.get(j);
								}
							}
							form.fieldErrors().add(field);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return jResult.asInnerOf(result);
	}
	
}
