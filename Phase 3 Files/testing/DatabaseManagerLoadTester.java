package testing;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
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
	private DatabaseManager database;
	
	@BeforeEach
	public void setUp() throws Exception {
		// runs a test database builder first to make sure
		// the test files are in tact and formatted correctly
		TestDatabaseBuilder.main(new String[0]);
		
		// tries to account for different dev config set up
		String directory = System.getProperty("user.dir");
		String folderName = Path.of("").toAbsolutePath().getFileName().toString();
		
		System.out.println("Current Folder: " + folderName);
		
		// assumes user directory is run in "Phase 3 Files", but if not, main folder name here
		if (folderName.equals("BankNet")) {
			directory += "//Phase 3 Files";
		}
		
		database = new DatabaseManager(directory + "//db//TestAllUsers.txt",
									   directory + "//db//TestAllAccounts.txt",
									   directory + "//db//TestUsers//",
									   directory + "//db//TestAccounts//");
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
		List<Integer> authAcctIDs1 = new ArrayList<Integer>() { { add(3); add(4); add(20); add(1);} };
		// add 1 because account 1 is added to user 1 in TestDatabaseBuilder
		String firstName1 = "Sharlene";
		String lastName1 = "Yaps";
		String address1 = "979 Story Rd, San Jose, CA 95122";
		LocalDate dob1 = LocalDate.of(2007, 4, 9);
		String phone1 = "(123)456-7890";
		
		// user 2 fields
		String username2 = "jelly";
		String password2 = "ultra";
		boolean isTeller2 = true;
		List<Integer> authAcctIDs2 = new ArrayList<Integer>() { { add(2); add(3); } };
		// add 2 and 3 because account 2 and 3 is added to user 2 in TestDatabaseBuilder
		String firstName2 = "Tayce";
		String lastName2 = "T.";
		String address2 = "1071 B St, Hayward, CA 94541";
		LocalDate dob2 = LocalDate.of(2000, 8, 11);
		String phone2 = "(098)765-4321";
		
		// account 1 fields
		int acctID1 = 1;
		String owner1 = username1;
		AcctType type1 = AcctType.Checking;
		String authUsers1 = username2;
		// String authUsers1_2 = username1;			// account 1 was added to user 1
		// auth users don't update in addAccount() in DatabaseManager, just authAcctIDs
		float balance1 = 10.00f;
		float creditLine1 = 0.00f;
		float availCredit1 = 0.00f;
		LocalDate dueDate1 = null;
		// due date is null because that's how the logic in DatabaseManager and BankAccount work for Checking
		boolean frozen1 = false;
		boolean closed1 = false;
		
		List<Transaction> trans1 = new ArrayList<Transaction>() {
			{
				add(new Transaction(1, LocalDateTime.of(2026, 4, 14, 10, 15, 30), username1, 184.27f, TranType.WITHDRAWAL));
				add(new Transaction(2, LocalDateTime.of(2026, 3, 25, 13, 3, 27), username1, 116.18f, TranType.DEPOSIT));
				add(new Transaction(3, LocalDateTime.of(2026, 4, 24, 0, 0, 0), username2, 7.00f, TranType.SYSTEM));
			}
		};
		
		// account 2 fields
		int acctID2 = 2;
		String owner2 = username2;
		AcctType type2 = AcctType.Credit;
		// String authUsers2 = username2;			// account 2 was added to user 2
		float balance2 = 0.00f;
		float creditLine2 = 27.67f;
		float availCredit2 = 72.33f;
		LocalDate dueDate2 = LocalDate.of(2026, 4, 24);
		boolean frozen2 = true;
		boolean closed2 = true;
		
		List<Transaction> trans2 = new ArrayList<Transaction>() {
			{
				add(new Transaction(4, LocalDateTime.of(2026, 4, 8, 10, 15, 30), username1, 27.67f, TranType.WITHDRAWAL));
			}
		};
		
		// account 3 fields
		int acctID3 = 3;
		String owner3 = username2;
		AcctType type3 = AcctType.Savings;
		String authUsers3 = username1;
		// String authUsers3_2 = username2;			// account 3 was added to user 2
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
			() -> assertEquals(username1, database.getUser(username1).getUsername(), "Username 1 is wrong"),
			() -> assertEquals(password1, database.getUser(username1).getPassword(), "Password 1 is wrong"),
			() -> assertEquals(isTeller1, database.getUser(username1).getRole(), "Role 1 is wrong"),
			() -> assertEquals(authAcctIDs1, database.getUser(username1).getAuthAcctIDs(), "AuthAccts 1 is wrong"),
			// does user info 1 match with user info 1 in the database
			() -> assertEquals(firstName1, database.getUser(username1).getUserInfo().getFirstName(), "First Name 1 is wrong"),
			() -> assertEquals(lastName1, database.getUser(username1).getUserInfo().getLastName(), "Last Name 1 is wrong"),
			() -> assertEquals(address1, database.getUser(username1).getUserInfo().getAddress(), "Address 1 is wrong"),
			() -> assertEquals(dob1, database.getUser(username1).getUserInfo().getDOB(), "DOB 1 is wrong"),
			() -> assertEquals(phone1, database.getUser(username1).getUserInfo().getPhone(), "Phone 1 is wrong"),
			
			// does user 2 match with user 2 in the database
			() -> assertEquals(username2, database.getUser(username2).getUsername(), "Username 2 is wrong"),
			() -> assertEquals(password2, database.getUser(username2).getPassword(), "Password 2 is wrong"),
			() -> assertEquals(isTeller2, database.getUser(username2).getRole(), "Role 2 is wrong"),
			() -> assertEquals(authAcctIDs2, database.getUser(username2).getAuthAcctIDs(), "AuthAccts 2 is wrong"),
			// does user info 2 match with user info 2 in the database
			() -> assertEquals(firstName2, database.getUser(username2).getUserInfo().getFirstName(), "First Name 2 is wrong"),
			() -> assertEquals(lastName2, database.getUser(username2).getUserInfo().getLastName(), "Last Name 2 is wrong"),
			() -> assertEquals(address2, database.getUser(username2).getUserInfo().getAddress(), "Address 2 is wrong"),
			() -> assertEquals(dob2, database.getUser(username2).getUserInfo().getDOB(), "DOB 2 is wrong"),
			() -> assertEquals(phone2, database.getUser(username2).getUserInfo().getPhone(), "Phone 2 is wrong"),
			
			// does account 1 match with account 1 in the database
			() -> assertEquals(acctID1, database.getAccount(acctID1).getAcctID(), "AcctID 1 is wrong"),
			() -> assertEquals(owner1, database.getAccount(acctID1).getOwner().getUsername(), "Owner 1 is wrong"),
			() -> assertEquals(type1, database.getAccount(acctID1).getType(), "Type 1 is wrong"),
			() -> assertEquals(authUsers1, database.getAccount(acctID1).getAuths().get(0).getUsername(), "AuthUser 1 is wrong"),
			() -> assertEquals(balance1, database.getAccount(acctID1).getBalance(), "Balance 1 is wrong"),
			() -> assertEquals(creditLine1, database.getAccount(acctID1).getCredit(), "Credit Line 1 is wrong"),
			() -> assertEquals(availCredit1, database.getAccount(acctID1).getAvailCredit(), "Available Credit 1 is wrong"),
			() -> assertEquals(dueDate1, database.getAccount(acctID1).getDueDate(), "Due Date 1 is wrong"),
			() -> assertEquals(frozen1, database.getAccount(acctID1).getFrozen(), "Frozen 1 is wrong"),
			() -> assertEquals(closed1, database.getAccount(acctID1).getClosed(), "Closed 1 is wrong"),
			// does acct 1 transaction 1 match with acct 1 transaction 1 in the database
			() -> assertEquals(trans1.get(0).getUID(), database.getAccount(acctID1).getTrans().get(0).getUID(),
					"Account 1, Transaction 1, ID is wrong"),
			() -> assertEquals(trans1.get(0).getDate(), database.getAccount(acctID1).getTrans().get(0).getDate(),
					"Account 1, Transaction 1, Date is wrong"),
			() -> assertEquals(trans1.get(0).getUser(), database.getAccount(acctID1).getTrans().get(0).getUser(),
					"Account 1, Transaction 1, User is wrong"),
			() -> assertEquals(trans1.get(0).getAmount(), database.getAccount(acctID1).getTrans().get(0).getAmount(),
					"Account 1, Transaction 1, Amount is wrong"),
			() -> assertEquals(trans1.get(0).getType(), database.getAccount(acctID1).getTrans().get(0).getType(),
					"Account 1, Transaction 1, Type is wrong"),
			// does acct 1 transaction 2 match with acct 1 transaction 2 in the database
			() -> assertEquals(trans1.get(1).getUID(), database.getAccount(acctID1).getTrans().get(1).getUID(),
					"Account 1, Transaction 2, ID is wrong"),
			() -> assertEquals(trans1.get(1).getDate(), database.getAccount(acctID1).getTrans().get(1).getDate(),
					"Account 1, Transaction 2, Date is wrong"),
			() -> assertEquals(trans1.get(1).getUser(), database.getAccount(acctID1).getTrans().get(1).getUser(),
					"Account 1, Transaction 2, User is wrong"),
			() -> assertEquals(trans1.get(1).getAmount(), database.getAccount(acctID1).getTrans().get(1).getAmount(),
					"Account 1, Transaction 2, Amount is wrong"),
			() -> assertEquals(trans1.get(1).getType(), database.getAccount(acctID1).getTrans().get(1).getType(),
					"Account 1, Transaction 2, Type is wrong"),
			// does acct 1 transaction 3 match with acct 1 transaction 3 in the database
			() -> assertEquals(trans1.get(2).getUID(), database.getAccount(acctID1).getTrans().get(2).getUID(),
					"Account 1, Transaction 3, ID is wrong"),
			() -> assertEquals(trans1.get(2).getDate(), database.getAccount(acctID1).getTrans().get(2).getDate(),
					"Account 1, Transaction 3, Date is wrong"),
			() -> assertEquals(trans1.get(2).getUser(), database.getAccount(acctID1).getTrans().get(2).getUser(),
					"Account 1, Transaction 3, User is wrong"),
			() -> assertEquals(trans1.get(2).getAmount(), database.getAccount(acctID1).getTrans().get(2).getAmount(),
					"Account 1, Transaction 3, Amount is wrong"),
			() -> assertEquals(trans1.get(2).getType(), database.getAccount(acctID1).getTrans().get(2).getType(),
					"Account 1, Transaction 3, Type is wrong"),
			
			// does account 2 match with account 2 in the database
			() -> assertEquals(acctID2, database.getAccount(acctID2).getAcctID(), "AcctID 2 is wrong"),
			() -> assertEquals(owner2, database.getAccount(acctID2).getOwner().getUsername(), "Owner 2 is wrong"),
			() -> assertEquals(type2, database.getAccount(acctID2).getType(), "Type 2 is wrong"),
			() -> assertTrue(database.getAccount(acctID2).getAuths().isEmpty(), "AuthUser 2 is wrong"),
			() -> assertEquals(balance2, database.getAccount(acctID2).getBalance(), "Balance 2 is wrong"),
			() -> assertEquals(creditLine2, database.getAccount(acctID2).getCredit(), "Credit Line 2 is wrong"),
			() -> assertEquals(availCredit2, database.getAccount(acctID2).getAvailCredit(), "Available Credit 2 is wrong"),
			() -> assertEquals(dueDate2, database.getAccount(acctID2).getDueDate(), "Due Date 2 is wrong"),
			() -> assertEquals(frozen2, database.getAccount(acctID2).getFrozen(), "Frozen 2 is wrong"),
			() -> assertEquals(closed2, database.getAccount(acctID2).getClosed(), "Closed 2 is wrong"),
			// does acct 2 transaction 1 match with acct 2 transaction 1 in the database
			() -> assertEquals(trans1.get(0).getUID(), database.getAccount(acctID1).getTrans().get(0).getUID(),
					"Account 2, Transaction 1, ID is wrong"),
			() -> assertEquals(trans2.get(0).getDate(), database.getAccount(acctID2).getTrans().get(0).getDate(),
					"Account 2, Transaction 1, Date is wrong"),
			() -> assertEquals(trans2.get(0).getUser(), database.getAccount(acctID2).getTrans().get(0).getUser(),
					"Account 2, Transaction 1, User is wrong"),
			() -> assertEquals(trans2.get(0).getAmount(), database.getAccount(acctID2).getTrans().get(0).getAmount(),
					"Account 2, Transaction 1, Amount is wrong"),
			() -> assertEquals(trans2.get(0).getType(), database.getAccount(acctID2).getTrans().get(0).getType(),
					"Account 2, Transaction 1, Type is wrong"),
			
			// does account 3 match with account 3 in the database
			() -> assertEquals(acctID3, database.getAccount(acctID3).getAcctID(), "AcctID 3 is wrong"),
			() -> assertEquals(owner3, database.getAccount(acctID3).getOwner().getUsername(), "Owner 3 is wrong"),
			() -> assertEquals(type3, database.getAccount(acctID3).getType(), "Type 3 is wrong"),
			() -> assertEquals(authUsers3, database.getAccount(acctID3).getAuths().get(0).getUsername(), "AcctID 3 is wrong"),
			() -> assertEquals(balance3, database.getAccount(acctID3).getBalance(), "Balance 3 is wrong"),
			() -> assertEquals(creditLine3, database.getAccount(acctID3).getCredit(), "Credit Line 3 is wrong"),
			() -> assertEquals(availCredit3, database.getAccount(acctID3).getAvailCredit(), "Available Credit 3 is wrong"),
			() -> assertEquals(dueDate3, database.getAccount(acctID3).getDueDate(), "Due Date 3 is wrong"),
			() -> assertEquals(frozen3, database.getAccount(acctID3).getFrozen(), "Frozen 3 is wrong"),
			() -> assertEquals(closed3, database.getAccount(acctID3).getClosed(), "Closed 3 is wrong")
		);
	}
}
