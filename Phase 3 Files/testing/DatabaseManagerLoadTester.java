package testing;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.DatabaseManager;
import dev.AcctType;
import dev.TranType;
import dev.Transaction;

public class DatabaseManagerLoadTester {
	DatabaseManager database;
	
	@BeforeEach
	public void setUp() throws Exception {
		// runs a test database builder first to make sure
		// the test files are in tact and formatted correctly
		TestDatabaseBuilder.main(new String[0]);
		database = new DatabaseManager(System.getProperty("user.dir") + "\\db\\TestAllUsers.txt",
									   System.getProperty("user.dir") + "\\db\\TestAllAccounts.txt",
									   System.getProperty("user.dir") + "\\db\\TestUsers\\",
									   System.getProperty("user.dir") + "\\db\\TestAccounts\\");
	}
	
	@Test
	public void testLoadData() {
		database.loadData();
		
		// if you want to change the fields, please change them in the TestDatabaseBuilder too
		// that way, the test won't fail when you run it
		
		// user 1 fields
		String username1 = "shar";
		String password1 = "yap";
		boolean isTeller1 = false;
		List<Integer> authAcctIDs1 = new ArrayList<Integer>() { { add(1); add(3); add(4); add(20); } };
		String firstName1 = "Sharlene";
		String lastName1 = "Yaps";
		String address1 = "979 Story Rd, San Jose, CA 95122";
		LocalDate dob1 = LocalDate.of(2007, 4, 9);
		String phone1 = "(123)456-7890";
		
		// user 2 fields
		String username2 = "jelly";
		String password2 = "ultra";
		boolean isTeller2 = true;
		List<Integer> authAcctIDs2 = new ArrayList<Integer>();
		String firstName2 = "Tayce";
		String lastName2 = "T.";
		String address2 = "1071 B St, Hayward, CA 94541";
		LocalDate dob2 = LocalDate.of(2000, 8, 11);
		String phone2 = "(098)765-4321";
		
		// account 1 fields
		int acctID1 = 0;
		String owner1 = username1;
		AcctType type1 = AcctType.Checking;
		String authUsers1 = username2;
		float balance1 = 10.00f;
		float creditLine1 = 0.00f;
		float availCredit1 = 0.00f;
		LocalDate dueDate1 = LocalDate.of(2026, 4, 22);
		boolean frozen1 = false;
		boolean closed1 = false;
		
		List<Transaction> trans1 = new ArrayList<Transaction>() {
			{
				add(new Transaction(LocalDateTime.of(2026, 4, 14, 10, 15, 30), username1, 184.27f, TranType.WITHDRAWAL));
				add(new Transaction(LocalDateTime.of(2026, 3, 25, 13, 3, 27), username1, 116.18f, TranType.DEPOSIT));
				add(new Transaction(LocalDateTime.of(2026, 4, 24, 0, 0, 0), username2, 7.00f, TranType.SYSTEM));
			}
		};
		
		// account 2 fields
		int acctID2 = 1;
		String owner2 = username2;
		AcctType type2 = AcctType.Credit;
		// String authUsers2 = "";
		float balance2 = 0.00f;
		float creditLine2 = 27.67f;
		float availCredit2 = 72.33f;
		LocalDate dueDate2 = LocalDate.of(2026, 4, 24);
		boolean frozen2 = true;
		boolean closed2 = true;
		
		List<Transaction> trans2 = new ArrayList<Transaction>() {
			{
				add(new Transaction(LocalDateTime.of(2026, 4, 8, 10, 15, 30), username1, 27.67f, TranType.WITHDRAWAL));
			}
		};
		
		// account 3 fields
		int acctID3 = 2;
		String owner3 = username2;
		AcctType type3 = AcctType.Savings;
		String authUsers3 = username1;
		float balance3 = 80.97f;
		float creditLine3 = 0f;
		float availCredit3 = 0f;
		LocalDate dueDate3 = LocalDate.of(1998, 6, 2);
		boolean frozen3 = true;
		boolean closed3 = false;
		
		List<Transaction> trans3 = new ArrayList<Transaction>();
		
		assertAll(
			"Loading Test Data",
			// does user 1 match with user 1 in the database
			() -> assertEquals(username1, database.getUser(username1).getUsername()),
			() -> assertEquals(password1, database.getUser(username1).getPassword()),
			() -> assertEquals(isTeller1, database.getUser(username1).getRole()),
			() -> assertEquals(authAcctIDs1, database.getUser(username1).getAuthAcctIDs()),
			// does user info 1 match with user info 1 in the database
			() -> assertEquals(firstName1, database.getUser(username1).getUserInfo().getFirstName()),
			() -> assertEquals(lastName1, database.getUser(username1).getUserInfo().getLastName()),
			() -> assertEquals(address1, database.getUser(username1).getUserInfo().getAddress()),
			() -> assertEquals(dob1, database.getUser(username1).getUserInfo().getDOB()),
			() -> assertEquals(phone1, database.getUser(username1).getUserInfo().getPhone()),
			
			// does user 2 match with user 2 in the database
			() -> assertEquals(username2, database.getUser(username2).getUsername()),
			() -> assertEquals(password2, database.getUser(username2).getPassword()),
			() -> assertEquals(isTeller2, database.getUser(username2).getRole()),
			() -> assertEquals(authAcctIDs2, database.getUser(username2).getAuthAcctIDs()),
			// does user info 2 match with user info 2 in the database
			() -> assertEquals(firstName2, database.getUser(username2).getUserInfo().getFirstName()),
			() -> assertEquals(lastName2, database.getUser(username2).getUserInfo().getLastName()),
			() -> assertEquals(address2, database.getUser(username2).getUserInfo().getAddress()),
			() -> assertEquals(dob2, database.getUser(username2).getUserInfo().getDOB()),
			() -> assertEquals(phone2, database.getUser(username2).getUserInfo().getPhone()),
			
			// does account 1 match with account 1 in the database
			() -> assertEquals(acctID1, database.getAccount(acctID1).getAcctID()),
			() -> assertEquals(owner1, database.getAccount(acctID1).getOwner().getUsername()),
			() -> assertEquals(type1, database.getAccount(acctID1).getType()),
			() -> assertEquals(authUsers1, database.getAccount(acctID1).getAuths().get(0).getUsername()),
			() -> assertEquals(balance1, database.getAccount(acctID1).getBalance()),
			() -> assertEquals(creditLine1, database.getAccount(acctID1).getCredit()),
			() -> assertEquals(availCredit1, database.getAccount(acctID1).getAvailCredit()),
			() -> assertEquals(dueDate1, database.getAccount(acctID1).getDueDate()),
			() -> assertEquals(frozen1, database.getAccount(acctID1).getFrozen()),
			() -> assertEquals(closed1, database.getAccount(acctID1).getClosed()),
			// does acct 1 transaction 1 match with acct 1 transaction 1 in the database
			() -> assertEquals(trans1.get(0).getDate(), database.getAccount(acctID1).getTrans().get(0).getDate()),
			() -> assertEquals(trans1.get(0).getUser(), database.getAccount(acctID1).getTrans().get(0).getUser()),
			() -> assertEquals(trans1.get(0).getAmount(), database.getAccount(acctID1).getTrans().get(0).getAmount()),
			() -> assertEquals(trans1.get(0).getType(), database.getAccount(acctID1).getTrans().get(0).getType()),
			// does acct 1 transaction 2 match with acct 1 transaction 2 in the database
			() -> assertEquals(trans1.get(1).getDate(), database.getAccount(acctID1).getTrans().get(1).getDate()),
			() -> assertEquals(trans1.get(1).getUser(), database.getAccount(acctID1).getTrans().get(1).getUser()),
			() -> assertEquals(trans1.get(1).getAmount(), database.getAccount(acctID1).getTrans().get(1).getAmount()),
			() -> assertEquals(trans1.get(1).getType(), database.getAccount(acctID1).getTrans().get(1).getType()),
			// does acct 1 transaction 3 match with acct 1 transaction 3 in the database
			() -> assertEquals(trans1.get(2).getDate(), database.getAccount(acctID1).getTrans().get(2).getDate()),
			() -> assertEquals(trans1.get(2).getUser(), database.getAccount(acctID1).getTrans().get(2).getUser()),
			() -> assertEquals(trans1.get(2).getAmount(), database.getAccount(acctID1).getTrans().get(2).getAmount()),
			() -> assertEquals(trans1.get(2).getType(), database.getAccount(acctID1).getTrans().get(2).getType()),
			
			// does account 2 match with account 2 in the database
			() -> assertEquals(acctID2, database.getAccount(acctID2).getAcctID()),
			() -> assertEquals(owner2, database.getAccount(acctID2).getOwner().getUsername()),
			() -> assertEquals(type2, database.getAccount(acctID2).getType()),
			() -> assertTrue(database.getAccount(acctID2).getAuths().isEmpty()),
			() -> assertEquals(balance2, database.getAccount(acctID2).getBalance()),
			() -> assertEquals(creditLine2, database.getAccount(acctID2).getCredit()),
			() -> assertEquals(availCredit2, database.getAccount(acctID2).getAvailCredit()),
			() -> assertEquals(dueDate2, database.getAccount(acctID2).getDueDate()),
			() -> assertEquals(frozen2, database.getAccount(acctID2).getFrozen()),
			() -> assertEquals(closed2, database.getAccount(acctID2).getClosed()),
			// does acct 2 transaction 1 match with acct 2 transaction 1 in the database
			() -> assertEquals(trans2.get(0).getDate(), database.getAccount(acctID2).getTrans().get(0).getDate()),
			() -> assertEquals(trans2.get(0).getUser(), database.getAccount(acctID2).getTrans().get(0).getUser()),
			() -> assertEquals(trans2.get(0).getAmount(), database.getAccount(acctID2).getTrans().get(0).getAmount()),
			() -> assertEquals(trans2.get(0).getType(), database.getAccount(acctID2).getTrans().get(0).getType()),
			
			// does account 3 match with account 3 in the database
			() -> assertEquals(acctID3, database.getAccount(acctID3).getAcctID()),
			() -> assertEquals(owner3, database.getAccount(acctID3).getOwner().getUsername()),
			() -> assertEquals(type3, database.getAccount(acctID3).getType()),
			() -> assertEquals(authUsers3, database.getAccount(acctID3).getAuths().get(0).getUsername()),
			() -> assertEquals(balance3, database.getAccount(acctID3).getBalance()),
			() -> assertEquals(creditLine3, database.getAccount(acctID3).getCredit()),
			() -> assertEquals(availCredit3, database.getAccount(acctID3).getAvailCredit()),
			() -> assertEquals(dueDate3, database.getAccount(acctID3).getDueDate()),
			() -> assertEquals(frozen3, database.getAccount(acctID3).getFrozen()),
			() -> assertEquals(closed3, database.getAccount(acctID3).getClosed())
		);
	}
}
