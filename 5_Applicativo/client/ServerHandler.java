/**Istanza per ascoltare i messaggi del server
 * @version 7 set 2021
 * @author Gioele Zanetti
 */

package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerHandler implements  Runnable{
    
    /**
     * Il socket con il server
     */
    private Socket server;
    
    /**
     * Il client corrente
     */
    private Client c;
    
    /**
     * Lo stream di dati dal server
     */
    private DataInputStream in;

    
    /**
     * Istanzia una nuova istanza per ascoltare 
     * i messaggi del server
     * @param server il socket con il server
     * @param c il client corrente
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    public ServerHandler(Socket server, Client c) throws IOException {
        this.server = server;
        in = new DataInputStream(server.getInputStream());
        this.c = c;
    }
    
    @Override
    public void run() {
        try{
            int current;
            //leggo quanti byte devo leggere
            while ((current = in.read()) != -1) {
                byte[] packet = new byte[current];
                //leggo il numero di byte specificato
                in.read(packet, 0, current);
                //elaboro la risposta
                c.elaborateResponse(packet);
                //controllo se la thread è stata fermata
                if(Thread.interrupted()){
                    return;
                }
            }
        }catch(IOException e){
            return;
        }
    }

}
