import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static int clientCounter = 0;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server started on port 8080...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + "Client-" + clientCounter);
                ClientHandler clientHandler = new ClientHandler(clientSocket, "Client-" + clientCounter);
                clients.add(clientHandler);
                sendPreviousMessages(clientHandler);
                new Thread(clientHandler).start();
                clientCounter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void broadcastMessage(String message, ClientHandler sender) {
        logEvent(sender.getClientName() + ": " + message);
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(sender.getClientName() + ": " + message);
            }
        }
    }

    private static void sendPreviousMessages(ClientHandler clientHandler) {
        try (Scanner logScanner = new Scanner(new File("server.log"))) {
            while (logScanner.hasNextLine()) {
                String message = logScanner.nextLine();
                clientHandler.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void logEvent(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("server.log", true))) {
            writer.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
