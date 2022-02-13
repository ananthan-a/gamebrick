package in.gamebrick.server.websockethandler;

import java.net.InetAddress;

public class Websockerconstant {
    public static final int WS_PORT = 2408;
    public static final int WS_BACKLOG = 30;
    public static final InetAddress getIP(){
        try{
            return InetAddress.getByName("127.0.0.1");
        }catch(Exception e){}
        return null;
    }
    
}
