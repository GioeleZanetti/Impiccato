/**Client da terminale per il gioco dell'impiccato
 * 
 * @version 7 set 2021
 * @author Gioele Zanetti
 */

package application;

import client.Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App implements Playable{
    
    /**Il client che permette di scambiare informazioni con il server
     * 
     */
    private Client c;
    
    /**Permette di leggere input dalla tastiera
     * 
     */
    private BufferedReader k;

    /**istanzia un istanza della classe App
     * 
     */
    public App() {
        try{
            this.c = new Client(this);
            this.k = new BufferedReader(new InputStreamReader(System.in));
        }catch(IOException e){
            System.out.println("Error while creating client, server not available");
            System.exit(0);
        }
    }
    
    /**Ritorna il client corrente
     * 
     * @return il client corrente
     */
    public Client getClient(){
        return this.c;
    }
    
    /**Ritorna l'input della tastiera
     * 
     * @return input tastiera
     */
    public BufferedReader getKeyboard(){
        return this.k;
    }
    
    /**permette di scrivere sulla console dell'app
     * 
     * @param msg il messaggio da scrivere
     */
    public void writeOnConsole(String msg){
        System.out.println(msg);
    }
    
    public void play(){
        boolean done = false;
        try{
            App a = new App();
            a.c.changeUsername(a.k);
            while (!done) {
                String line = a.k.readLine();
                a.getClient().elaborateRequest(line);
            }
        }catch(IOException e){
            
        }
    }
}
