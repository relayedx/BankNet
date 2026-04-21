import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AtmGUI implements RoleBasedGUI {
	private JFrame frame;
	private ClientController clientController;
	
	public AtmGUI(ClientController clientController) {
		this.clientController = clientController;
	}
	
	public void launchUI() {
		// GUI frame
		frame = new JFrame("Welcome Customer!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1050, 750);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		
		
		
		frame.setVisible(true);
	}
	
	public void closeUI() {
		if (frame != null) {
			frame.dispose();
		}
	}
}
