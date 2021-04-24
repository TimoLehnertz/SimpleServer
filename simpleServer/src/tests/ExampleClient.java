package tests;

import java.util.Scanner;

import simpleServer.Sender;
import simpleServer.SimpleClient;

public class ExampleClient {
	public static void main(String[] args) {
		boolean running = true;
		Scanner s = new Scanner(System.in);
		String ip;
		while(running) {
			System.out.println("enter an ip address to connect to");
			ip = s.next();
			SimpleClient client = new SimpleClient(ip);
			client.start();
			if(client.isRunning()) {
				System.out.println("connected succsessfully.");
				System.out.println("	enter \"stop\" to stop the client");
				System.out.println("	enter \"stop server\" to stop the server");
				client.addReceiveListener((String message, Sender sender) -> {
					System.out.println("<< " + message);
				});
				while(true) {
					String line = s.next();
					if(line.toLowerCase().contentEquals("stop")) {
						running = false;
						System.out.println("stopping client");
						client.close();
						break;
					} else {
						client.send(line);
						System.out.println(">> " + line);
					}
				}
			}
		}
		s.close();
	}
}
