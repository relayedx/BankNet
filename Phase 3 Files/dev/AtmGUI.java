package dev;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class AtmGUI implements RoleBasedGUI {
	private JFrame frame;
	private ClientController clientController;
	private List<Message> userAccts;
	
	public AtmGUI(ClientController clientController, List<Message> userAccts) {
		this.clientController = clientController;
		this.userAccts = userAccts;
	}
	
	public void launchUI() {
		// GUI frame
		frame = new JFrame("Welcome Customer!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1050, 750);
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

	        // Left side — account type and ID
	        JLabel acctLabel = new JLabel(acct.getAcctType() + " Account #" + acct.getAcctID());
	        acctLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

	        // Center — balance
	        JLabel balLabel = new JLabel(String.format("$%,.2f", acct.getBalance()));
	        balLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
	        balLabel.setHorizontalAlignment(SwingConstants.CENTER);

	        // Right side — buttons
	        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
	        btnPanel.setOpaque(false);
	        JButton withdrawBtn = new JButton("Withdraw");
	        JButton depositBtn = new JButton("Deposit");
	        withdrawBtn.addActionListener(e -> handleWithdraw(acct.getAcctID()));
	        depositBtn.addActionListener(e -> handleDeposit(acct.getAcctID()));
	        btnPanel.add(withdrawBtn);
	        btnPanel.add(depositBtn);

	        acctPanel.add(acctLabel, BorderLayout.WEST);
	        acctPanel.add(balLabel, BorderLayout.CENTER);
	        acctPanel.add(btnPanel, BorderLayout.EAST);

	        mainPanel.add(acctPanel);
	    }

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
	
	public void handleWithdraw(int acctID) {
		
	}
	
	public void handleDeposit(int acctID) {
		
	}
}
