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

public class Server implements Runnable{

    /**
     * La porta in cui il server ascolta
     */
    private static final int PORT = 3000;
    
    /**
     * La lista di client
     */
    private static List<ClientHandler> clients = new ArrayList<>();
    
    /**
     * Il pool in cui vengono eseguiti i client
     */
    private static ExecutorService pool = Executors.newFixedThreadPool(100);
    
    public void run(){
        try {
            ServerSocket listener = new ServerSocket(PORT);
            while(true){
                Socket client = listener.accept();
                ClientHandler clientThread = new ClientHandler(client, clients);
                clients.add(clientThread);
                pool.execute(clientThread);
            }
        } catch (IOException ex) {
            System.out.printf("Port %d not supported or busy\n", PORT);
        }
    }
    
    /**
     * Rimuove un client dalla lista
     * @param c il client da eliminare
     */
    public static void removeFromClients(ClientHandler c){
        while(clients.indexOf(c) != -1){
            clients.remove(c);
        }
    }
    
    /**
     * Ritorna la lista dei client
     * @return la lista dei client
     */
    public static List<ClientHandler> getClients(){
        return clients;
    }
}
