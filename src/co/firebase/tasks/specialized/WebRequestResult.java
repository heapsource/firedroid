package co.firebase.tasks.specialized;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.AbstractHttpClient;

import co.firebase.tasks.GenericFireTaskResult;

/**
 * Standard response for a Web HTTP Request.
 * @author Johan Hernandez<johan@firebase.co>
 *
 */
public class WebRequestResult extends GenericFireTaskResult<HttpResponse> {

	private static final long serialVersionUID = 1L;
	private AbstractHttpClient client;
	
	public WebRequestResult(HttpResponse value) {
		this(value, (Exception) null, null);
	}

	public WebRequestResult(HttpResponse value, Exception error) {
		this(value, error, null);
	}

	public WebRequestResult(HttpResponse value, String successMessage) {
		this(value, null, successMessage);
	}

	public WebRequestResult(HttpResponse value, Exception error, String successMessage) {
		super(value, error, successMessage);
	}

	public void setHttpClient(AbstractHttpClient client) {
		this.client = client;
	}

	public AbstractHttpClient getHttpClient() {
		return client;
	}
	
}
