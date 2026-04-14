
public class LoginMessage extends Message{
	private final String user;
	private final String pass;
	public LoginMessage(msgType type, Status status, String user, String pass) {
		super(type,status);
		this.user = user;
		this.pass = pass;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getPass() {
		return pass;
	}
}
