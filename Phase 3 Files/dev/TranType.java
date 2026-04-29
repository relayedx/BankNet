package dev;

public enum TranType{
	WITHDRAWAL,
	DEPOSIT,
	TRANSFER,
	SYSTEM;
	
	// added parsing method to translate from our files
	public static TranType parseTranType(String type) {
		switch(type) {
			case "WITHDRAWAL":
				return TranType.WITHDRAWAL;
			case "DEPOSIT":
				return TranType.DEPOSIT;
			case "SYSTEM":
				return TranType.SYSTEM;
			case "TRANSFER":
				return TranType.TRANSFER;
			default:
				return null;
		}
	}
}
