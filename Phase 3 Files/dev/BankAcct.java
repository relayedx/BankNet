package dev;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.time.temporal.ChronoUnit;

public class BankAcct {
	// private static int count = 0;
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
    
	// When account is instantiated during server start up (merged from file-management branch)
	public BankAcct(int id, User owner, AcctType type, List<User> authUsers,
			float balance, float credit, float availCredit, LocalDate due,
			boolean frozen, boolean closed, List<Transaction> trans) {
		this.acctID = id;
		this.owner = owner;
		this.type = type;
		this.authUser = authUsers;
		this.balance = balance;
		this.creditLine = credit;
		this.availCredit = availCredit;
		this.dueDate = due;
		this.frozen = frozen;
		this.closed = closed;
		this.transactions = trans;
	}
    
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
    		creditLine = 4000f;
    		availCredit = creditLine;
    	}
    	this.frozen = frozen;
    	this.closed = closed;
        this.transactions = new ArrayList<Transaction>(transactions);
        this.authUser = new ArrayList<>(authUsers);
    }
    
    public BankAcct(AcctType type, User owner, int id) { // When a user opens an account
    	this.acctID = id;	// gets id from database
    	// (which keeps track of the latest bank id instead of instantiating from 0 at startup)
    	this.owner = owner;
    	this.type = type;
    	if (type != AcctType.Credit) {
    		// Since these are primitive types, we need to set them to something
    		creditLine = 0;
    		availCredit = 0;
    	}
    	if(type == AcctType.Credit) { // If account is a credit account
    		// Account is opened with a line of credit
    		creditLine = 4000f; 
    		availCredit = creditLine;
    	}
    	if(type != AcctType.Checking) {
    		dueDate = LocalDate.now().plusDays(30);
    	}
    	balance = 0;
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

    
    public TransactionMessage withdraw(Transaction trans) {
    	if (trans.getType() == TranType.DEPOSIT) { // Is the transaction a deposit but trying to make a withdrawal
    		return new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.ERROR, trans, acctID, balance); // Return error
    	}
    	if (frozen || closed) { // If this bank account is frozen or closed and user attempts to deposit
    		return new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.ERROR, trans, acctID, balance); // Return error
    	}
        if (type != AcctType.Credit) { // If it's checking or savings
        	float tempBal = Math.round((balance - trans.getAmount()) * 100.0) / 100.0f;
        	
        	if (tempBal < 0) { // If they try and withdraw
        		return new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.ERROR, trans, acctID, balance);
        	}
        	balance = tempBal;
        	transactions.add(trans);
        	return new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.SUCCESS, trans, acctID, balance);
        }else {
        	float tempAvailCredit = Math.round((availCredit - trans.getAmount()) * 100.0) / 100.0f;        	if (tempAvailCredit < 0) {
        		return new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.ERROR, trans, acctID, balance);
        	}
        	balance = Math.round((balance + trans.getAmount()) * 100) / 100f; // We'll just add onto the balance.
        	availCredit = tempAvailCredit;
        	transactions.add(trans);
        	return new TransactionMessage(msgType.WITHDRAWAL_REQUEST,Status.SUCCESS, trans, acctID, availCredit);
        }
    }

    public TransactionMessage deposit(Transaction trans) {
    	if (trans.getType() == TranType.WITHDRAWAL) { // Is the transaction a withdrawal but trying to make a deposit?
    		return new TransactionMessage(msgType.DEPOSIT_REQUEST,Status.ERROR, trans, acctID, balance);
    	}
    	if (frozen || closed) { // If this bank account is frozen or closed and user attempts to deposit
    		return new TransactionMessage(msgType.DEPOSIT_REQUEST,Status.ERROR, trans, acctID, balance);
    	}
        if (type != AcctType.Credit) { // If it's checking or savings
        	float tempBalance = Math.round((balance + trans.getAmount()) * 100) / 100.0f; // We'll just add onto the balance.
        	balance = tempBalance; 
        }else {
        	balance = Math.round((balance - trans.getAmount()) * 100) / 100.0f;
        	availCredit = Math.round((availCredit + trans.getAmount()) * 100) / 100.0f;
        	return new TransactionMessage(msgType.DEPOSIT_REQUEST,Status.SUCCESS, trans, acctID, availCredit);
        	
        }
        transactions.add(trans);
        return new TransactionMessage(msgType.DEPOSIT_REQUEST,Status.SUCCESS, trans, acctID, balance);
    }
    
    public void calculateMonths(LocalDate date, int transCount) { // Used for credit/savings, calculating accured interest

    	long monthsLate = Math.max(0,ChronoUnit.MONTHS.between(dueDate, date));
    	if (monthsLate == 0 || type == AcctType.Checking) { // If there is no due date or the account is checkings 
    		return; // We can return
    	}
    	// If we get here, we know we need to either add to the balance (Savings), or add what they owe (Credit)
    	if (type == AcctType.Savings) { // If we have a savings account
    		float toAdd = Math.round((balance * 0.0125f * monthsLate ) * 100) / 100.0f;
    		Transaction sys = new Transaction(transCount, "System/Interest", toAdd, TranType.DEPOSIT);
    		balance += toAdd;
    		transactions.add(sys);
   
    	}else { // Else, the account is a credit (they owe to the bank
    		// Withdraw only allows credit is below 0, so we would need to override withdraw
    		float interest = Math.round((balance * 0.10f * monthsLate) * 100.0) / 100.0f;
    		balance += interest; // The amount owed would go into your balance
    		availCredit -= interest;  
    		Transaction sys = new Transaction(transCount, "System/Interest", interest, TranType.SYSTEM);
    		transactions.add(sys);
    		
    	}
    	
    }
    
    /// SETTERS AND GETTERS /// 

    public void freezeAcc() {
        if (frozen) {
        	frozen = false;
        }else {
        	frozen = true;
        }
    }

    public boolean closeAcc() {
        if (!closed) { // If we haven't closed the account
        	closed = true; // Let's close it
        	return true; // Return to true to know that it worked
        } // Otherwise, account is already closed.
        return false;
        
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