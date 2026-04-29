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


public class CreditAccountTests {
	private BankAcct testAcct;
	
	@BeforeEach
	public void setUp() {
		List<Integer> authAccts = new ArrayList<>();
		authAccts.add(1);
		authAccts.add(2);
		UserInfo info = new UserInfo("first", "last", "add", LocalDate.now(), "phone");
		User user = new User("jerrick", "pass", info, false, authAccts, true);
		testAcct = new BankAcct(AcctType.Credit,user);
	}
	
	@Test
	@DisplayName("Constructor")
	public void constructor() {
		Assertions.assertAll(
			() -> Assertions.assertNotNull(testAcct.getDueDate()),
			() -> Assertions.assertEquals(4000, testAcct.getCredit())
		);
	}
	
	@Test
	@DisplayName("Withdraw Test")
	public void withdraw() {
		Transaction trans1 = new Transaction("User", 10.35f, TranType.WITHDRAWAL);
		for (int i = 0; i < 9; i ++) {
			testAcct.withdraw(trans1);
		}
		TransactionMessage msg = testAcct.withdraw(trans1);
		Assertions.assertAll(
				// Since we are using doubles, we need to allow for a bit of an error 
				() -> Assertions.assertEquals(3896.5, testAcct.getAvailCredit(),0.01),
				() -> Assertions.assertEquals(10, testAcct.getTrans().size()),
				() -> Assertions.assertEquals(103.5, testAcct.getBalance(), 0.01),
				() -> Assertions.assertEquals(Status.SUCCESS,msg.getStatus())
		);
	}
	
	@Test
	@DisplayName("Withdrawal Overdraft Test")
	public void withdrawO() {
		Transaction trans1 = new Transaction("User", 4001, TranType.WITHDRAWAL);
		TransactionMessage msg = testAcct.withdraw(trans1);
		Assertions.assertAll(
				() -> Assertions.assertEquals(Status.ERROR, msg.getStatus()),
				() -> Assertions.assertEquals(4000, testAcct.getAvailCredit()),
				() -> Assertions.assertEquals(0, testAcct.getTrans().size())
		);
	}
	
	@Test
	@DisplayName("Deposit Test")
	public void deposit() {
		Transaction trans1 = new Transaction("User", 10.35f, TranType.DEPOSIT);
		for (int i = 0; i < 9; i ++) {
			testAcct.deposit(trans1);
		}
		TransactionMessage msg = testAcct.deposit(trans1);
		Assertions.assertAll(
				// Since we are using doubles, we need to allow for a bit of an error 
				() -> Assertions.assertEquals(4103.5, testAcct.getAvailCredit(),0.01),
				() -> Assertions.assertEquals(-103.5, testAcct.getBalance(), 0.01),
				() -> Assertions.assertEquals(10, testAcct.getTrans().size()),
				() -> Assertions.assertEquals(Status.SUCCESS,msg.getStatus())
		);
	}
	
	
}
