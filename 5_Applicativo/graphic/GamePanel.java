/**Pannello di gioco
 *
 * @author gioele.zanetti
 * @version 11.11.2021
 */

package graphic;


import application.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.Timer;
import javax.swing.Timer;

public class GamePanel extends javax.swing.JPanel 
implements Addable, KeyListener{

    /**
     * Il frame che contiene il pannello
     */
    private MainFrame frame;
    
    /**
     * La lista dei giocatori
     */
    private String playerList;
    
    /**
     * La parola censurata
     */
    private String word;
    
    /**
     * La lunghezza del turno
     */
    private int time;
    
    /**
     * Il tempo dall'inizio della partita
     */
    private int timePassed;
    
    /**
     * Il timer che mostra il tempo rimanente
     */
    private Timer timer;
    
    /**
     * Gli errori compiuti dal giocatore
     */
    private int errors;
    
    /**
     * Crea un nuovo form GamePanel
     * @param frame il frame che contiene il panello
     * @param playerList la lista dei giocatori
     */
    public GamePanel(MainFrame frame, String playerList) {
        initComponents();
        this.frame = frame;
        this.playerList = playerList;
        jList1.setListData(playerList.split("\n"));
        this.word = frame.getClient().getMaskedWord();
        jTextField2.setText(word);
        jTextField5.addKeyListener(this);
        this.timer = new Timer(1000, action);
        this.time = frame.getClient().getLengthInSeconds();
        this.errors = 0;
        jTextField1.setText(Integer.toString(errors));
        timer.start();
    }    
    
    /**
     * L'azione compiuta dal timer
     */
    ActionListener action = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            timePassed++;
            jTextField3.setText(Integer.toString((time - timePassed)));
        }
    };
    
    public void setData(Object o, String parameter) {
        if(parameter.equals("end game")){
            timer.stop();
            frame.removePanel();
            frame.addPanel(new FinalPanel(this.frame));
        }else if(parameter.equals("masked word")){
            this.word = (String)o;
            jTextField2.setText((String)o);
        }else if(parameter.equals("playerList")){
            this.playerList = (String)o;
            jList1.setListData(((String)o).split("\n"));
        }else if(parameter.equals("error")){
            jLabel1.setText((String)o);
        }else if(parameter.equals("word")){
            jTextArea2.append("The word was " + (String)o + "!\n");
        }else if(parameter.equals("end turn")){
            timer.stop();
            this.errors = 0;
            this.timePassed = 0;
            jTextField1.setText("0");
            timer.start();
        }else if(parameter.equals("errors")){
            this.errors = (int)o;
            jTextField1.setText(Integer.toString(errors));
        }else if(parameter.equals("message")){
            jTextArea2.append((String)o + "\n");
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

        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();

        jTextField2.setEditable(false);
        jTextField2.setText("Parola");

        jTextField3.setEditable(false);

        jTextField5.setText("Inserimento lettera");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jTextField1.setEditable(false);
        jTextField1.setText("Errori");

        jScrollPane3.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                            .addComponent(jScrollPane2)
                            .addComponent(jTextField5))
                        .addGap(12, 12, 12)))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    /**
     * Evento che ascolta quando viene rilasciato il tasto invio
     * @param e l'evento della tastiera
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == '\n'){
            try{
                this.frame.getClient().elaborateRequest(jTextField5.getText());
                jTextField5.setText("");
            }catch(IOException ioe){
                return;
            }
        }
    }
}
