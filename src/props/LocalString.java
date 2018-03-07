package props;

public class LocalString implements CharSequence{
	
	
	
	private String msg;
	
	public LocalString(String msg){
		this.msg=msg;
	}
	@Override
	public int length() {
		return msg.length();
	}

	@Override
	public char charAt(int index) {
		return msg.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return msg.subSequence(start, end);
	}
	@Override
	public String toString() {
		return msg;
	}

}
