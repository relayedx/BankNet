import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
	private List<bankAcct> accountsDB;
	private ArrayList<User> users;
	private String usersFile = "AllUsers.txt";
	private String AccountsFile = "AllAccounts.txt";
	private static int counting;
	
	public void loadData() {
		char defaultDelimiter;
		char userDelimiter;
		char transactionDelimiter;
		char transactionDetailsDelimiter;
		
	}

	public User getUser(String username) {
		User user = new User();
		return user;
	}
	
	// + getUsers(ArrayList<String>) : ArrayList<User>
	
	public bankAcct getAccount(int acctID) {
		bankAcct account = new bankAcct();
		return account;
	}
	
	// + getAccounts(ArrayList<int>) : ArrayList<bankAcct>
	
	public ArrayList<bankAcct> getUserAllAccounts(String user) {
		ArrayList<bankAcct> accounts = null;
		return accounts;
	}

	public void addUser(String user, String pass, UserInfo info) {
		
	}
	
	public void addAccount(UserInfo user, acctType type) {
		
	}
	
	public void addAuthUser(UserInfo info, String user, String pass, int acctID) {
		
	}
	
	// + assignAcc(String acctID, String user) : boolean

	public void updateName(String user, String firstName, String lastName) {
		
	}
	
	public void updateAddress(String user, String address) {
		
	}
	
	public void updateDOB(String user, String dob) {
		
	}
	
	public void updatePhone(String user, String phone) {
		
	}
	
	public void updatePassword(String user, String pass) {
		
	}

	public void updateBalance(int acctID, double balance) {
		
	}
	
	public void updateAuthUsers(int acctID, ArrayList<String> authUser) {
		
	}

	// do i really need this / is there repeating write code
	// - writeToAllAccounts(boolean modifying) : void
	// - writeToAllUsers() : void
	// - writeToUserInfo() : void
	// - writeToTransactions() : void
}
