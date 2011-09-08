package co.firebase.tasks.specialized.forms;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

public class UIDateField  extends UIField {
	private String value;
	
	public UIDateField() {
		
	}
	
	public UIDateField(String dataFieldName, int hintResource) {
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
	public View createUI(final Context context) {
		Button text = new Button(context);
		text.setHint(this.getHintResourceId());
		text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
		        int mYear = c.get(Calendar.YEAR);
		        int mMonth = c.get(Calendar.MONTH);
		        int mDay = c.get(Calendar.DAY_OF_MONTH);
		        
				DatePickerDialog dialog = new DatePickerDialog(context,new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						
						
					}
					
				},mYear,mMonth,mDay );
				dialog.show();
			}
		});
		return text;
	}
	
}