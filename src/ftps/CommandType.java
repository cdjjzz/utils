package ftps;

public enum CommandType {
	USER("USER"),PASS("PASS"),LIST("LIST"),
	PORT("PORT"),QUIT("QUIT"),CWD("CWD"),
	GET("GET"),STOR("STOR"),LS("LS"),
	SHOW("SHOW"),EXIT("EXIT");
	
	private String value;

	CommandType(String value) {
		this.value=value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	
	
	
	

}
