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
		System.out.println("Calling to see if " + user + pass + " is authenticatdd");
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
}
