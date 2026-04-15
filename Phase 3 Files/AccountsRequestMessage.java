
public class AccountsRequestMessage extends Message {
	private final String user;
	public AccountsRequestMessage(msgType type, Status status, String user) {
		super(type,status);
		this.user = user;
	}
	
	public String getUser() {
		return user;
	}
}
