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
		BankAcct test = new BankAcct(AcctType.Savings,user);
		Transaction trans1 = new Transaction(user.getUsername(), 10, TranType.DEPOSIT);
		test.deposit(trans1);
		test.deposit(trans1);
		test.deposit(trans1);
		test.deposit(trans1);
		List<Transaction> transactions = test.getTrans();
		for (Transaction transaction : transactions) {
			String[] m = transaction.toString().split("%");
			System.out.println(transaction);
			System.out.println(m[0]);
		}
	}
}
