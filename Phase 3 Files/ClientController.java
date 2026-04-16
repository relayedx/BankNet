import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class ClientController {
	private Client client;
	public ClientController(Client client){
		this.client = client; 
	}
	
	/// METHODS CALLED FROM GUI
	public void login(String user, String pass) throws ClassNotFoundException, IOException { // This method is called from GUI
		// We call client login with the arguments we are given
		boolean auth = client.login(user, pass); // See if the client is an auth user
		// Request for accounts
		// TODO: Display ATM GUI or Teller GUI (Fosa)
		if (auth) {
			JOptionPane.showMessageDialog(null, "User is a user"); // TODO: These will be replaced w/ gui, placeholder for debugging
		}else {
			JOptionPane.showMessageDialog(null, "Error");
		}
	}
	
	public void logout() throws ClassNotFoundException, IOException {
		boolean logout = client.logout();
		if (logout) { // If they were able to logout
			// TODO: Return back to login (?)
		}
		// TODO: What if they were not able to log out....
	}
	
	public void withdraw(int acctID, float amount, String user) throws ClassNotFoundException, IOException {
		Transaction temp = new Transaction(user, amount, TranType.WITHDRAWAL);
		TransactionMessage msg = client.withdraw(acctID,temp); // We're going to get a transaction msg back so we can have the updated bal and whether it was an error
		if (msg.getStatus() == Status.SUCCESS) { // If we were able to deduct the balance from the account
			// Update GUI
			JOptionPane.showMessageDialog(null, msg.getUpdatedBalance());
		}else {
			JOptionPane.showMessageDialog(null, "You broke.");
			// Else throw error
		}
	
	}
	
	public void deposit (int acctID, float amount, String user) throws ClassNotFoundException, IOException{
		Transaction temp = new Transaction(user, amount, TranType.DEPOSIT); 
		TransactionMessage msg  = client.deposit(acctID, temp);
		if (msg.getStatus() == Status.SUCCESS) { // If we were able to deposit the balance to the account
			// Update GUI
			JOptionPane.showMessageDialog(null, msg.getUpdatedBalance());
		}else {
			JOptionPane.showMessageDialog(null, "You broke.");
			// Else throw error
		}
		
	}
	
	/// These are TELLER OPERATIONS, called from the respective GUI
	public void resetPassword(String user, String pass) throws ClassNotFoundException, IOException{ 
		boolean reset = client.resetPass("he", "yeah");
		if (reset) {
			JOptionPane.showMessageDialog(null, "Password reset");

		}else {
			JOptionPane.showMessageDialog(null, "no.");

		}
	}
	
	public SkeletonAccountMessage createAccount(String user, String accType) throws ClassNotFoundException, IOException{ // TODO: Swap accType with the actual account type later
		// Call create account
		SkeletonAccountMessage acct = client.createAccount(user, accType);
		return acct; // We return this acct so the GUI knows to update w/ this skeleton acct, or display error.
	}
	
	
	// Getting skeleton accounts can be used when user is looked up by teller, AND when user logs in through ATM GUI.
	public ArrayList<Message> getSkelAccts(String user) throws ClassNotFoundException, IOException{
		ArrayList<Message> accts = client.getSkelAccts(user);
		String temp = "";
		for (Message msg : accts) {
			temp += msg + "\n";
		}
		JOptionPane.showMessageDialog(null, temp);
		return accts;
		
	}
	
	public void closeAccount(int acctID) throws ClassNotFoundException, IOException {
		boolean close = client.closeAccount(acctID);
		if (close) {
			JOptionPane.showMessageDialog(null,"Closed");

		}else {
			JOptionPane.showMessageDialog(null, "Error in closing");

		}
	}
	
	
}
