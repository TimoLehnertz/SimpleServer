package simpleServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleClient extends Thread {

	private Socket socket;
	private String masterIp;
	private int masterPort;
	
	private boolean running = false;
	
	private DataOutputStream out;
	DataInputStream in;
	
	List<ReceiveListener> listeners = new ArrayList<>();
	
	public SimpleClient(String masterIp) {
		this(masterIp, SimpleServer.DEFAULT_PORT);
	}
	
	public SimpleClient(String masterIp, int masterPort) {
		super();
		this.masterIp = masterIp;
		this.masterPort = masterPort;
	}
	
	public void start() {
		System.out.println("Starting client");
		try {
			socket = new Socket(masterIp, masterPort);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			running = true;
			
			// Starting the Thread
			super.start();
		} catch (IOException e) {
			System.err.print("Could not Connect: " + e.getMessage());
		}
	}
	
	
	public void close() {
		if(!running) return;
		System.out.println("Stopping client");
		send(Message.DISCONNECT_MESSAGE);
		running = false;
		if(socket == null) {
			return;
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(running) {
			Message message = ServerThread.read(in);
			processMessage(message);
		}
	}
	
	private void processMessage(Message message) {
		if(message.isError() && message.errorCode != Message.CLOSE_ERROR) {
			System.err.println(message);
		}
		if(message.type == Message.TYPE_MESSAGE) {
			for (ReceiveListener listener : listeners) {
				listener.receive(message.message, new Sender());
			}
		}
		switch(message.type) {
		case Message.TYPE_DISCONNECT: close(); return;
		}
	}
	
	public void send(String message) {
		if(!running) return;
		ServerThread.send(out, new Message(message));
	}
	
	private void send(Message message) {
		if(!running) return;
		ServerThread.send(out, message);
	}
	
	public boolean addReceiveListener(ReceiveListener l) {
		return listeners.add(l);
	}
	
	public boolean removeReceiveListener(ReceiveListener l) {
		return listeners.remove(l);
	}

	public boolean isRunning() {
		return running;
	}
}