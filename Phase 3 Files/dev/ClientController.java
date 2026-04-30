package dev;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ClientController {
	private Client client;
	private RoleBasedGUI currentGUI;
	
	public ClientController(Client client){
		this.client = client;
		this.currentGUI = new LoginGUI(this);
		currentGUI.launchUI();
	}
	
	/// METHODS CALLED FROM GUI
	public void login(String username, String password) throws ClassNotFoundException, IOException { // This method is called from GUI
		// We call client login with the arguments we are given
		Message res = client.login(username, password); // See if the client is an auth user
		if (res.getStatus() == Status.ERROR) {
			JOptionPane.showMessageDialog(null, "Could not successfully login");
			return;
		}
		
		if (res.getStatus() == Status.SUCCESS) {
			String role = res.getText();
			if (role.equals("teller")) {
				// this is a teller so we can open their portal
				currentGUI.closeUI();
				currentGUI = new TellerGUI(this);
				currentGUI.launchUI();
				return;
			} else {
				// this is a customer so we can req for their accounts
				List<Message> listOfAccts = client.getAccts(username);
				System.out.println(listOfAccts);
				currentGUI.closeUI();
				currentGUI = new AtmGUI(this, listOfAccts);
				currentGUI.launchUI();
			}
		}
	}
	
	public void logout() throws ClassNotFoundException, IOException {
		boolean logout = client.logout();
		if (logout) { // If they were able to logout
			currentGUI.closeUI();
			currentGUI = new LoginGUI(this);
			currentGUI.launchUI();
		}
	}
	
	public Message withdraw(int acctID, float amount, String user) throws ClassNotFoundException, IOException {
		Transaction temp = new Transaction(user, amount, TranType.WITHDRAWAL);
		TransactionMessage msg = client.withdraw(acctID, temp); // We're going to get a transaction msg back.. 
		// ..so we can have the updated bal and whether it was an error
		return (Message) msg;
	}
	
	public Message deposit (int acctID, float amount, String user) throws ClassNotFoundException, IOException{
		Transaction temp = new Transaction(user, amount, TranType.DEPOSIT); 
		TransactionMessage msg  = client.deposit(acctID, temp);
		return (Message) msg;
		
	}
	
	public TransactionMessage transfer(int outgoingAcctID, int incomingAcctID, float amount, String user) 
			throws ClassNotFoundException, IOException {
		TransactionMessage msg = client.transfer(outgoingAcctID, incomingAcctID, amount, user);
		return msg;
	}
	
	public List<Transaction> getTransactions(int acctID, String username) throws ClassNotFoundException, IOException {
		List<Transaction> transactions = client.getTransactions(acctID, username);
		return transactions;
	}
	
	public Boolean resetPassword(String username, String newPassword) throws ClassNotFoundException, IOException{ 
		boolean reset = client.resetPass(username, newPassword);
		return reset;
	}
	
	public AccountMessage createAccount(String user, String accType) throws ClassNotFoundException, IOException { 
		// TODO: Swap accType with the actual account type later
		// Call create account
		AcctType type = AcctType.parseAcctType(accType);
		AccountMessage acct = client.createAccount(user, type);
		return acct; // We return this acct so the GUI knows to update w/ this skeleton acct, or display error.
	}
	
	
	// Getting skeleton accounts can be used when user is looked up by teller, AND when user logs in through ATM GUI.
	public List<Message> getSkelAccts(String user) throws ClassNotFoundException, IOException{
		List<Message> accts = client.getAccts(user);
		String temp = "";
		for (Message msg : accts) {
			temp += msg + "\n";
		}
		JOptionPane.showMessageDialog(null, temp);
		return accts;
		
	}
	
	public AccountMessage getAccount(String acctID) throws ClassNotFoundException, IOException{
		AccountMessage acct = client.getAccount(acctID);
		System.out.println(acct.getOwner());
		return acct;
	}
	
	public void closeAccount(int acctID) throws ClassNotFoundException, IOException {
		boolean close = client.closeAccount(acctID);
		if (close) {
			JOptionPane.showMessageDialog(null,"Closed");

		}else {
			JOptionPane.showMessageDialog(null, "Error in closing");

		}
	}
	
	public void addAuthUser(String user, int acctID) throws ClassNotFoundException, IOException{
		boolean add = client.addAuthUser(user, acctID);
		if (add) {
			JOptionPane.showMessageDialog(null,"User added");

		} else {
			JOptionPane.showMessageDialog(null, "Error in adding authorized user");

		}
	}
	
	public boolean freezeAccount(int acctID) throws ClassNotFoundException, IOException {
		boolean freeze = client.freezeAccount(acctID);
		if (freeze) {
			JOptionPane.showMessageDialog(null,"Freezed/Unfrozen");
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Error in freezing");
			return false;
		}
	}
	
	public boolean createUser(UserInfo userInfo, String user, String pass) throws ClassNotFoundException, IOException {
		boolean created = client.createUser(userInfo, user, pass);
		return created;
	}
	
}