/**Rappresenta un giocatore dell'impiccato
 * @version 14 set 2021
 * @author Gioele Zanetti
 */

package game;

public class Player {

    /**
     * L'username scelto
     */
    private String name;
    
    /**
     * I punti che ha fatto
     */
    private int points;
    
    /**
     * Se ha finito o meno una partita
     */
    private boolean hasFinished;
    
    /**
     * Quanti errori ha fatto
     */
    private int errors;
    
    /**
     * Istanzia una nuova istanza di un giocatore
     * @param name il nome del giocatore
     * @param points i punti che ha fatto
     */
    public Player(String name, int points){
        this.name = name;
        this.points = points;
        this.hasFinished = false;
        this.errors = 0;
    }
    
    /**
     * Istanzia una nuova istanza di un giocatore, mettendo
     * i punti fatti a 0
     * @param name Il nome del giocatori
     */
    public Player(String name){
        this(name, 0);
    }
    
    /**
     * Ritorna il nome del giocatore
     * @return 
     * il nome del giocatore
     */
    public String getName(){
        return name;
    }
    
    /**
     * Ritorna il punteggio del giocatore
     * @return il punteggio del giocatore
     */
    public int getPoints(){
        return points;
    }
    
    /**
     * Imposta il nome del giocatore
     * @param name il nome del giocatore
     */
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Imposta i punte del giocatore
     * @param points i punti 
     */
    public void setPoints(int points){
        if(points > 0){
            this.points = points;
        }
    }
    
    /**
     * Ritorna se un giocatore ha finito il turno o no
     * @return se un giocatore ha finito il turno
     */
    public boolean hasFinished(){
        return hasFinished;
    }
    
    /**
     * Imposta se un giocatore ha finito o no
     * @param state lo stato del giocatore
     */
    public void setHasFinished(boolean state){
        this.hasFinished = state;
    }
    
    /**
     * Aggiunge un errore
     */
    public void addError(){
        this.errors += 1;
    }
    
    /**
     * Aggiunge dei punti
     * @param points i punti da aggiungere
     */
    public void addPoints(int points){
        if(this.errors != 10){
            this.points += points;
        }
    }
    
    /**
     * Ritorna il numero di errori
     * @return il numero di errori
     */
    public int getErrors(){
        return this.errors;
    }
    
    /**
     * Imposta gli errori a 0
     */
    public void setErrorsToZero(){
        this.errors = 0;
    }
    
    /**
     * Ritorna una rappresentazione sotto forma di stringa 
     * di un giocatore
     * @return una rappresentazione sotto forma di stringa 
     */
    public String toString(){
        return name + " : " + points;
    }
}
