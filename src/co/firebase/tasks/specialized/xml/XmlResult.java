package co.firebase.tasks.specialized.xml;

import org.w3c.dom.Document;

import co.firebase.tasks.GenericFireTaskResult;

public class XmlResult extends GenericFireTaskResult<Document>{

	private static final long serialVersionUID = 1L;

	public XmlResult(Document value) {
		this(value, null);
	}
	
	public XmlResult(Document value, Exception error) {
		super(value, error);
		
	}

}