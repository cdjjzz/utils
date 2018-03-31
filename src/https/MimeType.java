package https;

public enum MimeType {
	JSON("application/json;charset=UTF-8"),TEXT("text/plain"),HTML("text/html")
	,IMAGE("IMAGE/*"),FORM("application/x-www-form-urlencoded");
	private String value;

	private MimeType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
