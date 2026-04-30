package testing;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
	CheckingAccountTests.class,
	CreditAccountTests.class,
	UserTests.class,
	UserInfoTests.class,
	DatabaseManagerLoadTester.class,
	DatabaseManagerSaveTester.class
})
public class TestSuite {}
