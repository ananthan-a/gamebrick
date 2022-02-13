package in.gamebrick.server;

import in.gamebrick.server.websockethandler.WebSocketManager;

public class ServerMain {
    public static void main(String[] args) {
        System.out.println("GameBrick Server Started");

        if (!WebSocketManager.initialize(args)) {
            System.exit(1);
        }else{
            System.out.println("Websocket created successfully");
        }
    }
}