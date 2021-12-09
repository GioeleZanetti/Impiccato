/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import application.MainFrame;
import application.App;
import client.Client;
import game.UsernameChecker;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author gioele.zanetti
 */
public class MainPanel extends javax.swing.JPanel 
implements Addable{

    private MainFrame frame;
    
    /**
     * Creates new form MainPanel
     */
    public MainPanel(MainFrame frame) {
        initComponents();
        this.frame = frame;

    }
    
    public MainPanel(MainFrame frame, String username) {
        initComponents();
        this.frame = frame;
        jTextField1.setText(username);
    }

    
    @Override
    public void setData(Object o, String parameter) {
        if(parameter.equals("gameToken")){
            this.frame.getClient().setGameName((String)o);
            this.frame.removePanel();
            this.frame.addPanel(new WaitingPanel(this.frame));
        }else if(parameter.equals("join game")){
            this.frame.getClient().setGameName((String)o);
            this.frame.removePanel();
            this.frame.addPanel(new WaitingPanel(this.frame, (String)o));
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();

        jTextField1.setText("Username");

        jButton1.setText("Crea partita");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Unisciti ad una partita");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField2.setText("Token");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(77, 77, 77)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(124, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(!jTextField1.getText().isBlank() && !jTextField1.getText().isEmpty()
        && !jTextField2.getText().isBlank() && !jTextField2.getText().isEmpty()
        && UsernameChecker.isUsernameValid(jTextField1.getText())){
            frame.setUsername(jTextField1.getText());
            this.frame.removePanel();
            this.frame.addPanel(new CreateGamePanel(this.frame));
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(!jTextField1.getText().isBlank() && !jTextField1.getText().isEmpty()
        && !jTextField2.getText().isBlank() && !jTextField2.getText().isEmpty()
        && UsernameChecker.isUsernameValid(jTextField1.getText())){
            frame.setUsername(jTextField1.getText());
            try {
                frame.getClient().elaborateRequest("join game " + jTextField2.getText());
            } catch (IOException ex) {
                
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
