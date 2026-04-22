import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class AccountTester {
	public static void main(String[] args) {
		LocalDate dueDate = LocalDate.now();
		long monthsLate = Math.max(0,ChronoUnit.MONTHS.between(dueDate, LocalDate.now().plusDays(61)));
		System.out.println(monthsLate);
		List<Integer> authAccts = new ArrayList<>();
		authAccts.add(1);
		authAccts.add(2);
		User user = new User("jerrick", "pass", false,authAccts, true);
		BankAcct test = new BankAcct(AcctType.Checking,user);
		Transaction trans1 = new Transaction(user.getUsername(), 10, TranType.DEPOSIT);
		Transaction trans2 = new Transaction(user.getUsername(), 10, TranType.WITHDRAWAL);

		
		test.deposit(trans1);
		test.withdraw(trans2);
		test.deposit(trans1);
		test.withdraw(trans2);
		test.withdraw(trans2);
		AccountMessage msg = new AccountMessage(msgType.ACCOUNT_REQUEST,Status.SUCCESS,test);
		List<Transaction> transactions = msg.getTrans();
		test.deposit(trans1);
		test.deposit(trans1);
		test.deposit(trans1);
		for (Transaction transaction : transactions) {
			System.out.println(transaction);
		}
		System.out.println("Msg Balance: " + msg.getBalance());

		System.out.println("Acct Actual Balance: " + test.getBalance());
	}
}
