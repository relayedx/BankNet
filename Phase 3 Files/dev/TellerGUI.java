package dev;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class TellerGUI implements RoleBasedGUI {
	private ClientController clientController;
	private JFrame frame;
	private String user;
	
	
	public TellerGUI(ClientController clientController, String username) {
		this.clientController = clientController;
		user = username;
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
		mainPanel.setLayout(new GridLayout(0,3));
		
		// creating buttons for teller actions
		JButton createUserBtn = new JButton("Create User");
		JButton createBankAcctBtn = new JButton("Create Bank Acct");
		JButton withdrawBtn = new JButton("Withdraw");
		JButton depositBtn = new JButton("Deposit");
		JButton freezeAcctBtn = new JButton("Freeze Acct");
		JButton closeAcctBtn = new JButton("Close Acct");
		JButton addAuthUserBtn = new JButton("add Authorized User");
		JButton delAuthUserBtn = new JButton("Delete Authorized User");
		JButton viewTransactionsBtn = new JButton("View Transactions");
		JButton updateUserInfoBtn = new JButton("Update User Info");
		JButton logoutBtn = new JButton("logout");
		
		// adding action listeners for buttons
		createUserBtn.addActionListener(e -> handleCreateUser());
		createBankAcctBtn.addActionListener(e -> handleCreateBankAcct());
		withdrawBtn.addActionListener(e -> handleWithdraw());
		depositBtn.addActionListener(e -> handleDeposit());
		freezeAcctBtn.addActionListener(e -> handleFreezeAcct());
		closeAcctBtn.addActionListener(e -> handleCloseAcct());
		addAuthUserBtn.addActionListener(e -> handleAddAuthUser());
		delAuthUserBtn.addActionListener(e ->  handleDeleteAuthUser());
		viewTransactionsBtn.addActionListener(e -> handleViewTransactions());
		updateUserInfoBtn.addActionListener(e -> handleUpdateUserInfo());
		logoutBtn.addActionListener(e -> handleLogout());
		
		// adding buttons to panel
		mainPanel.add(createUserBtn);
		mainPanel.add(createBankAcctBtn);
		mainPanel.add(withdrawBtn);
		mainPanel.add(depositBtn);
		mainPanel.add(freezeAcctBtn);
		mainPanel.add(closeAcctBtn);
		mainPanel.add(addAuthUserBtn);
		mainPanel.add(delAuthUserBtn);
		
		mainPanel.add(viewTransactionsBtn);
		mainPanel.add(updateUserInfoBtn);
		mainPanel.add(logoutBtn);
		
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
				JOptionPane.showMessageDialog(frame, "System error occured.");
			}
		}
	}
	
	public void handleCreateBankAcct() {
		String username = JOptionPane.showInputDialog(frame, "Enter username of customer:");
		String acctType = JOptionPane.showInputDialog(frame, "Enter account type (checking, savings, credit):");
		
		try {
			int result = clientController.createAccount(username, acctType);
			if (result > 0) {
				JOptionPane.showMessageDialog(frame, "Account Successfully created! The customer's account ID is" + result);
			} else {
				JOptionPane.showMessageDialog(frame, "Account could not be created.");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "System error occured");
		}
	}
	
	public void handleWithdraw() {
		// dialog popup
		JDialog dialog = new JDialog(frame, "Assist customer with withdrawal", true); // true = modal (blocks until closed)
	    dialog.setSize(350, 300);
	    dialog.setLocationRelativeTo(frame);
	    dialog.setLayout(new BorderLayout());

	    // Form panel
	    JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
	    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
	    
	    formPanel.add(new JLabel("Enter account ID:"));
	    JTextField acctIDField = new JTextField();
	    formPanel.add(acctIDField);
	    
	    formPanel.add(new JLabel("Enter withdrawal amount:"));
	    JTextField amountField = new JTextField();
	    formPanel.add(amountField);
	    
	    formPanel.add(new JLabel("Enter customer username:"));
	    JTextField usernameField = new JTextField();
	    formPanel.add(usernameField);
	    
	    // Button panel
	    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    JButton confirmBtn = new JButton("Confirm");
	    JButton cancelBtn = new JButton("Cancel");
	    btnPanel.add(cancelBtn);
	    btnPanel.add(confirmBtn);
	    
	    // confirmBtn onClick logic
	    confirmBtn.addActionListener(e -> {
	    	try {
	    		int acctID = Integer.parseInt(acctIDField.getText());
	    		Float amount = Float.parseFloat(amountField.getText());
	    		String username = usernameField.getText();
	    		Message msg = clientController.withdraw(acctID, amount, username);
	    		if (msg.getStatus() == Status.SUCCESS) {
	    			JOptionPane.showMessageDialog(frame, "Transaction was made Successfully");
	    		} else {
	    			JOptionPane.showMessageDialog(frame, "Transaction could not be made.");
	    		}
	    	} catch (Exception error) {
	    		JOptionPane.showMessageDialog(frame, "System error occured");
	    	}
	    	dialog.dispose();
	    });
	    
	    // cancelBtn onClick logic
	    cancelBtn.addActionListener(e -> dialog.dispose());
	    
	    // display
	    dialog.add(formPanel, BorderLayout.CENTER);
	    dialog.add(btnPanel, BorderLayout.SOUTH);
	    dialog.setVisible(true); // blocks here until dialog is closed because modal=true
	}
	
	public void handleDeposit() {
		// dialog popup
		JDialog dialog = new JDialog(frame, "Assist customer with deposit", true); // true = modal (blocks until closed)
	    dialog.setSize(450, 350);
	    dialog.setLocationRelativeTo(frame);
	    dialog.setLayout(new BorderLayout());

	    // Form panel
	    JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
	    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
	    
	    formPanel.add(new JLabel("Enter account ID:"));
	    JTextField acctIDField = new JTextField();
	    formPanel.add(acctIDField);
	    
	    formPanel.add(new JLabel("Enter deposit amount:"));
	    JTextField amountField = new JTextField();
	    formPanel.add(amountField);
	    
	    formPanel.add(new JLabel("Enter customer username:"));
	    JTextField usernameField = new JTextField();
	    formPanel.add(usernameField);
	    
	    // Button panel
	    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    JButton confirmBtn = new JButton("Confirm");
	    JButton cancelBtn = new JButton("Cancel");
	    btnPanel.add(cancelBtn);
	    btnPanel.add(confirmBtn);
	    
	    // confirmBtn onClick logic
	    confirmBtn.addActionListener(e -> {
	    	try {
	    		int acctID = Integer.parseInt(acctIDField.getText());
	    		Float amount = Float.parseFloat(amountField.getText());
	    		String username = usernameField.getText();
	    		Message msg = clientController.deposit(acctID, amount, username);
	    		if (msg.getStatus() == Status.SUCCESS) {
	    			JOptionPane.showMessageDialog(frame, "Transaction was made Successfully");
	    		} else {
	    			JOptionPane.showMessageDialog(frame, "Transaction could not be made.");
	    		}
	    	} catch (Exception error) {
	    		JOptionPane.showMessageDialog(frame, "System error occured");
	    	}
	    	dialog.dispose();
	    });
	    
	    // cancelBtn onClick logic
	    cancelBtn.addActionListener(e -> dialog.dispose());
	    
	    // display
	    dialog.add(formPanel, BorderLayout.CENTER);
	    dialog.add(btnPanel, BorderLayout.SOUTH);
	    dialog.setVisible(true); // blocks here until dialog is closed because modal=true
	    
	}
	
	public void handleViewTransactions() {
		// dialog popup
		JDialog dialog = new JDialog(frame, "Assist customer with deposit", true); // true = modal (blocks until closed)
	    dialog.setSize(450, 350);
	    dialog.setLocationRelativeTo(frame);
	    dialog.setLayout(new BorderLayout());

	    // Form panel
	    JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
	    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
	    
	    formPanel.add(new JLabel("Enter account ID:"));
	    JTextField acctTextField = new JTextField();
	    formPanel.add(acctTextField);
	    
	    formPanel.add(new JLabel("Enter username:"));
	    JTextField usernameTextField = new JTextField();
	    formPanel.add(usernameTextField);
	    
	    // Button panel
	    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    JButton confirmBtn = new JButton("Confirm");
	    JButton cancelBtn = new JButton("Cancel");
	    btnPanel.add(cancelBtn);
	    btnPanel.add(confirmBtn);
	    
	    // confirmBtn onClick logic
	    confirmBtn.addActionListener(e -> {
	    	try {
	    		int acctID = Integer.parseInt(acctTextField.getText());
	    		String username = usernameTextField.getText();
	    		List<Transaction> userTransactions = clientController.getTransactions(acctID, username);
	    		String myTrx = "Transactions for Account #" + acctTextField.getText() + "\n";
	    		for (Transaction currTrx : userTransactions) {
	    			myTrx += currTrx.toString() + "\n";
	    		}
	    		JOptionPane.showMessageDialog(frame, myTrx);
	    	} catch (Exception error) {
	    		JOptionPane.showMessageDialog(frame, "System error occured.");
	    	}
	    	
	    	dialog.dispose();
	    });
	    
	    cancelBtn.addActionListener(e -> dialog.dispose());
	    
	    // display
	    dialog.add(formPanel, BorderLayout.CENTER);
	    dialog.add(btnPanel, BorderLayout.SOUTH);
	    dialog.setVisible(true); // blocks here until dialog is closed because modal=true
	}
	
	public void handleFreezeAcct() {
		String strAcctID = JOptionPane.showInputDialog(frame, "Enter Account ID to freeze account:");
		if (strAcctID.contentEquals("")) return; // If the field is empty, return
		int acctID = Integer.parseInt(strAcctID);
		try {
			Boolean result = clientController.freezeAccount(acctID,user);
			if (result) {
				JOptionPane.showMessageDialog(frame, "Account #" + strAcctID + 
					" successfully frozen!");
			} else {
				JOptionPane.showMessageDialog(frame, "Potential system error, Account #" + strAcctID + 
					" could not be frozen.");
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "System error occurred.");
		}
	}
	
	public void handleCloseAcct() {
		String strAcctID = JOptionPane.showInputDialog(frame, "Enter Account ID to request it to be closed:");
		if (strAcctID.contentEquals("")) return; // If the field is empty, return
		int acctID = Integer.parseInt(strAcctID);
		try {
			Boolean result = clientController.closeAccount(acctID);
			if (result) {
				JOptionPane.showMessageDialog(frame, "Account #" + strAcctID + 
					" successfully closed!");
			} else {
				JOptionPane.showMessageDialog(frame, "Potential system error, Account #" + strAcctID + 
					" could not be closed.");
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "System error occurred.");
		}
	}
	
	public void handleAddAuthUser() {
		String strAcctID = JOptionPane.showInputDialog(frame, "Enter Account ID to add authorized user to:");
		String username = JOptionPane.showInputDialog(frame, "Enter the username of the requested authorized user:");
		if (strAcctID.contentEquals("") || username.contentEquals("")) return; // If the fields are empty, return
		int acctID = Integer.parseInt(strAcctID);
		try {
			Boolean result = clientController.addAuthUser(username, acctID);
			if (result) {
				JOptionPane.showMessageDialog(frame, "User successfully added as an authorized user!");
			} else {
				JOptionPane.showMessageDialog(frame, "Potential system error, user could not be added as an "
					+ "authorized user");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "System error occurred.");
		} 
	}
	
	
	public void handleLogout() {
		try {
			clientController.logout();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "System Error occured");
		}
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
	    formPanel.add(dobField);
	    
	    formPanel.add(new JLabel("Phone Number:"));
	    JTextField phoneNumField = new JTextField();
	    formPanel.add(phoneNumField);

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
	
	private UserInfo[] showUpdatedUserDialog(UserInfo info) {
	    JDialog dialog = new JDialog(frame, "Update User", true); // true = modal (blocks until closed)
	    dialog.setSize(350, 300);
	    dialog.setLocationRelativeTo(frame);
	    dialog.setLayout(new BorderLayout());

	    // Form panel
	    JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
	    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

	    formPanel.add(new JLabel("First Name:"));
	    JTextField firstNameField = new JTextField();
	    firstNameField.setText(info.getFirstName());
	    formPanel.add(firstNameField);

	    formPanel.add(new JLabel("Last Name:"));
	    JTextField lastNameField = new JTextField();
	    lastNameField.setText(info.getLastName());
	    formPanel.add(lastNameField);

	    formPanel.add(new JLabel("Current Address:"));
	    JTextField addressField = new JTextField();
	    addressField.setText(info.getAddress());
	    formPanel.add(addressField);
	    
	    formPanel.add(new JLabel("Date Of Birth (Ex: 2000-11-03):"));
	    JTextField dobField = new JTextField();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    dobField.setText(info.getDOB().format(formatter));
	    formPanel.add(dobField);
	    
	    formPanel.add(new JLabel("Phone Number:"));
	    JTextField phoneNumField = new JTextField();
	    phoneNumField.setText(info.getPhone());
	    formPanel.add(phoneNumField);

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
	
	public void handleUpdateUserInfo() {
		String username = JOptionPane.showInputDialog(frame, "Enter username of user's info to edit");
		UserInfo info = null;
		if (username == null) {
			JOptionPane.showMessageDialog(frame, "Username cannot be blank, try again.");
			return;
		}
		try {
			info = clientController.getUserInfo(username);
			if (info != null) {
				UserInfo[] updated = showUpdatedUserDialog(info);
				if (updated[0] != null) {
					boolean success = clientController.updatedUser(updated[0], username);
					if (success) {
						JOptionPane.showMessageDialog(frame, "Successfully updated user info");
					}else {
						JOptionPane.showMessageDialog(frame, "Error updating user info");
					}
				}
			}else {
				JOptionPane.showMessageDialog(frame,"User not found, check spelling");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Error occured.");
		}
	}
	
	public void handleDeleteAuthUser() {
		String strAcctID = JOptionPane.showInputDialog(frame, "Enter Account ID to delete authorized user from:");
		String username = JOptionPane.showInputDialog(frame, "Enter the username of the requested deleted authorized user:");
		int acctID = Integer.parseInt(strAcctID);
		try {
			Boolean result = clientController.deleteAuthUser(username, acctID);
			if (result) {
				JOptionPane.showMessageDialog(frame, "User successfully deleted as an authorized user!");
			} else {
				JOptionPane.showMessageDialog(frame, "Potential system error, user could not be deleted as an "
					+ "authorized user");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "System error occurred.");
		} 
	}
	
	
}