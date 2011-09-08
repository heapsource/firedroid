package co.firebase.tasks.specialized.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import co.firebase.tasks.FireTask;
import co.firebase.tasks.FireTaskResult;
import co.firebase.tasks.GenericFireTaskResult;
import co.firebase.tasks.specialized.OuterTask;
import co.firebase.tasks.specialized.WebRequestResult;

public class HttpStringResponseTask<OuterParams, OuterProgress> extends OuterTask<OuterParams, OuterProgress, FireTaskResult<String>, OuterParams, OuterProgress, WebRequestResult> {

	public HttpStringResponseTask(
			FireTask<OuterParams, OuterProgress, WebRequestResult> inner) {
		super(inner);
	}

	@Override
	protected FireTaskResult<String> doInBackground(OuterParams... params) {
		WebRequestResult webResult = this.executeInner(params);
		if(webResult.isSuccess()){
			InputStream is;
			try {
				is = webResult.getValue().getEntity().getContent();
			} catch (Exception e) {
				return webResult.asInnerOf(new GenericFireTaskResult<String>(null,e));
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (Exception e) {
				return webResult.asInnerOf(new GenericFireTaskResult<String>(null,e));
			} finally {
				try {
					reader.close();
				} catch (Exception e) {
					return webResult.asInnerOf(new GenericFireTaskResult<String>(null,e));
				}
			}
			return webResult.asInnerOf(new GenericFireTaskResult<String>(sb.toString()));
		}
		return webResult.asInnerOf(new GenericFireTaskResult<String>(null));
	}
}
