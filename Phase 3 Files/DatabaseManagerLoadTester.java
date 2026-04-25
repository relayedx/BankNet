import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// make sure the test files are in tact and formatted correctly
// if not, run the test database builder first
public class DatabaseManagerLoadTester {
	DatabaseManager database;
	
	@BeforeEach
	public void setUp() throws Exception {
		TestDatabaseBuilder.main(new String[0]);
		database = new DatabaseManager("db\\TestAllUsers.txt", "db\\TestAllAccounts.txt",
									   "db\\TestUsers\\", "db\\TestAccounts\\");
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testLoadData() {
		database.loadData();
		
		fail("Not yet implemented");
	}
}
