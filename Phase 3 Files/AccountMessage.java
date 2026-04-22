import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountMessage extends Message {
    private final String owner;
    private final List<String> authUser;
    private final int acctID;
    private final float balance;
    private final float availCredit;
    private final AcctType acctType;
    private final boolean frozen;
    private final boolean closed;
    private final List<Transaction> transactions;
    private final float creditLine;
    private final LocalDate dueDate;
    
    AccountMessage(msgType type, Status status,BankAcct acct){
    	super(type,status);
    	owner = acct.getOwner().getUsername();
    	List<String> temp = new ArrayList<String>();
    	for (User user : acct.getAuths()) {
    		temp.add(user.getUsername());
    	}
    	authUser = new ArrayList<String>(temp);
    	acctID = acct.getAcctID();
    	balance = acct.getBalance();
    	availCredit = acct.getAvailCredit();
    	acctType = acct.getType();
    	frozen = acct.getFrozen();
    	closed = acct.getClosed();
    	transactions = new ArrayList<Transaction>(acct.getTrans());
    	creditLine = acct.getCredit();
    	dueDate = acct.getDueDate();
    }
    
    public String getOwner() {
        return owner;
    }

    public List<String> getAuths() {
        return authUser;
    }
    
    public int getAcctID() {
        return acctID;
    }

    public float getBalance() {
        return balance;
    }

    public AcctType getAcctType() {
        return acctType;
    }

    public boolean getFrozen() {
        return frozen;
    }

    public List<Transaction> getTrans() {
        return transactions;
    }
    
    public float getCredit() {
        return creditLine;
    }

    public float getAvailCredit() {
        return availCredit;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public boolean getClosed() {
    	return closed;
    }
}
