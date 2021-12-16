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
        boolean serverStarted = false;
        
        System.out.println("Write \"Server\" to start server \n"
                + "Write \"Client\" to choose interface");
        System.out.print(">");
        while(!done){
            String cmd = "";
            if(!serverStarted){
                cmd = s.nextLine();
            }
            if(cmd.equals("Client") || serverStarted){
                System.out.println("Write \"GUI\" to play on GUI \n"
                    + "Write \"CLI\" to play on CLI");
                System.out.print(">");
                cmd = s.nextLine();
                if(cmd.equals("GUI")){
                    m.setApplication(new MainFrame());
                    done = true;
                }else if(cmd.equals("CLI")){
                    m.setApplication(new App());
                    done = true;
                }
            }else if(cmd.equals("Server") && !serverStarted){
                (new Thread(new Server())).start();
                serverStarted = true;
                System.out.println("Write \"Yes\" to choose interface \n"
                    + "Write \"No\" to only start server");
                System.out.print(">");
                cmd = s.nextLine();
                if(cmd.equals("No")){
                    done = true;
                }
            }
        }
        if(m.hasApplication()){
            m.play();
        }else{
            System.out.println("Write \"Quit\" to quit");
            while(true){
                String cmd = s.nextLine();
                if(cmd.equals("Quit")){
                    System.exit(0);
                }
            }
        }
        
        
    }
    
}
