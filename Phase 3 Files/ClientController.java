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
}
