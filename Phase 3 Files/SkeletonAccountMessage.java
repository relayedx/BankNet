public class SkeletonAccountMessage extends Message{
	private final int acctID;
	private final float balance;
	
	public SkeletonAccountMessage(msgType type, Status status,int id, float bal) {
		super(type, status);
		this.acctID = id;
		this.balance = bal;
		// TODO Auto-generated constructor stub
	}
	
	public int getAcctID() {
		return acctID;
	}
	public float getBal() {
		return balance;
	}
	public String toString() {
		String temp = "";
		return temp + "ID: " + acctID + " Balance: balance";
	}
}