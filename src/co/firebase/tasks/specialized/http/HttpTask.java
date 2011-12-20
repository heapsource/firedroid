package co.firebase.tasks.specialized.http;

import java.util.ArrayList;

import org.apache.http.HttpResponse;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

import co.firebase.Base64;
import co.firebase.R;
import co.firebase.tasks.FireTask;
import co.firebase.tasks.specialized.WebRequestParams;
import co.firebase.tasks.specialized.WebRequestResult;

public class HttpTask extends
		FireTask<WebRequestParams, Integer, WebRequestResult> {
	private ArrayList<Integer> successStatusCodes = new ArrayList<Integer>();
	
	/**
	 * All Status codes considered as successful. By default only 200(OK) and 201(Created) are considered as successful status codes.
	 * @return
	 */
	public ArrayList<Integer> successStatusCodes() {
		return successStatusCodes;
	}
	
	public HttpTask() {
		successStatusCodes.add(200);
		successStatusCodes.add(201);
	}
	
	public HttpTask addSuccessStatusCode(int status) {
		this.successStatusCodes.add(status);
		return this;
	}
	
	public HttpTask clearSuccessStatusCodes() {
		this.successStatusCodes.clear();
		return this;
	}
	
	@Override
	protected WebRequestResult doInBackground(WebRequestParams... params) {

		WebRequestParams webParams = params[0];
		
		WebRequestResult result = null;
		AbstractHttpClient httpclient = webParams.getHttpClient() != null ? webParams.getHttpClient() : new DefaultHttpClient();
		if(webParams.getCredentials() != null) {
			webParams.getHttpRequest().setHeader("Authorization", "Basic " + Base64.encodeBytes((webParams.getCredentials().getUserPrincipal().getName() + ":" + webParams.getCredentials().getPassword()).getBytes()));
		}
		if(webParams.hasCookies()) {
			for(Cookie cookie : webParams.cookies()) {
				httpclient.getCookieStore().addCookie(cookie);
			}
		}
		HttpResponse response = null;
		HttpUriRequest request = webParams.getHttpRequest();
		try {
			if (request == null) {
				throw new NullPointerException(
						"http request instance required in WebRequestParams");
			}
			if (webParams.hasBodyValues()) {
				
				if (!(request instanceof HttpEntityEnclosingRequestBase)) {
					throw new Exception(
							String.format("%s http request is not able to send body values", request));
				}
				HttpEntityEnclosingRequestBase entityPost = (HttpEntityEnclosingRequestBase) request;
				entityPost.setEntity(new UrlEncodedFormEntity(webParams
						.bodyValues()));
			}
			response = httpclient.execute(request);
			if(!successStatusCodes.contains(response.getStatusLine().getStatusCode())) {
				Context firedroid = applicationContext();// applicationContext().createPackageContext("co.firebase", Context.CONTEXT_IGNORE_SECURITY);
				String errorMsg = firedroid.getString(R.string.unknown_http_status_exception_message);
				throw new UnexpectedHttpStatusException(errorMsg, response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			result = new WebRequestResult(response, e);
			result.setHttpClient(httpclient);
			return result;
		}
		result = new WebRequestResult(response);
		result.setHttpClient(httpclient);
		return result;
	}

}