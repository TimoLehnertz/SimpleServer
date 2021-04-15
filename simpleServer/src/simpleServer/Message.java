package simpleServer;

public class Message {

	static final int CLOSE_ERROR = 1;
	
	static final int TYPE_DISCONNECT = 1;
	static final int TYPE_MESSAGE = 2;
	
	static final Message ERROR_MESSAGE = new Message(-1, null, CLOSE_ERROR);
	static final Message DISCONNECT_MESSAGE = new Message(TYPE_DISCONNECT, null);
	
	public int type;
	public String message;
	public int errorCode;
	
	public Message(String message) {
		this(TYPE_MESSAGE, message);
	}
	
	public Message(int type, String message) {
		this(type, message, 0);
	}
	
	public Message(int type, String message, int errorCode) {
		super();
		this.type = type;
		this.message = message;
		this.errorCode = errorCode;
	}
	
	public boolean isError() {
		return errorCode != 0 || type < 0;
	}
	
	@Override
	public String toString() {
		if(isError()) {
			return "Message{Error Message Code: " + errorCode + "}";
		}
		return "Message {type: " + type + ", message: " + message + "}";
	}
}