import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI implements RoleBasedGUI {
	ClientController clientController;
	private JFrame frame;
	private JTextField userField;
	private JPasswordField passField;
	private JLabel errorLabel;
	private JButton loginButton;
	
	public LoginGUI(ClientController clientController) {
		this.clientController = clientController;
	}
	
	public void launchUI() {
		// GUI frame
		frame = new JFrame("BankNet Login");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(350, 250);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		// GUI panel
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		
		// creating Grid Structure/Rules
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        // Username row
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        userField = new JTextField();
        gbc.gridy = 1;
        panel.add(userField, gbc);

        // Password row
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        passField = new JPasswordField();
        gbc.gridy = 3;
        panel.add(passField, gbc);
        
        // Login button
        loginButton = new JButton("Log In");
        gbc.gridy = 4;
        panel.add(loginButton, gbc);
        
        // loginBtn action listener
        loginButton.addActionListener(e -> handleLogin());
        frame.add(panel);
        frame.setVisible(true);
	}
	
	public void handleLogin() {
		
	}
	
	
	public void closeUI() {
		if (frame != null) {
			frame.dispose();
		}
	}
}
