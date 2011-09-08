package co.firebase.tasks.ui;

import android.graphics.Bitmap;
import co.firebase.tasks.GenericFireTaskResult;

public class BitmapTaskResult extends GenericFireTaskResult<Bitmap> {
	private static final long serialVersionUID = 1L;
	
	public BitmapTaskResult(Bitmap value) {
		this(value, null);
	}
	public BitmapTaskResult(Bitmap value, Exception error) {
		super(value, error);
	}
	
}
