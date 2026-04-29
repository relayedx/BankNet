package dev;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

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
		JButton addAuthUser = new JButton("add Authorized User");
		JButton addCreditLine = new JButton("add Credit Line");
		
		// adding action listeners for buttons
		createUserBtn.addActionListener(e -> handleCreateUser());
		createBankAcctBtn.addActionListener(e -> handleCreateBankAcct());
		withdrawBtn.addActionListener(e -> handleWithdraw());
		depositBtn.addActionListener(e -> handleDeposit());
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
		
	}
	
	public void handleCreateUser() {
		
	}
	
	public void handleCreateBankAcct() {
		
	}
	
	public void handleWithdraw() {
		
	}
	
	public void handleDeposit() {
		
	}
	
	public void handleAddAuthUser() {
		
	}
	
	public void handleAddCreditLine() {
		
	}
	
	
	
	
}