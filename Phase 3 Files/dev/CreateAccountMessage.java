package dev;

public class CreateAccountMessage extends Message {
	private final String user;
	private final AcctType acctType; // TODO: replace
	public CreateAccountMessage(msgType type, Status status, String user, AcctType acctType) {
		super(type,status);
		this.user = user;
		this.acctType = acctType;
	}
	
	public String getUser() {
		return user;
	}
	
	public AcctType getAcctType() {
		return acctType;
	}
}
