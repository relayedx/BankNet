package dev;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class AtmGUI implements RoleBasedGUI {
	private JFrame frame;
	
	private ClientController clientController;
	private List<Message> userAccts;
	private String user;
	
	public AtmGUI(ClientController clientController, List<Message> userAccts, String user) {
		this.clientController = clientController;
		this.userAccts = userAccts;
		this.user = user;
	}
	
	public void launchUI() {
		// GUI frame
		if (userAccts.size() > 0) {
			AccountMessage currAcct = (AccountMessage) userAccts.get(0);
		}
		frame = new JFrame("Welcome Customer!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 550);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
	    // Main panel that holds all account panels, stacked vertically
	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

	    // Loop through each account and build a row for it
	    for (Message msg : userAccts) {
	        AccountMessage acct = (AccountMessage) msg;

	        // Panel for this individual account row
	        JPanel acctPanel = new JPanel();
	        acctPanel.setLayout(new BorderLayout());
	        acctPanel.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), // bottom divider line
	            BorderFactory.createEmptyBorder(12, 16, 12, 16)                // inner padding
	        ));
	        acctPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // caps the height

	        // Left side — account type and I
	        JPanel leftPanel = new JPanel();
	        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
	        
	        JLabel acctLabel = new JLabel(acct.getAcctType() + " Account #" + acct.getAcctID());
	        acctLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
	        JLabel frozenLabel = new JLabel("Frozen: " + acct.getFrozen());
	        frozenLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
	        leftPanel.add(acctLabel);
	        leftPanel.add(Box.createRigidArea(new Dimension(10, 0)));
	        leftPanel.add(frozenLabel);
	        leftPanel.add(Box.createRigidArea(new Dimension(10, 0)));

	      	        
	        

	        // Center — balance
	        JLabel balanceLabel = new JLabel(String.format("$%,.2f", acct.getBalance()));
	        balanceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
	        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);

	        // Right side — buttons
	        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
	        btnPanel.setOpaque(false);
	        JButton withdrawBtn = new JButton("Withdraw");
	        JButton depositBtn = new JButton("Deposit");
	        JButton transferBtn = new JButton("Transfer Funds");
	        JButton viewTrxBtn = new JButton("View Transactions");
	        JButton freezeBtn = new JButton("Freeze Account");
	        withdrawBtn.addActionListener(e -> handleWithdraw(acct.getAcctID(), balanceLabel, user));
	        depositBtn.addActionListener(e -> handleDeposit(acct.getAcctID(), balanceLabel, user));
	        transferBtn.addActionListener(e -> handleTransfer(acct.getAcctID(), balanceLabel, user));
	        viewTrxBtn.addActionListener(e -> handleViewTrx(acct.getAcctID(), user));
	        freezeBtn.addActionListener(e -> handleFreeze(acct.getAcctID(),frozenLabel));
	        btnPanel.add(withdrawBtn);
	        btnPanel.add(depositBtn);
	        btnPanel.add(transferBtn);
	        btnPanel.add(viewTrxBtn);
	        btnPanel.add(freezeBtn);

	        // add acct details to panel
	        acctPanel.add(leftPanel, BorderLayout.WEST);
	        acctPanel.add(balanceLabel, BorderLayout.CENTER);
	        acctPanel.add(btnPanel, BorderLayout.EAST);
	        
	        // add acctPanel to mainPanel
	        mainPanel.add(acctPanel);
	        
	    }
	    
	    // add logout button and actionListener
	    JButton logoutBtn = new JButton("Logout");
	    logoutBtn.addActionListener(e -> handleLogout());
	    
	    // add reset password btn and actionListener
	    JButton resetPassBtn = new JButton("Reset Password");
	    resetPassBtn.addActionListener(e -> handleResetPass());
	    
	    // add btn's outside acctPanel to mainPanel
	    mainPanel.add(logoutBtn);
	    mainPanel.add(resetPassBtn);

	    // Wrap in a scroll pane in case there are many accounts
	    JScrollPane scrollPane = new JScrollPane(mainPanel);
	    frame.add(scrollPane);
	    frame.setVisible(true);
		
		
		frame.setVisible(true);
	}
	
	public void closeUI() {
		if (frame != null) {
			frame.dispose();
		}
	}
	
	public void handleWithdraw(int acctID, JLabel balanceLabel, String username) {
		// getting withdrawl amount
		String input = JOptionPane.showInputDialog(frame, 
				"Enter amount you'd like to withdraw (see teller if cents needed): ");
		Float amount = strToFloat(input);
		
		if (amount.equals(null)) {
			JOptionPane.showMessageDialog(frame, "Error occured, see teller");
			return;
		}
		// initiate command via clientController
		try {
			Message msg = clientController.withdraw(acctID, amount, username);
			TransactionMessage res = (TransactionMessage) msg;
			String updatedBalance = String.format("$%,.2f", res.getUpdatedBalance());
			balanceLabel.setText(updatedBalance);	
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Error occured, see teller");
		}
	}
	
	public void handleDeposit(int acctID, JLabel balanceLabel, String username) {
		// getting deposit amount
		String input = JOptionPane.showInputDialog(frame, 
				"Enter amount you'd like to deposit (see teller if cents needed): ");
		Float amount = strToFloat(input);
		
		if (amount.equals(null)) {
			JOptionPane.showMessageDialog(frame, "Error occured, see teller");
			return;
		}
		// initiate command via clientController
		try {
			Message msg = clientController.deposit(acctID, amount, username);
			TransactionMessage res = (TransactionMessage) msg;
			String updatedBalance = String.format("$%,.2f", res.getUpdatedBalance());
			JOptionPane.showMessageDialog(frame, updatedBalance);
			balanceLabel.setText(updatedBalance);		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "error occured, see teller");
		}
	}
	
	public void handleTransfer(int outgoingAcctID, JLabel balanceLabel, String username) {
		String input = JOptionPane.showInputDialog(frame, "Enter transfer amount: ");
		String transferToAcctID = JOptionPane.showInputDialog(frame, "Enter Account ID you'd like to transfer to: ");
		
		try {
			Float transferAmt = Float.parseFloat(input);
			int incomingAcctID = Integer.parseInt(transferToAcctID);
			TransactionMessage res = clientController.transfer(outgoingAcctID, incomingAcctID, transferAmt, username);
			String updatedBalance = String.valueOf(res.getUpdatedBalance());
			balanceLabel.setText("$" + updatedBalance);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Error occured, see teller");
		}
	}
	
	public void handleViewTrx(int acctID, String username) {
		List<Transaction> myTransactions = new ArrayList<Transaction>();
		try {
			myTransactions = clientController.getTransactions(acctID, username);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Error occured, see teller");
		}
		String acctNum = String.valueOf(acctID);
		String myTrx = "Transactions for Account #" + acctNum + "\n";
		for (Transaction currTrx : myTransactions) {
			myTrx += currTrx.toString() + "\n";
		}
		
		JOptionPane.showMessageDialog(frame, myTrx);
	}
	
	public void handleResetPass() {
		String username = JOptionPane.showInputDialog(frame, "Enter current Username: ");
		String newPassword = JOptionPane.showInputDialog(frame, "Enter NEW password: ");
		
		if (!username.equals("") && !newPassword.equals("")) {
			try {
				Boolean response = clientController.resetPassword(username, newPassword);
				if (response) {
					JOptionPane.showMessageDialog(frame, "Password successfully changed!");
				} else {
					JOptionPane.showMessageDialog(frame, "Password change was unsuccessful");
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frame, "Oh No! An Error occured, see teller");
			}
		}
	}
	
	public void handleLogout() {
		try {
			clientController.logout();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Error occured, see teller");
		}
	}
	
	public void handleFreeze(int acctID, JLabel frozenLabel) {
		try {
			Boolean froze = clientController.freezeAccount(acctID);
			if (froze) {
				if (frozenLabel.getText().contains("false")) {
					frozenLabel.setText("Frozen: true");
				}else {
					frozenLabel.setText("Frozen: false");
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Error occured, see teller");
		}
	}
	
	// for taking user input into float
	public Float strToFloat(String input) {
		try {
			// checking if user entered an amount with cents
			if(input.contains(".")) {
				return null;
			}
			
			// if user enterd value with comma, remove comma
			if (input.contains(",")) {
				input = input.replace(",", "");
			}
			
			// converting withdrawl amount to float to return
			return Float.parseFloat(input);
		} catch (Exception e) {
			return null;
		}
	}
}
