package server;

/**
 *
 * @author gioele.zanetti
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{

    private Socket client;
    private DataOutputStream out;
    private DataInputStream in;
    private List<ClientHandler> clients;

    public ClientHandler(Socket client, List<ClientHandler> clients) throws IOException {
        this.client = client;
        this.clients = clients;
        
        out = new DataOutputStream(client.getOutputStream());
        in = new DataInputStream(client.getInputStream());
    }
    
    
    @Override
    public void run() {
        try {
            int current;
            while ((current = in.read()) != -1) {
                byte[] packet = new byte[current];
                in.read(packet, 0, current);
            }
        } catch (IOException ex) {}
        finally{
            try {
                in.close();
            } catch (IOException ex) {}
        }
    }
    
    public static void main(String[] args) {

    }
}

