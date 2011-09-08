package co.firebase.tasks.specialized.forms;

import android.content.Context;
import android.view.View;

public abstract class UIField {
	private String dataFieldName;
	private int hintResourceId;
	//private String errorMessage;
	
	public String getDataFieldName() {
		return dataFieldName;
	}
	
	public void setDataFieldName(String dataFieldName) {
		this.dataFieldName = dataFieldName;
	}
	
	public int getHintResourceId() {
		return hintResourceId;
	}
	
	public void setHintResourceId(int hintResourceId) {
		this.hintResourceId = hintResourceId;
	}
	
	/*
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	*/
	
	public abstract String getValue();
	
	public abstract void setValue(String value);
	
	public abstract View createUI(Context context);
}
