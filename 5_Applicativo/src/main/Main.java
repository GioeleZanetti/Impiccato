/**Main class, permette di avviare il server e di 
 * giocare da terminale o da GUI
 *
 * @author gioele.zanetti
 * @version 09.12.2021
 */


package main;

import application.App;
import application.MainFrame;
import application.Playable;
import java.util.Scanner;
import server.Server;

public class Main {
    
    /**
     * Il tipo di applicazione (GUI o CLI)
     */
    private Playable application;
    
    /**
     * Costruttore vuoto della classe Main
     */
    public Main(){}
    
    /**
     * Setter attributo application
     * @param application l'applicazione
     */
    public void setApplication(Playable application){
        this.application = application;
    }
    
    /**
     * Ritorna se l'applicazione è diversa da null
     */
    public boolean hasApplication(){
        return application != null;
    }
    
    /**
     * Avvia il gioco
     */
    public void play(){
        application.play();
    }

    
    public static void main(String[] args) {
        Main m = new Main();
        Scanner s = new Scanner(System.in);
        
        boolean done = false;
        
        System.out.println("Write \"Server\" to start server \n"
                + "Write \"Client\" to choose interface");
        System.out.print(">");
        while(!done){
            String cmd = s.nextLine();
            if(cmd.equals("Client")){
                System.out.println("Write \"GUI\" to play on GUI \n"
                    + "Write \"CLI\" to play on CLI");
                System.out.print(">");
                cmd = s.nextLine();
                if(cmd.equals("GUI")){
                    m.setApplication(new MainFrame());
                    m.play();
                    done = true;
                }else if(cmd.equals("CLI")){
                    m.setApplication(new App());
                    m.play();
                    done = true;
                }
            }else if(cmd.equals("Server")){
                (new Thread(new Server())).start();
                System.out.println("Server started");
                System.out.println("Close this window to stop server");
                done = true;
            }else{
                System.out.println("Unknown command, please try again");
            }
        }
    }  
}
