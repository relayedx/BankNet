package testing;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dev.DatabaseManager;
import dev.AcctType;
import dev.BankAcct;
import dev.Transaction;
import dev.TranType;
import dev.User;
import dev.UserInfo;

public class TestDatabaseBuilder {
	public static void main(String[] args) {
		
		/// Deletes all previous data in the database
		DatabaseManager database = new DatabaseManager
				(System.getProperty("user.dir") + "\\db\\TestAllUsers.txt",
				 System.getProperty("user.dir") + "\\db\\TestAllAccounts.txt",
				 System.getProperty("user.dir") + "\\db\\TestUsers\\",
				 System.getProperty("user.dir") + "\\db\\TestAccounts\\");
		
		File file1 = new File(System.getProperty("user.dir") + "\\db\\TestAllUsers.txt"); 
		File file2 = new File(System.getProperty("user.dir") + "\\db\\TestAllAccounts.txt"); 
		File folder1 = new File(System.getProperty("user.dir") + "\\db\\TestUsers\\"); 
		File folder2 = new File(System.getProperty("user.dir") + "\\db\\TestAccounts\\"); 
		
		file1.delete();
		file2.delete();
		
		File[] folderFiles1 = folder1.listFiles();
		
		if (folderFiles1 != null) {
			for (File file : folderFiles1) {
				if (file.isFile()) {
					file.delete();
				}
			}
		}
		
		File[] folderFiles2 = folder2.listFiles();
		
		if (folderFiles2 != null) {
			for (File file : folderFiles2) {
				if (file.isFile()) {
					file.delete();
				}
			}
		}
		
		/// User 1 and 2 info
		String username1 = "shar";
		String password1 = "yap";
		String username2 = "jelly";
		String password2 = "ultra";
		
		UserInfo info1 = new UserInfo("Sharlene", "Yaps", "979 Story Rd, San Jose, CA 95122", LocalDate.of(2007, 4, 9), "(123)456-7890");
		User user1 = new User(username1, password1, info1, false, new ArrayList<Integer>() { { add(3); add(4); add(20); } }, true);
		
		UserInfo info2 = new UserInfo("Tayce", "T.", "1071 B St, Hayward, CA 94541", LocalDate.of(2000, 8, 11), "(098)765-4321");
		User user2 = new User(username2, password2, info2, true, new ArrayList<Integer>(), true);
		
		/// Bank Account 1 info
		List<Transaction> trans1 = new ArrayList<Transaction>() {
			{
				add(new Transaction(1, LocalDateTime.of(2026, 4, 14, 10, 15, 30), username1, 184.27f, TranType.WITHDRAWAL));
				add(new Transaction(2, LocalDateTime.of(2026, 3, 25, 13, 3, 27), username1, 116.18f, TranType.DEPOSIT));
				add(new Transaction(3, LocalDateTime.of(2026, 4, 24, 0, 0, 0), username2, 7.00f, TranType.SYSTEM));
			}
		};
		
		BankAcct account1 = new BankAcct(1, user1, AcctType.Checking, new ArrayList<User>() { { add(user2); } },
				10.00f, 0.00f, 0.00f, null, false, false, trans1);
		// due date is null because that's how the logic in DatabaseManager and BankAccount work for Checking
		
		/// Bank Account 2 info
		List<Transaction> trans2 = new ArrayList<Transaction>() {
			{
				add(new Transaction(4, LocalDateTime.of(2026, 4, 8, 10, 15, 30), username1, 27.67f, TranType.WITHDRAWAL));
			}
		};
		
		BankAcct account2 = new BankAcct(2, user2, AcctType.Credit, new ArrayList<User>(),
				0.00f, 27.67f, 72.33f, LocalDate.of(2026, 4, 24), true, true, trans2);
		
		/// Bank Account 3 info
		List<Transaction> trans3 = new ArrayList<Transaction>();
		
		BankAcct account3 = new BankAcct(3, user2, AcctType.Savings, new ArrayList<User>() { { add(user1); } },
				80.97f, 0f, 0f, LocalDate.of(1998, 6, 2), true, false, trans3);
		
		/// Adds all objects to database
		database.addUser(user1);
		database.addUser(user2);
		database.addAccount(account1);
		database.addAccount(account2);
		database.addAccount(account3);
	}
}
