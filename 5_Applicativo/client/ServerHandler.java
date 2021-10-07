package client;

/**
 *
 * @author gioele.zanetti
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerHandler implements  Runnable{
    
    private Socket server;
    private Client c;
    private DataInputStream in;

    public ServerHandler(Socket server, Client c) throws IOException {
        this.server = server;
        in = new DataInputStream(server.getInputStream());
        this.c = c;
    }
    
    @Override
    public void run() {
        try{
            int current;
            while ((current = in.read()) != -1) {
                byte[] packet = new byte[current];
                in.read(packet, 0, current);
            }
        }catch(IOException e){
            
        }
    }

}

