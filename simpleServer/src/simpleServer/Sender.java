package simpleServer;

public class Sender {

	String name = "name";
	boolean isServer;
	
	private ServerThread serverThread;
	
	public Sender() {
		super();
	}
	
	public Sender(ServerThread serverThread) {
		super();
		this.serverThread = serverThread;
	}
	
	public void disconnect() {
		
	}
	
	public void sendMessage(String message) {
		if(serverThread != null) {
			serverThread.send(message);
		}
	}
}
