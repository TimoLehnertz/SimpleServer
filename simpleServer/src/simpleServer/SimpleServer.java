package simpleServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * SimpleServer class intendet for all interaction with the server
 * after instantiating a new Server the server
 * has to be started with SimpleSerevr::start()
 * and stopped with SimpleSerevr::close()
 * 
 * @author Timo Lehnertz
 *
 */
public class SimpleServer extends Thread{

	public final static int DEFAULT_PORT = 56665;
	private int port;
	private boolean running = false;
	private ServerSocket serverSocket;
	private List<ReceiveListener> receiveListeners = new ArrayList<>();
	private List<ConnectionListener> connectionListeners = new ArrayList<>();
	
    
    private List<ServerThread> threads = new ArrayList<>();	
    
	public SimpleServer() {
		this(DEFAULT_PORT);
	}
	
	public SimpleServer(int port) {
		super();
		this.port = port;
	}
	
	public void start() {
		System.out.println("starting server on port " + port);
		
		try {
			serverSocket = new ServerSocket(port);
			running = true;
			
			// Starting Thread
			super.start();
		} catch (IOException e1) {
			System.err.println("Failed to start server: " + e1);
			running = false;
		}
	}
	
	public void close() {
		running = false;
		for (ServerThread thread : threads) {
			thread.close();
		}
		try {
			if(serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server stopped");
	}

	public void run() {
		while (running) {
			Socket socket = null;
            try {
                socket = serverSocket.accept();
                System.out.println("Client connected :)");
                
                Sender client = new Sender();
                
                ServerThread thread = new ServerThread(socket, receiveListeners, client);
                thread.start();
                threads.add(thread);
                
                for (ConnectionListener listener : connectionListeners) {
                	listener.newConnection(thread);
				}
            } catch (IOException e) {
            	if(!e.getMessage().contentEquals("Interrupted function call: accept failed")) {
            		e.printStackTrace();
            	}
            }
        }
	}
	
	public boolean addReceiveListener(ReceiveListener l) {
		return receiveListeners .add(l);
	}
	
	public boolean removeReceiveListener(ReceiveListener l) {
		return receiveListeners.remove(l);
	}
	
	public boolean addConnectionListener(ConnectionListener l) {
		return connectionListeners.add(l);
	}
	
	public boolean removeConnectionListener(ConnectionListener l) {
		return connectionListeners.remove(l);
	}	

	public int getPort() {
		return port;
	}

	public boolean isRunning() {
		return running;
	}

	public List<ServerThread> getClients() {
		return threads;
	}
}
