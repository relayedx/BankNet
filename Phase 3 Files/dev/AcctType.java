package dev;

public enum AcctType{
	Credit,
	Checking,
	Savings;
	
	// added parsing method to translate from our files
	public static AcctType parseAcctType(String type) {
		switch(type.toLowerCase()) {
			case "credit":
				return AcctType.Credit;
			case "checking":
				return AcctType.Checking;
			case "savings":
				return AcctType.Savings;
			default:
				return null;
		}
	}
}
