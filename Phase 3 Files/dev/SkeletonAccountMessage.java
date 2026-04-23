package dev;
public class SkeletonAccountMessage extends Message{
	private final int acctID;
	private final float balance;
	private final String acctType;
	
	public SkeletonAccountMessage(msgType type, Status status,int id, float bal, String acctType) {
		super(type, status);
		this.acctID = id;
		this.balance = bal;
		this.acctType = acctType;
	
	}
	
	public int getAcctID() {
		return acctID;
	}
	public float getBal() {
		return balance;
	}
	public String getAcctType() {
		return acctType;
	}
	public String toString() {
		String temp = "";
		return temp + "ID: " + acctID + " Balance:" + balance + " Acct Type: " + acctType;
	}
}