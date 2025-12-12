package client;

import java.io.*;
import java.net.*;
import common.ChatMessage;

public class ChatClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 1234);
        System.out.println("Connected to chat server!");

        BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter your name: ");
        String name = userIn.readLine();

        // Thread to read messages from server
        new Thread(() -> {
            try {
                String msg;
                while ((msg = serverIn.readLine()) != null) {
                    System.out.println(msg);
                }
            } catch (Exception e) {
                System.out.println("Server closed.");
            }
        }).start();

        // Sending messages
        while (true) {
            String input = userIn.readLine();
            ChatMessage message = new ChatMessage(name, input);
            serverOut.println(message.toString());
        }
    }
}

