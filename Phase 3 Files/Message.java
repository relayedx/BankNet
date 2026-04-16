import java.io.Serializable;

enum Status{
	SUCCESS,
	IN_PROGRESS,
	ERROR
}

enum msgType{
	LOGIN_REQUEST,
	LOGOUT_REQUEST,
	USER_REQUEST,
	USER_EDIT,
	AUTHUSER_REQUEST,
	AUTHUSER_EDIT,
	AUTHUSER_DELETE,
	PWRESET_REQUEST,
	CREDIT_REQUEST,
	ACCOUNT_CREATE,
	ACCOUNT_CLOSE,
	ACCOUNTS_REQUEST,
	ACCOUNTS_FREEZE,
	WITHDRAWAL_REQUEST,
	DEPOSIT_REQUEST,
	TRANSFER_REQUEST
}
// must implement Serializable in order to be sent
public class Message implements Serializable{
    private final msgType type;
    private final Status status;

    public Message(msgType type, Status status) {
    	this.type = type;
    	this.status = status;
    }

    public msgType getType() {
    	return type;
    }
    public Status getStatus() {
    	return status;
    }
}

