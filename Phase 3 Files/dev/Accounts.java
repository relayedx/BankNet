package dev;
import java.util.ArrayList;
import java.util.List;

public class Accounts {
	private List<BankAcct> bankAccts;
	
	Accounts(List<BankAcct> bankAccts) {
		this.bankAccts = bankAccts;
	}
	
	// get multiple accts
	// probably just use this for request for getting single accts as well
	// theres no way we can guarentee an account will be found
	public List<BankAcct> getAccts(List<Integer> acctIDs) {
		List<BankAcct> acctsRequested = new ArrayList<>();
		for (BankAcct currBankAcct : bankAccts) {
			int currentID = currBankAcct.getAcctID();
			for (int acctID : acctIDs) {
				if (currentID == acctID) {
					acctsRequested.add(currBankAcct);
				}
			}
		}
		return acctsRequested;
	}
	
	/*
	// get singular acct
	public BankAcct getAcct(int acctID) {
		BankAcct acctRequested;
		for (BankAcct currBankAcct : bankAccts) {
			int currentID = currBankAcct.getAcctID();
			if (currentID == acctID) {
				return currBankAcct;
			}
		}
		
	}
	*/
}
