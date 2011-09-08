package co.firebase.tasks.ui;

import android.widget.TextView;
import co.firebase.tasks.FireTaskResult;
import co.firebase.tasks.FireTaskUIHandler;

public class TextHandler<Params, Progress, Result extends FireTaskResult<?>>
		extends FireTaskUIHandler<Params, Progress, Result> {
	private TextView view;
	private CharSequence originalText, inProgressText, cancelledText,
			executedText;

	public TextHandler(TextView view) {
		this.view = view;
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		if (this.view == null)
			return;
		
		originalText = this.view.getText();
		if(this.inProgressText != null) {
			this.view.setText(inProgressText);
		}

	}

	@Override
	public void onPostExecute(Result result) {
		super.onPostExecute(result);
		if (view == null)
			return;
		if (this.executedText != null) {
			view.setText(this.executedText);
		} else {
			view.setText(originalText);
		}
	}

	@Override
	public void onCancelled() {
		super.onCancelled();
		if (view == null)
			return;
		if (this.cancelledText != null) {
			view.setText(this.cancelledText);
		} else {
			view.setText(originalText);
		}
	}
	@Override
	public void onTaskUnbinded() {
		super.onTaskUnbinded();
		this.view = null;
	}

	public TextHandler<Params, Progress, Result> setInProgressText(CharSequence inProgressText) {
		this.inProgressText = inProgressText;
		return this;
	}

	public CharSequence getInProgressText() {
		return inProgressText;
	}

	public TextHandler<Params, Progress, Result> setCancelledText(CharSequence cancelledText) {
		this.cancelledText = cancelledText;
		return this;
	}

	public CharSequence getCancelledText() {
		return cancelledText;
	}

	public TextHandler<Params ,Progress, Result> setExecutedText(CharSequence executedText) {
		this.executedText = executedText;
		return this;
	}

	public CharSequence getExecutedText() {
		return executedText;
	}

}
