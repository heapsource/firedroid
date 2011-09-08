package co.firebase.tasks.ui;

import java.net.URL;

import android.graphics.Bitmap;
import android.widget.ImageView;
import co.firebase.tasks.FireTaskUIHandler;

public class BitmapImageViewHandler extends FireTaskUIHandler<URL,Integer,BitmapTaskResult>{
	private ImageView view;
	private Bitmap inProgressImage, defaultImage;
	
	public BitmapImageViewHandler() {
		this(null);
	}
	
	public BitmapImageViewHandler(ImageView view) {
		setImageView(view);
	}
	
	public ImageView getImageView() {
		return view;
	}

	public void setImageView(ImageView view) {
		this.view = view;
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		if(view != null) {
			if(this.defaultImage != null) {
				this.view.setImageBitmap(this.defaultImage);
			}
		}
	}
	
	@Override
	public void onPostExecute(BitmapTaskResult result) {
		super.onPostExecute(result);
		if(view != null) {
			if(result.isSuccess() && result.getValue() != null) {
				this.view.setImageBitmap(result.getValue());
			}else {
				if(this.defaultImage != null) {
					this.view.setImageBitmap(this.defaultImage);
				}
			}
		}
	}
	
	@Override
	public void onCancelled() {
		super.onCancelled();
		if(view != null) {
			if(this.defaultImage != null) {
				this.view.setImageBitmap(this.defaultImage);
			}
		}
	}
	
	@Override
	public void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
	@Override
	public void onTaskUnbinded() {
		super.onTaskUnbinded();
	}

	public Bitmap getInProgressImage() {
		return inProgressImage;
	}
	
	public BitmapImageViewHandler setInProgressImage(Bitmap inProgressImage) {
		this.inProgressImage = inProgressImage;
		return this;
	}

	public Bitmap getDefaultImage() {
		return defaultImage;
	}

	public BitmapImageViewHandler setDefaultImage(Bitmap defaultImage) {
		this.defaultImage = defaultImage;
		return this;
	}
}
