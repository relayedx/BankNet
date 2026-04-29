package dev;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class AccountTester {
	public static void main(String[] args) {
		LocalDate dueDate = LocalDate.now();
		List<Integer> authAccts = new ArrayList<>();
		authAccts.add(1);
		authAccts.add(2);
		UserInfo info = new UserInfo("first", "last", "add", LocalDate.now(), "phone");
		User user = new User("jerrick", "pass", info, false, authAccts, true);
		User user1 = new User("crying", "pass", info, false, authAccts, true);
		User user2 = new User("man", "pass", info, false, authAccts, true);
		BankAcct test = new BankAcct(AcctType.Credit,user);
		test.addAuthUser(user1);
		test.addAuthUser(user2);
		Transaction trans1 = new Transaction(user.getUsername(), 100.23f, TranType.DEPOSIT);
		Transaction trans2 = new Transaction(user.getUsername(), 10, TranType.WITHDRAWAL);

	
		test.deposit(trans1);
		
		/*
		test.withdraw(trans2);
		test.deposit(trans1);
		test.withdraw(trans2);
		test.withdraw(trans2);
		*/
		LocalDate tester = LocalDate.now().plusDays(1001);
		test.calculateMonths(tester);
		AccountMessage msg = new AccountMessage(msgType.ACCOUNT_REQUEST,Status.SUCCESS,test);
		List<Transaction> transactions = msg.getTrans();
		

		for (Transaction trans : transactions) {
			System.out.println(trans);
		}
		/*
		test.deposit(trans1);
		test.deposit(trans1);
		test.deposit(trans1);
		*/
	}
}
