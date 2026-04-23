package dev;

public class CreateAccountMessage extends Message {
	private final String user;
	private final String acctType; // TODO: replace
	public CreateAccountMessage(msgType type, Status status, String user, String acctType) {
		super(type,status);
		this.user = user;
		this.acctType = acctType;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getAcctType() {
		return acctType;
	}
}
