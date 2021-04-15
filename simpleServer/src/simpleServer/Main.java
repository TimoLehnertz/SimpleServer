package simpleServer;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		SimpleServer server = new SimpleServer();
		server.addReceiveListener((String message, Sender client) -> {
			System.out.println("Server received: " + message + " from " + client.name);
		});
		server.addConnectionListener((ServerThread connection) -> {
			connection.send("Hello from the server");
		});
		
		
		SimpleClient client = new SimpleClient("www.roller-results.com");
		client.addReceiveListener((String message, Sender c) -> {
			System.out.println("Client received: " + message + " from " + c.name);
		});
		
		server.start();
		client.start();
		
		
		Scanner s = new Scanner(System.in);
		
		System.out.println("enter something for client to send something");
		s.next();
		client.send("Hallo welt from the client");
		
		System.out.println("enter something for server to send something");
		s.next();
		server.getClients().get(0).send("Hallo welt from th client");
		
		System.out.println("enter something to close the server");
		s.next();
		s.close();
		server.close();
		client.close();
	}
}