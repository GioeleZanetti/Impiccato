/**Main class, permette di giocare da terminale o da GUI
 *
 * @author gioele.zanetti
 * @version 09.12.2021
 */


package main;

import application.App;
import application.MainFrame;
import application.Playable;
import java.util.Scanner;

public class Main {
    
    private Playable application;
    
    public Main(){}
    
    public void setApplication(Playable application){
        this.application = application;
    }
    
    public void play(){
        application.play();
    }

    
    public static void main(String[] args) {
        Main m = new Main();
        Scanner s = new Scanner(System.in);
        
        boolean done = false;
        System.out.println("Write \"GUI\" to play on GUI \n"
                + "Write \"CLI\" to play on CLI");
        System.out.print(">");
        
        while(!done){
            String cmd = s.nextLine();
            if(cmd.equals("GUI")){
                m.setApplication(new MainFrame());
                done = true;
            }else if(cmd.equals("CLI")){
                m.setApplication(new App());
                done = true;
            }
        }
        
        m.play();
    }
    
}
