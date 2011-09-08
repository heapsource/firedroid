package co.firebase.tasks.specialized.forms;

public class FormFieldError {
	private String fieldName;
	private String[] messages;
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setMessages(String[] messages) {
		this.messages = messages;
	}
	public String[] getMessages() {
		return messages;
	}
}
