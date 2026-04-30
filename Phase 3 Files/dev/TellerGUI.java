package dev;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

class TellerGUI implements RoleBasedGUI {
	private ClientController clientController;
	private JFrame frame;
	
	
	public TellerGUI(ClientController clientController) {
		this.clientController = clientController;
		
	}
	
	public void launchUI() {
		// GUI frame
		frame = new JFrame("Welcome Teller");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(750,550);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		// GUI Panel holding all components
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0,2));
		
		// creating buttons for teller actions
		JButton createUserBtn = new JButton("Create User");
		JButton createBankAcctBtn = new JButton("Create Bank Acct");
		JButton withdrawBtn = new JButton("Withdraw");
		JButton depositBtn = new JButton("Deposit");
		JButton freezeAcctBtn = new JButton("Freeze Acct");
		JButton closeAcctBtn = new JButton("Close Acct");
		JButton addAuthUser = new JButton("add Authorized User");
		JButton addCreditLine = new JButton("add Credit Line");
		
		// adding action listeners for buttons
		createUserBtn.addActionListener(e -> handleCreateUser());
		createBankAcctBtn.addActionListener(e -> handleCreateBankAcct());
		withdrawBtn.addActionListener(e -> handleWithdraw());
		depositBtn.addActionListener(e -> handleDeposit());
		freezeAcctBtn.addActionListener(e -> handleFreezeAcct());
		closeAcctBtn.addActionListener(e -> handleCloseAcct());
		addAuthUser.addActionListener(e -> handleAddAuthUser());
		addCreditLine.addActionListener(e -> handleAddCreditLine());
		
		// adding buttons to panel
		mainPanel.add(createUserBtn);
		mainPanel.add(createBankAcctBtn);
		mainPanel.add(withdrawBtn);
		mainPanel.add(depositBtn);
		mainPanel.add(addAuthUser);
		mainPanel.add(addCreditLine);
		
		frame.add(mainPanel);
		frame.setVisible(true);
	}
	
	public void closeUI() {
		if (frame != null) {
			frame.dispose();
		}
	}
	
	public void handleCreateUser() {
		UserInfo[] userInfo = showCreateUserDialog();
		if (userInfo[0] != null) {
			String username = JOptionPane.showInputDialog(frame, "Enter username:");
			String password = JOptionPane.showInputDialog(frame, "Enter password:");
			try {
				boolean result = clientController.createUser(userInfo[0], username, password);
				if (result) {
					JOptionPane.showMessageDialog(frame, "User successfully created!");
				} else {
					JOptionPane.showMessageDialog(frame, "User could not be created.");
				}
			} catch (Exception e) {
				
			}
		}
	}
	
	public void handleCreateBankAcct() {
		
	}
	
	public void handleWithdraw() {
		
	}
	
	public void handleDeposit() {
		
	}
	
	public void handleFreezeAcct() {
		
	}
	
	public void handleCloseAcct() {
		
	}
	
	public void handleAddAuthUser() {
		
	}
	
	public void handleAddCreditLine() {
		
	}
	
	private UserInfo[] showCreateUserDialog() {
	    JDialog dialog = new JDialog(frame, "Create New User", true); // true = modal (blocks until closed)
	    dialog.setSize(350, 300);
	    dialog.setLocationRelativeTo(frame);
	    dialog.setLayout(new BorderLayout());

	    // Form panel
	    JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
	    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

	    formPanel.add(new JLabel("First Name:"));
	    JTextField firstNameField = new JTextField();
	    formPanel.add(firstNameField);

	    formPanel.add(new JLabel("Last Name:"));
	    JTextField lastNameField = new JTextField();
	    formPanel.add(lastNameField);

	    formPanel.add(new JLabel("Current Address:"));
	    JTextField addressField = new JTextField();
	    formPanel.add(addressField);
	    
	    formPanel.add(new JLabel("Date Of Birth (Ex: 2000-11-03):"));
	    JTextField dobField = new JTextField();
	    
	    formPanel.add(new JLabel("Phone Number:"));
	    JTextField phoneNumField = new JTextField();

	    // Button panel
	    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    JButton confirmBtn = new JButton("Create");
	    JButton cancelBtn = new JButton("Cancel");
	    btnPanel.add(cancelBtn);
	    btnPanel.add(confirmBtn);

	    // Result array — null means cancelled
	    UserInfo[] result = {null};

	    confirmBtn.addActionListener(e -> {
	        String firstName = firstNameField.getText().trim();
	        String lastName = lastNameField.getText().trim();
	        String address = addressField.getText().trim();
	        String strDOB = dobField.getText().trim();
	        String phoneNum = phoneNumField.getText().trim();

	        if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty()
	        		|| strDOB.isEmpty() || phoneNum.isEmpty()) {
	            JOptionPane.showMessageDialog(dialog, "All fields have not been filled!");
	            return;
	        }
	        
	        LocalDate dob = LocalDate.parse(strDOB);
	        result[0] = new UserInfo(firstName, lastName, address, dob, phoneNum);
	        
	        dialog.dispose();
	    });

	    cancelBtn.addActionListener(e -> dialog.dispose());

	    dialog.add(formPanel, BorderLayout.CENTER);
	    dialog.add(btnPanel, BorderLayout.SOUTH);
	    dialog.setVisible(true); // blocks here until dialog is closed because modal=true

	    return result;
	}
	
}