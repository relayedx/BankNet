import java.io.IOException;

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
}
