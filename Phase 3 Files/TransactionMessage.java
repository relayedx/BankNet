
public class TransactionMessage extends Message{
	private final Transaction transaction;
	private final float updatedBalance;
	private final int acctID;
	public TransactionMessage(msgType type, Status status, Transaction trans, int bal, int id) {
		super(type,status);
		transaction = trans;
		updatedBalance = bal;
		acctID = id;
	}
	
	public Transaction getTransaction() {
		return transaction;
	}
	
	public float getUpdatedBalance() {
		return updatedBalance;
	}
	
	public int getID() {
		return acctID;
	}
}
