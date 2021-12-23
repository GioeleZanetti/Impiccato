/**Form che contiene tutti i pannelli
 *
 * @author Gioele Zanetti
 * @version 11.11.2021
 */

package application;

import client.Client;
import graphic.Addable;
import graphic.ErrorPanel;
import graphic.MainPanel;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame
implements Playable{
    
    /**
     * Il client corrente
     */
    private Client c;
    
    /**
     * Il pannello corrente
     */
    private Addable panel;
    
    /**
     * Mostra se è avvenuto un errore durante la 
     * connessione con il server
     */
    private boolean error;
    
    /**
     * Crea un nuovo MainFrame
     */
    public MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 500);
        //this.addPanel(new MainPanel(this));
        this.setResizable(false);
        try {
            System.out.println("Trying 127.0.0.1 on 3000...");
            c = new Client(this);
        } catch (IOException ex) {
            this.error = true;
        }
    }

    /**
     * Metodo utile ad aggiungere un pannello
     * @param panel il pannello da aggiungere
     */
    public void addPanel(Addable panel){
        this.panel = panel;
        ((JPanel)this.panel).setVisible(true);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(((JPanel)this.panel),CENTER);
        pack();
        setVisible(true);
    }
    
    /**
     * Metodo per rimuovere tutti i pannelli presenti
     */
    public void removePanel() {
        this.getContentPane().removeAll();
    }
    
    /**
     * Ritorna il client corrente
     * @return il client corrente
     */
    public Client getClient(){
        return c;
    }
    
    /**
     * Imposta l'username del giocatore
     * @param username l'username del giocatore
     */
    public void setUsername(String username){
        this.c.setUsername(username);
    }
    
    /**
     * Ritorna il pannello corrente
     * @return il pannello corrente
     */
    public Addable getCurrentPanel(){
        return panel;
    }

    /**
     * Imposta il client
     * @param port la porta del server
     * @param ip l'ip del server
     */
    public void setClient(int port, String ip){
        try {
            System.out.printf("trying %s on %d...\n", ip, port);
            c = new Client(this, null, port, ip);
            removePanel();
            addPanel(new MainPanel(this));
        } catch (IOException ex) {
            removePanel();
            addPanel(new ErrorPanel(this));
        }
    }
    
    @Override
    public void play(){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                if(!error){
                    frame.addPanel(new MainPanel(frame));
                }else{
                    frame.addPanel(new ErrorPanel(frame));
                }
            }
        });
    }
}
