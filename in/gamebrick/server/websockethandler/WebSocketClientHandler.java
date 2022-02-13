package in.gamebrick.server.websockethandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class WebSocketClientHandler implements Runnable{
	public static ArrayList<WebSocketClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String clientName;

	public WebSocketClientHandler(Socket socket){
		try{
			this.socket = socket;
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.clientName = bufferedReader.readLine();
			clientHandlers.add(this);
			System.out.println("New Client Connected, Name : " + clientName);
			broadcastMessage("Server : " + clientName + " has entered the chat!");
		}catch(Exception e){
			closeTheSocket(socket, bufferedReader, bufferedWriter);
		}
	}

	public void run(){
		String messageFromClient;

		while(socket.isConnected()){
			try{
				messageFromClient = bufferedReader.readLine();
				broadcastMessage(messageFromClient);
			}catch(Exception e){
				closeTheSocket(socket, bufferedReader, bufferedWriter);
				break;
			}
		}
	}

	public void broadcastMessage(String msg){
		for(WebSocketClientHandler clientHandler : clientHandlers){
			try{
				if(!clientHandler.clientName.equals(clientName)){
					clientHandler.bufferedWriter.write(msg);
					clientHandler.bufferedWriter.newLine();
					clientHandler.bufferedWriter.flush();
				}
			}catch(Exception e){
				closeTheSocket(socket, bufferedReader, bufferedWriter);
			}
		}
	}

	public void closeTheSocket(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
		removeClientHandler();
		try{
			if(bufferedReader != null){
				bufferedReader.close();
			}
			if(bufferedWriter != null){
				bufferedWriter.close();
			}
			if(socket != null){
				socket.close();
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}

	public void removeClientHandler(){
		if(clientHandlers.contains(this)){
			clientHandlers.remove(this);
			broadcastMessage("SERVER : " + clientName + " has left the chat!");
			System.out.println(clientName + " has left the chat!");
		}
	}
}