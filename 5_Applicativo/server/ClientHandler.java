/**
 * @version 7 set 2021
 * @author Gioele Zanetti
 */

package server;

import game.GameHoster;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import protocol.ProtocolCodes;

public class ClientHandler implements Runnable{

    private Socket client;
    private DataOutputStream out;
    private DataInputStream in;
    private List<ClientHandler> clients;
    private String gameToken;

    public ClientHandler(Socket client, List<ClientHandler> clients) throws IOException {
        this.client = client;
        this.clients = clients;
        gameToken = "";
        
        out = new DataOutputStream(client.getOutputStream());
        in = new DataInputStream(client.getInputStream());
    }
    
    public void setGameToken(String gameToken){
        this.gameToken = gameToken;
    }
    
    public String getGameToken(){
        return gameToken;
    }
    
    @Override
    public void run() {
        try {
            int current;
            while ((current = in.read()) != -1) {
                byte[] packet = new byte[current];
                in.read(packet, 0, current);
                
                elaborateRequest(packet);
            }
        } catch (IOException ex) {}
        finally{
            try {
                in.close();
            } catch (IOException ex) {}
        }
    }

    private void elaborateRequest(byte[] request) throws IOException {

    }
    
    private byte[] readFromTo(byte[] packet, int start, int end){
        byte[] data = new byte[end - start];
        for(int i=0;i<data.length;i++){
            data[i] = packet[start + i];
        }
        return data;
    }
    
    private void broadcastToGame(String gameToken, byte [] packet) throws IOException{
        for(ClientHandler c : clients){
            if(c.getGameToken().equals(gameToken)){
                c.out.write(ProtocolCodes.buildBroadcastMessagePacket(packet));
            }
        }
    }
    
    public static void main(String[] args) {

    }
}
