package co.firebase.tasks.specialized.xml;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import co.firebase.tasks.FireTask;
import co.firebase.tasks.FireTaskResult;
import co.firebase.tasks.specialized.OuterTask;

public class XmlTask<InnerParams, InnerProgress>
		extends
		OuterTask<InnerParams, InnerProgress, XmlResult, InnerParams, InnerProgress, FireTaskResult<String>> {

	public XmlTask(
			FireTask<InnerParams, InnerProgress, FireTaskResult<String>> inner) {
		super(inner);
	}

	@Override
	protected XmlResult doInBackground(InnerParams... params) {
		FireTaskResult<String> jsonStringResult = this.executeInner(params);

		if (jsonStringResult.isSuccess()) {

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();

				StringReader reader = new StringReader(
						jsonStringResult.getValue());
				InputSource inputSource = new InputSource(reader);
				Document doc = builder.parse(inputSource);
				reader.close();

				return new XmlResult(doc);
			}  catch (Exception e) {
				e.printStackTrace();
				return new XmlResult(null, e);
			} 
		} else {
			XmlResult result = new XmlResult(null);
			result.setInnerResult(jsonStringResult);
			return result;
		}
	}

}
