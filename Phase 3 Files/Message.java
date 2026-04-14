import java.io.Serializable;

enum Status{
	SUCCESS,
	IN_PROGRESS,
	ERROR
}

enum msgType{
	DEPOSIT_REQUEST,
	WITHDRAWAL_REQUEST,
	LOGIN_REQUEST,
	ACCOUNTS_REQUEST
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

