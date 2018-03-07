package ftps;

public class UserInfo {
	/**
	 * 用户名
	 */
	private String name;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 当前用户所在目录
	 */
	private String path;
	/**
	 * 判断当前用户是否登录
	 */
	private boolean logined;
	/**
	 * 登录时间轴（登录超过30分钟后需重新登录）
	 */
	private long login_time;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isLogined() {
		return logined;
	}
	public void setLogined(boolean logined) {
		this.logined = logined;
	}
	public long getLogin_time() {
		return login_time;
	}
	public void setLogin_time(long login_time) {
		this.login_time = login_time;
	}
	
	
	
}
