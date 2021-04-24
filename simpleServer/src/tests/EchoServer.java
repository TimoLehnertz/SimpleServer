package tests;

import simpleServer.Sender;
import simpleServer.SimpleServer;

public class EchoServer {
	public static void main(String[] args) {
		SimpleServer server = new SimpleServer();
		server.start();
		server.addReceiveListener((String message, Sender sender) -> {
			System.out.println("received: " + message);
			if(message.contentEquals("stop server")) {
				sender.sendMessage("closing server");
				server.close();
			} else {
				sender.sendMessage("Echo: " + message);
			}
		});
	}
}