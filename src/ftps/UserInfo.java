package ftps;

public class UserInfo {
	/**
	 * �û���
	 */
	private String name;
	/**
	 * ����
	 */
	private String password;
	/**
	 * ��ǰ�û�����Ŀ¼
	 */
	private String path;
	/**
	 * �жϵ�ǰ�û��Ƿ��¼
	 */
	private boolean logined;
	/**
	 * ��¼ʱ���ᣨ��¼����30���Ӻ������µ�¼��
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
