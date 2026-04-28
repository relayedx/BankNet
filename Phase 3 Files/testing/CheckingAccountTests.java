package testing;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.BankAcct;
import dev.TranType;
import dev.Transaction;
import dev.User;
import dev.UserInfo;
import dev.AcctType;


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
		testAcct.deposit(trans1);
		System.out.println("testBal: " + testAcct.getBalance());
		Assertions.assertTrue(testAcct.getBalance() > 100);
	}
	
	@Test
	@DisplayName("Deposit Test - Wrong Transaction Type")
	public void depositWT() {
		Transaction trans1 = new Transaction("User", 10, TranType.WITHDRAWAL);
		Assertions.assertFalse(testAcct.deposit(trans1));
	}
	
	@Test
	@DisplayName("Withdrawal Test")
	public void withdrawTest() {
		Transaction trans1 = new Transaction("User", 10, TranType.WITHDRAWAL);
		testAcct.withdraw(trans1);
		System.out.println(testAcct.getBalance());
		Assertions.assertTrue(testAcct.getBalance() < 100);
	}
	
	@Test
	@DisplayName("Withdrawal Overdraft Test")
	public void overdraft() {
		Transaction trans1 = new Transaction("User", 110, TranType.WITHDRAWAL);
		Assertions.assertFalse(testAcct.withdraw(trans1));
	}
	
	@Test
	@DisplayName("Withdrawal Test - Wrong Transaction Type")
	public void withdrawalWT() {
		Transaction trans1 = new Transaction("User", 10, TranType.DEPOSIT);
		Assertions.assertFalse(testAcct.withdraw(trans1));
	}
	
	

}
