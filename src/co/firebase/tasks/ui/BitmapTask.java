package co.firebase.tasks.ui;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import co.firebase.tasks.FireTask;

public class BitmapTask extends FireTask<URL, Integer, BitmapTaskResult> {
	public static final String LOG_TAG = "BitmapTask";

	@Override
	protected BitmapTaskResult doInBackground(URL... params) {
		URL url = params[0];
		try {

			HttpURLConnection c = (HttpURLConnection) url.openConnection();
			c.setDoInput(true);
			c.connect();
			InputStream is = c.getInputStream();
			Bitmap img = null;
			img = BitmapFactory.decodeStream(is);

			return new BitmapTaskResult(img);
		} catch (Exception e) {
			return new BitmapTaskResult(null, e);
		}
	}

}
