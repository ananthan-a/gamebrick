package in.gamebrick.client;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public Client(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = userName;
        } catch (Exception e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(userName + " : " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (Exception e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
        return;
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            public void run() {
                String msgFromGroupChat = "";
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    } catch (Exception e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        return;
                    }
                    if (msgFromGroupChat == null) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        return;
                    }
                }
                closeEverything(socket, bufferedReader, bufferedWriter);
                return;
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        System.out.println("closeing connection....");
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name : ");
        String userName = scanner.nextLine();
        int port = 2408;
        String ip = "127.0.0.1";
        try {
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }
            if (args.length >= 2) {
                ip = args[1];
            }
        } catch (Exception e) {
        }
        System.out.println("Connecting with IP:" + ip + ", PORT:" + port);
        Socket socket = new Socket(ip, port);
        Client client = new Client(socket, userName);
        client.listenForMessage();
        client.sendMessage();
    }
}
