package ftps;

public enum CommandType {
	USER("USER"),PASS("PASS"),LIST("LIST"),
	PORT("PORT"),QUIT("QUIT"),CWD("CWD"),
	GET("GET"),STOR("STOR");
	
	private String value;

	private CommandType(String value) {
		this.value=value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	
	
	

}
