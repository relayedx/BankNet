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
		User user = new User("jerrick", "pass", false,authAccts, true);
		User user1 = new User("crying", "pass", false,authAccts, true);
		User user2 = new User("man", "pass", false,authAccts, true);
		BankAcct test = new BankAcct(AcctType.Credit,user);
		test.addAuthUser(user1);
		test.addAuthUser(user2);
		Transaction trans1 = new Transaction(user.getUsername(), 10, TranType.DEPOSIT);
		Transaction trans2 = new Transaction(user.getUsername(), 10, TranType.WITHDRAWAL);

	
		test.deposit(trans1);
		test.withdraw(trans2);
		test.withdraw(trans2);
		
		/*
		test.withdraw(trans2);
		test.deposit(trans1);
		test.withdraw(trans2);
		test.withdraw(trans2);
		*/
		AccountMessage msg = new AccountMessage(msgType.ACCOUNT_REQUEST,Status.SUCCESS,test);
		List<Transaction> transactions = msg.getTrans();
		/*
		test.deposit(trans1);
		test.deposit(trans1);
		test.deposit(trans1);
		*/
		for (Transaction transaction : transactions) {
			System.out.println(transaction);
		}
		System.out.println("Msg Balance: " + msg.getBalance());

		System.out.println("Acct Actual Balance: " + test.getBalance());
		System.out.println(test.getAvailCredit() + " Expected Credit: " + test.getCredit());
		System.out.println(test.calculateMonths(LocalDate.now().plusDays(92)));
		
		for (User use : test.getAuths()) {
			System.out.println(use.getUsername());
		}
		test.removeAuth(user2);
		for (User use : test.getAuths()) {
			System.out.println(use.getUsername());
		}
	}
}
