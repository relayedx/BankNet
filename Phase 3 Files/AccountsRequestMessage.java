
public class AccountsRequestMessage extends Message {
	private final String user;
	private final int acctID;
	public AccountsRequestMessage(msgType type, Status status, String user) {
		super(type,status);
		this.user = user;
		this.acctID = -1;
	}
	
	public AccountsRequestMessage(msgType type, Status status, String user, int acctID) {
		super(type,status);
		this.user = user;
		this.acctID = acctID;
	}
	
	public AccountsRequestMessage(msgType type, Status status, int acctID) {
		super(type,status);
		this.user = "";
		this.acctID = acctID;
		
	}
	
	public String getUser() {
		return user;
	}
	
	public int getID() {
		return acctID;
	}
}
