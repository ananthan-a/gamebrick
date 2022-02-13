package in.gamebrick.server.websockethandler;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WebSocketManager {
	public WebSocketManager(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	private ServerSocket serverSocket;

	public static boolean initialize(String[] args) {
		int port = Websockerconstant.WS_PORT;
		int backlog = Websockerconstant.WS_BACKLOG;
		InetAddress ip = Websockerconstant.getIP();
		try{
			if(args.length >= 1){
				port = Integer.parseInt(args[0]);
			}
			if(args.length >= 2){
				ip = InetAddress.getByName(args[1]);
			}
		}catch(Exception e){}
		try {
			ServerSocket serverSocket = new ServerSocket(port, backlog, ip);
			System.out.println("ServerSocket created with IP:"+ip+", and PORT:"+port);
			WebSocketManager websocket = new WebSocketManager(serverSocket);
			websocket.startAcceptingSocket();
			return true;
		} catch (Exception e) {
			System.out.println("Exception while creating serversocket : " + e);
		}
		return false;
	}

	public void startAcceptingSocket() {
		try {
			while (!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				WebSocketClientHandler clientHandler = new WebSocketClientHandler(socket);
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		} catch (Exception e) {
			System.out.println("Exception while accept new socket : " + e);
			closeServerSocket();
		}
	}

	public void closeServerSocket() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (Exception e) {
			System.out.println("Exception while close serverSocket : " + e);
		}
	}
}
