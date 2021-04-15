package simpleServer;

@FunctionalInterface
public interface ReceiveListener {

	public void receive(String message, Sender sender);
}
