
public class CreateUserMessage extends Message{
	private final String info;
	private final String user;
	private final String pass;
	
	public CreateUserMessage(msgType type, Status status, String info, String user, String pass) {
		super(type,status);
		this.user = user;
		this.pass = pass;
		this.info = info;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getPass() {
		return pass;
	}
	
	public String getInfo() {
		return info;
	}
}
