package dev;
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
	private static int count = 0;
    private User owner;
    private List<User> authUser;
    private final int acctID;
    private float balance;
    private float availCredit;
    private AcctType type;
    private boolean frozen;
    private boolean closed;
    private List<Transaction> transactions;
    private float creditLine;
    private LocalDate dueDate = null;

    /*
    public BankAcct(String parse, ArrayList<Transaction> trans) { // When our account is instantiated when server starts
        this.authUser = new ArrayList<>(); // TODO: will need to instatiate below from parsed
        this.transactions = new ArrayList<Transaction>(trans);
        this.acctID = ++count;
    }
    */
    
    public BankAcct(int acctID, AcctType type, User owner, float balance,
    		boolean frozen, boolean closed, String dueDate, List<User> authUsers, List<Transaction> transactions) { // from server loadAccounts
    	this.acctID = acctID;
    	this.owner = owner;
    	this.type = type;
    	this.balance = balance;
    	if (!dueDate.equals("null")) {
    		this.dueDate = LocalDate.parse(dueDate);
    	}
    	if(type == AcctType.Credit) {
    		creditLine = 40;
    		availCredit = creditLine;
    	}
    	this.frozen = frozen;
    	this.closed = closed;
        this.transactions = new ArrayList<Transaction>(transactions);
        this.authUser = new ArrayList<>(authUsers);
    }
    
    public BankAcct(AcctType type, User owner) { // When a user opens an account
    	this.acctID = ++count;
    	this.owner = owner;
    	this.type = type;
    	if (type != AcctType.Checking && type != AcctType.Savings) {
    		dueDate = LocalDate.now().plusDays(30);
    		// Since these are primitive types, we need to set them to something
    		creditLine = 0;
    		availCredit = 0;
    	}
    	balance = 0;
    	if(type == AcctType.Credit) {
    		creditLine = 40;
    		availCredit = creditLine;
    	}
    	frozen = false;
    	closed = false;
        this.transactions = new ArrayList<Transaction>();
        this.authUser = new ArrayList<>();
        authUser.add(owner);
    	

    }
    
    public String toString() {
    	String str = "";
    	str += String.valueOf(acctID) + "|";
    	if (type == AcctType.Checking) {
    		str += "checking|";
    	} else if (type == AcctType.Savings) {
    		str += "savings|";
    	} else {
    		str += "credit|";
    	}
    	str += "$" + String.valueOf(balance) + "|";
    	if (frozen) {str += "true|";} else {str += "false|";}
    	if (closed) {str += "true|";} else {str += "false|";}
    	if (dueDate != null) {str += dueDate.toString() + "|";} else {
    		str += "null|";
    	}
    	str += owner.getUsername() + "|";
    	if (authUser.size() > 1) {
    		int size = 1;
	    	for (User authUser: authUser) {
	    		if (size == this.authUser.size()) {
	    			str += authUser.getUsername() + "\n";
	    		} else {
	    			str += authUser.getUsername() + ",";
	    		}
	    		size += 1;
	    	}
	    	
    	} else {
    		str += owner.getUsername() + "|\n";
    	}
    	return str;
    }

    public boolean withdraw(Transaction trans) {
    	if (frozen || closed) { // If this bank account is frozen or closed and user attempts to deposit
    		return false; // Return error
    	}
        if (type != AcctType.Credit) { // If it's checking or savings
        	float tempBal = balance - trans.getAmount();
        	if (tempBal < 0) { // If they try and withdraw
        		System.out.println("Balance would overdraft acct, error!");
        		return false;
        	}
        	balance = tempBal;
        	transactions.add(trans);
        }else {
        	float tempAvailCredit = availCredit - trans.getAmount();
        	if (tempAvailCredit < 0) {
        		System.out.println("Withdrawal goes below availbile credit, error!");
        		return false;
        	}
        	balance -= trans.getAmount(); // We'll just add onto the balance.
        	System.out.println("New Balance: " + balance);
        	availCredit -= trans.getAmount();
        	System.out.println("New Availible Credit: " + availCredit);
        }
        return false;
    }

    public boolean deposit(Transaction trans) {
    	if (frozen || closed) { // If this bank account is frozen or closed and user attempts to deposit
    		return false; // Return error
    	}
        if (type != AcctType.Credit) { // If it's checking or savings
        	balance += trans.getAmount(); // We'll just add onto the balance.
        }else {
        	balance -= trans.getAmount();
        	availCredit += trans.getAmount();
        	System.out.println("New Availible Credit: " + availCredit);
        	
        }
        System.out.println("New Balance: " + balance);
        transactions.add(trans);
        return true;
    }

    public void freezeAcc(boolean frozen) {
        this.frozen = frozen;
    }

    public void closeAcc(boolean closed) {
        this.closed = closed;
    }


    public User getOwner() {
        return owner;
    }

    public List<User> getAuths() {
        return authUser;
    }
    
    public boolean addAuthUser(User user) {
    	for (User users : authUser) { // If the user is already added 
    		if(users.getUsername().equals(user.getUsername()) && users.getPassword().equals(user.getPassword())) {
    			return false;  // We'll return false (user was not added)
    		}
    	}
    	// If we get here, we know the user is a new user to be added
    	authUser.add(user);
    	return true;
    }
    
    public void removeAuth(User user) {
    	authUser.removeIf(item -> item.getUsername() == user.getUsername() && item.getPassword() == user.getPassword());
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