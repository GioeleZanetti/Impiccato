/**Form che contiene tutti i pannelli
 *
 * @author Gioele Zanetti
 * @version 11.11.2021
 */

package graphic;

import application.App;
import client.Client;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame{
    
    /**
     * Il client corrente
     */
    private Client c;
    
    /**
     * Il pannello corrente
     */
    private Addable panel;
    
    /**
     * Crea un nuovo MainFrame
     */
    public MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 500);
        this.addPanel(new MainPanel(this));
        this.setResizable(false);
        try {
            c = new Client(this);
        } catch (IOException ex) {
            removePanel();
            addPanel(new ErrorPanel());
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
     * Main
     * @param args aregomenti da linea di comando
     */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }  
}
