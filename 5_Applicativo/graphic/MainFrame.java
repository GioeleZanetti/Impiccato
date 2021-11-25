/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import application.App;
import client.Client;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Gioele Zanetti
 * @version 11.11.2021
 */
public class MainFrame extends JFrame{
    
    private Client c;
    private Addable panel;
    
    /**
     * Creates new form MainWindow
     */
    public MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 500);
        this.addPanel(new MainPanel(this));
        this.setResizable(false);
        try {
            c = new Client(this, true);
        } catch (IOException ex) {
            removePanel();
            addPanel(new ErrorPanel());
        }
    }

    public void addPanel(Addable panel){
        this.panel = panel;
        ((JPanel)this.panel).setVisible(true);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(((JPanel)this.panel),CENTER);
        pack();
        setVisible(true);
    }
    
    
    public void removePanel() {
        this.getContentPane().removeAll();
    }
    
    public Client getClient(){
        return c;
    }
    
    public void setUsername(String username){
        this.c.setUsername(username);
    }
    
    public Addable getCurrentPanel(){
        return panel;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }  
}
