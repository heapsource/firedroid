package co.firebase.tasks.specialized;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.apache.http.auth.Credentials;

/**
 * Standard parameters for a Web HTTP Request.
 * @author Johan Hernandez<johan@firebase.co>
 *
 */
public class WebRequestParams {
	private HttpUriRequest request;
	private List<NameValuePair> bodyValues;
	private AbstractHttpClient client;
	private ArrayList<Cookie> cookies;
	private Credentials credentials;
	
	public WebRequestParams() {
		
	}
	public WebRequestParams(HttpUriRequest request) {
		this.request = request;
	}
	public void setHttpRequest(HttpUriRequest request) {
		this.request = request;
	}

	public HttpUriRequest getHttpRequest() {
		return request;
	}
	
	public List<NameValuePair> bodyValues() {
		if(bodyValues == null) {
			bodyValues = new ArrayList<NameValuePair> ();
		}
		return bodyValues;
	}
	
	public void setBodyValues(List<NameValuePair> values) {
		this.bodyValues = values;
	}
	
	public boolean hasBodyValues() {
		return bodyValues != null && bodyValues.size() > 0;
	}
	
	public void addBodyValue(String name, String value) {
		this.bodyValues().add(new BasicNameValuePair(name,value));
	}
	public void setHttpClient(AbstractHttpClient client) {
		this.client = client;
	}
	public AbstractHttpClient getHttpClient() {
		return client;
	}
	
	public ArrayList<Cookie> cookies() {
		if(cookies == null) {
			cookies = new ArrayList<Cookie>();
		}
		return cookies;
	}
	
	public boolean hasCookies() {
		return cookies != null && cookies.size() > 0;
	}
	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
}
