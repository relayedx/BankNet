package testing;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.BankAcct;
import dev.TranType;
import dev.Transaction;
import dev.TransactionMessage;
import dev.User;
import dev.UserInfo;
import dev.AcctType;
import dev.Status;


public class CheckingAccountTests {
	private BankAcct testAcct;
	
	@BeforeEach
	public void setUp() {
		List<Integer> authAccts = new ArrayList<>();
		authAccts.add(1);
		authAccts.add(2);
		UserInfo info = new UserInfo("first", "last", "add", LocalDate.now(), "phone");
		User user = new User("jerrick", "pass", info, false, authAccts, true);
		testAcct = new BankAcct(AcctType.Checking,user);
		Transaction trans1 = new Transaction("User", 100, TranType.DEPOSIT);
		testAcct.deposit(trans1);
	}
	
	@Test
	@DisplayName("Constructor Test")
	public void constructorTest() {
		Assertions.assertNotNull(testAcct);
	}
	
	@Test
	@DisplayName("Deposit Test")
	public void depositTest() {
		Transaction trans1 = new Transaction("User", 10.45f, TranType.DEPOSIT);
		TransactionMessage msg = testAcct.deposit(trans1);
		Assertions.assertAll(
				// The msg getting sent back should be a success, balance should not equal to what it was previous, and transactions should be > 1
				() -> Assertions.assertEquals(Status.SUCCESS, msg.getStatus()),
				() -> Assertions.assertNotEquals(100f, msg.getUpdatedBalance()),
				() -> Assertions.assertTrue(testAcct.getTrans().size() > 1),
				() -> Assertions.assertEquals(110.45f, msg.getUpdatedBalance(), 0.001)
		);
	}
	
	@Test
	@DisplayName("Deposit Test - Wrong Transaction Type")
	public void depositWT() {
		Transaction trans1 = new Transaction("User", 100, TranType.WITHDRAWAL);
		TransactionMessage msg = testAcct.deposit(trans1);
		
		Assertions.assertAll(
				() -> Assertions.assertEquals(Status.ERROR, msg.getStatus()),
				() -> Assertions.assertEquals(100f, msg.getUpdatedBalance())
		);
	}
	
	@Test
	@DisplayName("Withdrawal Test")
	public void withdrawTest() {
		Transaction trans1 = new Transaction("User", 10.1f, TranType.WITHDRAWAL);
		testAcct.withdraw(trans1);
		Assertions.assertEquals(89.9, testAcct.getBalance(), 0.001);
	}
	
	@Test
	@DisplayName("Withdrawal Overdraft Test")
	public void overdraft() {
		Transaction trans1 = new Transaction("User", 110, TranType.WITHDRAWAL);
		TransactionMessage msg = testAcct.withdraw(trans1);
		Assertions.assertAll(
				() -> Assertions.assertEquals(Status.ERROR, msg.getStatus()),
				() -> Assertions.assertEquals(100f, msg.getUpdatedBalance()) // Balance should not have changed
		);
	}
	
	@Test
	@DisplayName("Withdrawal Test - Wrong Transaction Type")
	public void withdrawalWT() {
		Transaction trans1 = new Transaction("User", 10, TranType.DEPOSIT);
		TransactionMessage msg = testAcct.withdraw(trans1);
		Assertions.assertEquals(Status.ERROR,msg.getStatus());
	}
	
	@Test
	@DisplayName("Credit Account Attributes Test")
	public void creditNull() { // Tests whether or not a credit was initialized (should be none)
		Assertions.assertAll(
				() -> Assertions.assertNull(testAcct.getDueDate()),
				() -> Assertions.assertEquals(0, testAcct.getAvailCredit()),
				() -> Assertions.assertEquals(0, testAcct.getCredit())
		);
	}

	// The below tests will not be tested in the other types of bank accounts, since the logic will not change //
	@Test
	@DisplayName("Adding Authorized User Test")
	public void addAuth() {
		User user = new User("hii", "pass", null, false, null, true);
		testAcct.addAuthUser(user);
		Assertions.assertTrue(testAcct.getAuths().contains(user));
	}
	
	@Test
	@DisplayName("Adding Repeat Authorized User Test")
	public void addAuthRepeat() {
		User user = new User("jerrick", "pass", null, false, null, true);
		testAcct.addAuthUser(user);
		Assertions.assertEquals(1, testAcct.getAuths().size());
	}
	
	@Test
	@DisplayName("Removing Authorized User Test")
	public void delAuth() {
		User user = new User("jerrick", "pass", null, false, null, true);
		testAcct.removeAuth(user);
		Assertions.assertFalse(testAcct.getAuths().contains(user));
	}
	
	@Test
	@DisplayName("Removing NonExistent Authorized User Test")
	public void delAuthNE() {
		User user = new User("notthere", "pass", null, false, null, true);
		testAcct.removeAuth(user);
		Assertions.assertEquals(1,testAcct.getAuths().size());
	}
	
	@Test
	@DisplayName("Closing Account")
	public void closeAcct() {
		Assertions.assertTrue(testAcct.closeAcc());
	}
	
	@Test
	@DisplayName("Closing an already closed Account")
	public void closeAcctError() {
		testAcct.closeAcc(); // Close the account 
		Assertions.assertFalse(testAcct.closeAcc()); // See if it lets us close it again
	}
	
	@Test
	@DisplayName("Freezing Account")
	public void freezeAcct() {
		testAcct.freezeAcc();
		Assertions.assertTrue(testAcct.getFrozen());
	}
	
	@Test
	@DisplayName("Unfreezing Account")
	public void unfreezeAcct() {
		testAcct.freezeAcc(); // Freeze it
		testAcct.freezeAcc(); // Unfreeze it
		Assertions.assertFalse(testAcct.getFrozen()); // Is it unfrozen?
	}
	

}
