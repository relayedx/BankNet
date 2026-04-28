package dev;

public enum AcctType{
	Credit,
	Checking,
	Savings;
	
	// added parsing method to translate from our files
	public static AcctType parseAcctType(String type) {
		switch(type) {
			case "Credit":
				return AcctType.Credit;
			case "Checking":
				return AcctType.Checking;
			case "Savings":
				return AcctType.Savings;
			default:
				return null;
		}
	}
}
