package co.firebase.tasks.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import co.firebase.tasks.FireTaskFilter;

/**
 * Add cache capabilities to the BitmapTask task.
 * @author Johan Hernandez<johan@firebase.co>
 *
 */
public class BitmapTaskCacheFilter extends FireTaskFilter<URL, BitmapTaskResult>{
	public static final String LOG_TAG = "BitmapTaskCacheFilter";
	private boolean disableCacheWrite;
	private boolean disableCacheRead;
	
	
	private static Bitmap getBitmapFromCache(Context context, String url) {
		if (url != null) {

			if (context != null) {
				File dir = context.getCacheDir();
				String fileName = url.hashCode() + ".png";
				File f = new File(dir, fileName);
				if (f.exists()) {
					return BitmapFactory.decodeFile(f.toString());
				}
			}
		}

		return null;
	}
	Bitmap cachedBitmap;
	@Override
	public boolean canRespond(URL[] params) {
		URL url = params[0];
		String urlString = url.toString();
		if(!disableCacheRead && this.targetTask().hostActivity() != null) {
			cachedBitmap = getBitmapFromCache(this.targetTask().hostActivity(), urlString);
			if(cachedBitmap != null) {
				Log.d(LOG_TAG,String.format("Cache used for %s", urlString));
				return true;
			} else {
				Log.d(LOG_TAG,String.format("Cache missing for %s", urlString));
			}
		} else {
			Log.d(LOG_TAG,String.format("Cache skipped for %s", urlString));
		}
		return super.canRespond(params);
	}

	@Override
	public BitmapTaskResult onRespond(URL[] params) {
		return new BitmapTaskResult(cachedBitmap);
	}

	@Override
	public BitmapTaskResult onPostExecute(BitmapTaskResult result, URL[] params) {
		BitmapTaskResult baseResult = super.onPostExecute(result, params);
        if(result.isSuccess() && !disableCacheWrite && this.targetTask().hostActivity() != null) {
        	URL url = params[0];
        	String urlString = url.toString();
        	saveBitmapToCache(this.targetTask().hostActivity(), urlString, result.getValue());
        }
		return baseResult;
	}

	private static void saveBitmapToCache(Context context, String url, Bitmap bitmap){
		if (bitmap != null) {

			if (context != null) {
				File dir = context.getCacheDir();
				String fileName = url.hashCode() + ".png";
				File f = new File(dir, fileName);
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(f);
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				} catch (FileNotFoundException e) {
					Log.w(LOG_TAG, "Error save bitmap from url: " + url + " to " + f);
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {

						}
					}
				}

			}
		}
	}

	/**
	 * Disable Cache Reads that leads to ignore the existing cache.
	 */
	public void disableCacheRead() {
		this.disableCacheRead = true;
	}
	
	/**
	 * Disable any updates to the cache.
	 */
	public void disableCacheWrite() {
		this.disableCacheWrite = true;
	}
}
