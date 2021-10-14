/**
 * @version 7 set 2021
 * @author Gioele Zanetti
 */

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 3000;
    private static List<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(100);
    
    public static void main(String[] args) {       
        try {
            ServerSocket listener = new ServerSocket(PORT);
            while(true){
                Socket client = listener.accept();
                ClientHandler clientThread = new ClientHandler(client, clients);
                clients.add(clientThread);
                pool.execute(clientThread);
            }
        } catch (IOException ex) {
            System.out.printf("Port %d not supported\n", PORT);
        }
    }
    
}
