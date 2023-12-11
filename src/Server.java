import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Server {
    private static HashMap<String, ClientHandler> clients = new HashMap<>();
    private static int clientCounter = 0;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server started on port 8080...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + "Client-" + clientCounter);
                ClientHandler clientHandler = new ClientHandler(clientSocket, "Client-" + clientCounter);
                clients.put(clientHandler.getClientName(), clientHandler);
                new Thread(clientHandler).start();
                clientCounter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients.values()) {
            if (client != sender) {
                client.sendMessage(sender.getClientName() + ": " + message);
            }
        }
    }

}