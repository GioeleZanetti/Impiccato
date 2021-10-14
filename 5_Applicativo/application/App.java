/**
 * @version 7 set 2021
 * @author Gioele Zanetti
 */

package application;

import client.Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    
    private Client c;
    private BufferedReader k;

    public App(String userName) {
        try{
            this.c = new Client(this, userName);
            this.k = new BufferedReader(new InputStreamReader(System.in));
        }catch(IOException e){
            System.out.println("Error while creating client, server not available");
            System.exit(0);
        }
    }
    
    public Client getClient(){
        return this.c;
    }
    
    public BufferedReader getKeyboard(){
        return this.k;
    }
    
    public void writeOnConsole(String msg){
        System.out.println(msg);
    }
    
    public static void main(String[] args) {
        try{
            App a = new App("");
            a.c.changeUsername(a.k);
            while (true) {             
                a.getClient().elaborateRequest(a.k.readLine());
            }
        }catch(IOException e){
            
        }
    }
}
