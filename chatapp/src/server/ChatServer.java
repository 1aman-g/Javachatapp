package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private static Set<PrintWriter> clients = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Chat Server running on port 1234...");
        ServerSocket serverSocket = new ServerSocket(1234);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");
            new ClientHandler(socket).start();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        ClientHandler(Socket socket) throws Exception {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            clients.add(out);
        }

        public void run() {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    for (PrintWriter writer : clients) {
                        writer.println(msg);
                    }
                }
            } catch (IOException e) {
                System.out.println("A client disconnected.");
            } finally {
                clients.remove(out);
                try { socket.close(); } catch (Exception ignored) {}
            }
        }
    }
}

