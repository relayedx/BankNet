import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

enum AcctType{
	Credit,
	Checking,
	Savings
}

public class BankAcct {

    private User owner;
    private List<User> authUser;
    private int acctID;
    private float balance;
    private AcctType type;
    private boolean frozen;
    private boolean closed;
    private List<Transaction> transactions;
    private float creditLine;
    private LocalDate dueDate;

    public BankAcct(String parse, ArrayList<Transaction> trans) { // When our account is instantiated when server starts
        this.authUser = new ArrayList<>(); // TODO: will be from za parsed
        this.transactions = new ArrayList<Transaction>(trans);
    }
    
    public BankAcct(AcctType type, User owner) { // When a user opens an account

    	this.owner = owner;
    	this.type = type;
    	if (type != AcctType.Checking) {
    		dueDate = LocalDate.now().plusDays(30);
    	}
    	if (type != AcctType.Credit) {
    		balance = 0;
    	}else {
    		balance = creditLine;
    	}
    	frozen = false;
    	closed = false;
        this.transactions = new ArrayList<Transaction>();
        this.authUser = new ArrayList<>();

    	

    }

    public boolean withdraw(Transaction trans) {
        // TODO: implement withdraw logic
        return false;
    }

    public boolean deposit(Transaction trans) {
    	if (frozen || closed) { // If this bank account is frozen or closed and user attempts to deposit
    		return false; // Return error
    	}
        if (type != AcctType.Credit) { // If it's checking or savings
        	balance += trans.getAmount(); // We'll just add onto the balance.
        	System.out.println("New Balance: " + balance);
        }
        transactions.add(trans);
        return true;
    }

    public void freezeAcc(boolean frozen) {
        this.frozen = frozen;
    }

    public void closeAcc(boolean closed) {
        this.closed = closed;
    }

    public void addAuthUser(User user) {
        if (user != null) {
            this.authUser.add(user);
        }
    }

    public User getOwner() {
        return owner;
    }

    public List<User> getAuths() {
        return authUser;
    }

    public int getAcctID() {
        return acctID;
    }

    public float getBalance() {
        return balance;
    }

    public AcctType getType() {
        return type;
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

    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public boolean getClosed() {
    	return closed;
    }
}