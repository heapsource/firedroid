package co.firebase.tasks.specialized.forms;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.view.ViewGroup;

public class UIForm {
	private ArrayList<UIField> fields = new ArrayList<UIField>();
	
	public List<UIField> fields() {
		return fields;
	}
	
	public void fillUI(ViewGroup view) {
		for(UIField field : fields) {
			view.addView(field.createUI(view.getContext()));
		}
	}

	public List<NameValuePair> getValues() {
		ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();
		for(UIField field : fields) {
			values.add(new BasicNameValuePair(field.getDataFieldName(), field.getValue()));
		}
		return values;
	}
}
