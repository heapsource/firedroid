package co.firebase.tasks.specialized.forms;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class UISpinnerField extends UIField {
	private String value;
	private int stringArrayResource;
	
	public int getStringArrayResource() {
		return stringArrayResource;
	}

	public void setStringArrayResource(int stringArrayResource) {
		this.stringArrayResource = stringArrayResource;
	}

	public UISpinnerField() {
		
	}
	
	public UISpinnerField(String dataFieldName, int stringArrayResource) {
		this.setStringArrayResource(stringArrayResource);
		this.setDataFieldName(dataFieldName);
	}
	
	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public View createUI(Context context) {
		Spinner view = new Spinner(context);
		if(stringArrayResource != 0) {
			ArrayAdapter adapter = ArrayAdapter.createFromResource( context, stringArrayResource , android.R.layout.simple_spinner_item); adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			view.setAdapter(adapter);
		}
		return view;
	}
	
}