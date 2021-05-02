package simpleServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ServerThread extends Thread {

	private Socket socket;
	
	private DataInputStream in;
	private DataOutputStream out;
    
    private boolean running = false;
    
    private List<ReceiveListener> listeners;
    
    private Sender client;
	
	public ServerThread(Socket socket, List<ReceiveListener> listeners, Sender client) {
		super();
		this.socket = socket;
		this.listeners = listeners;
		this.client = client;
	}
	
	public void start() {
		if(running) return;
		try {
            in = new DataInputStream((socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            running = true;
        } catch (IOException e) {
        	e.printStackTrace();
            return;
        }
		/**
		 * starting Thread
		 */
		super.start();
	}
	
	public void run() {
        while (running) {
        	Message message = read(in);
        	processMEssage(message);
        }
	}
	
	private void processMEssage(Message message) {
		if(message.isError() && message.errorCode != Message.CLOSE_ERROR) {
    		System.out.println("received an error message. Error code: " + message.errorCode);
    		return;
    	}
    	if(message.type == Message.TYPE_MESSAGE) {
    		for (ReceiveListener listener : listeners) {
        		listener.receive(message.message, new Sender(this));
			}
    	} else {
    		switch(message.type) {
    		case Message.TYPE_DISCONNECT: close(); return;
    		}
    	}
	}
	
	public static Message read(DataInputStream datInputStream) {
		try {
			byte messageType = datInputStream.readByte();
			String msg = datInputStream.readUTF();
			return new Message(messageType, msg);
		} catch (IOException e) {
			return Message.ERROR_MESSAGE;
		}
	}
	
	public void send(String msg) {
		send(out, new Message(msg));
	}
	
//	private void send(Message msg) {
//		send(out, msg);
//	}
	
	public static void send(DataOutputStream outputStream, Message message) {
		if(outputStream == null) {
			return;
		}
		try {
			outputStream.writeByte(message.type);
			if(message.message == null) {
				outputStream.writeUTF("");
			} else {
				outputStream.writeUTF(message.message);
			}
			outputStream.flush();
		} catch (IOException e) {
			if(!e.getMessage().contentEquals("Socket closed")) {
				e.printStackTrace();
			}
		}
	}
	
	public void disconnect() {
		close();
	}
	
	protected void close() {
		System.out.println("Closing ServerThread");
//		send(Message.DISCONNECT_MESSAGE);
		client.disconnect();
		running = false;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}