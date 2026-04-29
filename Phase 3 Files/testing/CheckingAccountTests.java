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
import dev.msgType;
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
		Transaction trans1 = new Transaction("User", 10, TranType.DEPOSIT);
		TransactionMessage msg = testAcct.deposit(trans1);
		Assertions.assertAll(
				// The msg getting sent back should be a success, balance should not equal to what it was previous, and transactions should be > 1
				() -> Assertions.assertEquals(Status.SUCCESS, msg.getStatus()),
				() -> Assertions.assertNotEquals(100f, msg.getUpdatedBalance()),
				() -> Assertions.assertTrue(testAcct.getTrans().size() > 1)
		);
	}
	
	@Test
	@DisplayName("Deposit Test - Wrong Transaction Type")
	public void depositWT() {
		Transaction trans1 = new Transaction("User", 10, TranType.WITHDRAWAL);
		TransactionMessage msg = testAcct.deposit(trans1);
		
		Assertions.assertAll(
				() -> Assertions.assertEquals(Status.ERROR, msg.getStatus()),
				() -> Assertions.assertEquals(100f, msg.getUpdatedBalance())
		);
	}
	
	@Test
	@DisplayName("Withdrawal Test")
	public void withdrawTest() {
		Transaction trans1 = new Transaction("User", 10, TranType.WITHDRAWAL);
		testAcct.withdraw(trans1);
		Assertions.assertTrue(testAcct.getBalance() < 100);
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

	
	

}
