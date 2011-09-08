package co.firebase.tasks.specialized.forms;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

public class UITextField extends UIField {
	private String value;
	
	public UITextField() {
		
	}
	
	public UITextField(String dataFieldName, int hintResource) {
		this.setHintResourceId(hintResource);
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
		EditText text = new EditText(context);
		text.setHint(this.getHintResourceId());
		return text;
	}
	
}
