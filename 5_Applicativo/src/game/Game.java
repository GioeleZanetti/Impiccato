/**Rappresenta una partita del gioco Impiccato
 * @version 7 set 2021
 * @author Gioele Zanetti
 */

package game;

import java.util.ArrayList;
import java.util.List;

public class Game {
    
    /**
     * Il token della partita
     */
    private String name;
    
    /**
     * La lista dei giocatori
     */
    private List<Player> players;
    
    /**
     * Indica se la partita è cominciata
     */
    private boolean hasStarted;
    
    /**
     * La parola estratta
     */
    private String word;
    
    /**
     * Il nome dell'admin
     */
    private String admin;
    
    /**
     * La lunghezza in secondi dei turni
     */
    private final int LENGTH_IN_SECONDS;
    
    /**
     * L'istante in cui il turno corrente è cominciato
     */
    private long beginning;
    
    /**
     * Indica in quale turno ci troviamo
     */
    private int turn;
    
    /**
     * Indica il numero di turni in una partita
     */
    private final int TURN_NUMBER;

    /**
     * Istanzia una nuova partita
     * @param gameName il token della partita
     */
    public Game(String gameName) {
        this(gameName, 5, 60);
    }
    
    /**
     * Istanzia una nuova partita
     * @param gameName Il token della partita
     * @param turns il numero di turni
     * @param lengthInSeconds la lugnghezza in secondi di ogni turno
     */
    public Game(String gameName, int turns, int lengthInSeconds){
        this.name = gameName;
        this.players = new ArrayList<>();
        this.hasStarted = false;
        this.word = Word.getRandomWord();
        this.admin = "";
        if(lengthInSeconds < 30)
            lengthInSeconds = 30;
        else if(lengthInSeconds > 200)
            lengthInSeconds = 200;
        this.LENGTH_IN_SECONDS = lengthInSeconds;
        this.beginning = 0;
        this.turn = 0;
        if(turns < 0)
            turns = 2;
        else if(turns > 10)
            turns = 10;
        this.TURN_NUMBER = turns;
    }
    
    /**
     * Ritorna la lista dei giocatori
     * @return la lista dei giocatori
     */
    public List<Player> getPlayerList(){
        return players;
    }
    
    /**
     * Metodo utile a cambiare lo stato della partita
     */
    public void startGame(){
        hasStarted = true;
        this.beginning = System.currentTimeMillis();
    }
    
    /**
     * Metodo che serve a stoppare una partita
     */
    public void stopGame(){
        hasStarted = false;
    }
    
    /**
     * Metodo che serve per impostare l'admin della partita
     * @param userName l'username dell'admin
     */
    public void setAdmin(String userName){
        admin = userName;
    }
    
    /**
     * Ritorna l'admin della partita
     * @return l'admin della partita
     */
    public String getAdmin(){
        return admin;
    }
    
    /**
     * Ritorna il token della partita
     * @return il token della partita
     */
    public String getName(){
        return name;
    }
    
    /**
     * ritorna la parola corrente
     * @return la parola corrente
     */
    public String getWord(){
        return word;
    }
    
    /**
     * Ritorna la lughezza in secondi di ogni turno
     * @return la lunghezza di ogni turno
     */
    public int getLengthInSeconds(){
        return this.LENGTH_IN_SECONDS;
    }
   
    /**
     * Aggiunge un giocatore alla lista
     * @param name il nome del giocatore
     */
    public void addPlayer(String name){
        players.add(new Player(name));
    }
    
    /**
     * Rimuove un giocatore dalla partita
     * @param name il nome del giocatore da rimouvere
     */
    public void removePlayer(String name){
        for(int i=0;i<players.size();i++){
            if(players.get(i).getName().equals(name)){
                players.remove(i);
            }
        }
    }
    
    /**
     * Ritorna un giocatore con un determinato nome
     * @param playerName il nome del giocatore
     * @return il giocatore
     */
    public Player getPlayer(String playerName){
        for(int i=0;i<players.size();i++){
            if(players.get(i).getName().equals(playerName)){
                return players.get(i);
            }
        }
        return null;
    }
    
    /**
     * Controlla che il nome inserito dall'utente non sia
     * già nella lista dei partecipanti
     * @param userName il nome utente
     * @return false se non c`é, true altrimenti
     */
    public boolean containsName(String userName){
        for(int i=0;i<players.size();i++){
            if(players.get(i).getName().equals(userName)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Controlla se un turno è finito
     * @return true se finito, false altrimenti
     */
    public boolean turnEnd(){
        if(turn < TURN_NUMBER - 1){
            boolean turnEnd = true;
            for(int i=0;i<players.size();i++){
                turnEnd = turnEnd && players.get(i).hasFinished();
            }
            return turnEnd;
        }
        return false;
    }
    
    /**
     * Controlla se la partita è finita
     * @return true se finita, false altrimenti
     */
    public boolean gameEnd(){
        if(turn == TURN_NUMBER - 1){
            boolean gameEnd = true;
            for(int i=0;i<players.size();i++){
                gameEnd = gameEnd && players.get(i).hasFinished();
            }
            return gameEnd;
        }
        return false;
    }
    
    /**
     * Cambia il turno della partita
     */
    public void nextTurn(){
        this.turn++;
        this.word = Word.getRandomWord();
        this.beginning = System.currentTimeMillis();
        setPlayerErrorsToZero();
        setPlayerHasFinished(false);
    }
    
    /**
     * Imposta gli errori di tutti i giocatori a 0
     */
    public void setPlayerErrorsToZero(){
        for(Player p : players){
            p.setErrorsToZero();
        }
    }
    
    /**
     * Imposta se un giocatore ha finito il turno o no
     * @param state lo stato (finito = true, non finito = false)
     */
    public void setPlayerHasFinished(boolean state){
        for(Player p : players){
            p.setHasFinished(state);
        }
    }
    
    /**
     * Controlla se la lettera digitata dall'utente è corretta
     * @param c la lettera inserita dall'utente
     * @return gli indici in cui compare la lettera, vuoto se 
     * la lettera è sbagliata
     */
    public byte[] isLetterRight(byte c){
        byte[] indexes = new byte[0];
        for(int i=0;i<word.length();i++){
            if(word.charAt(i) == c){
                indexes = add(indexes, i);
            }
        }
        return indexes;
    }
    
    /**
     * aggiunge un indice ad un array
     * @param array l'array di partenza
     * @param index l'indice da aggiungere
     * @return l'array di base con l'indice aggiunto
     */
    public byte[] add(byte[] array, int index){
        byte[] newIndexes = new byte[array.length + 1];
        for(int i=0;i<array.length;i++){
            newIndexes[i] = array[i];
        }
        newIndexes[array.length] = (byte)index;
        return newIndexes;
    }
        
    /**
     * aggiunge i punti a un giocatore in base a quanto
     * tempo rimane alla fine del turno
     * @param playerName il player a cui aggiungere i punti
     */
    public void addPointsToPlayer(String playerName){
        double timeFormBeginning = (System.currentTimeMillis() - this.beginning) / 1000;
        if(timeFormBeginning <= this.LENGTH_IN_SECONDS){
            double points = ((this.LENGTH_IN_SECONDS - timeFormBeginning) / this.LENGTH_IN_SECONDS) * 500.0;
            getPlayer(playerName).addPoints((int)points);
        }
    }

    /**
     * Ritorna una rappresentazione sotto forma di stringa
     * dell'oggetto corrente
     * @return una rappresentazione sotto forma di stringa
     */
    @Override
    public String toString(){
        String ris = name + ":\n";
        for(Player p : players){
            ris += p + ", ";
        }
        ris += "\n\n";
        return ris;
    }
    
    /**
     * Ritorna la lista dei giocatori formattata
     * @return la lista dei giocatori formattata
     */
    public String getPlayers(){
        String ris = "";
        if(players.size() != 0){
            for(int i=0;i<players.size() - 1;i++){
                ris += players.get(i) + "\n";
            }
            ris += players.get(players.size() - 1).toString();
        }
        return ris;
    }
    
    /**
     * Ritorna la classifica ordinata dei giocatori formattata
     * @return la classifica ordinata dei giocatori
     */
    public String getLeaderboard(){
        List<Player> leaderboard = getOrderedPlayerList();
        String ris = "";
        for(int i=0;i<leaderboard.size() - 1;i++){
            ris += leaderboard.get(i) + "\n";
        }
        ris += leaderboard.get(leaderboard.size() - 1).toString();
        return ris;
    }
    
    /**
     * Ordina la lista dei giocatori
     * @return la lista dei giocatori ordinata
     */
    public List<Player> getOrderedPlayerList(){
        List<Player> playersCopy = new ArrayList<>();
        playersCopy.addAll(players);
        int n = playersCopy.size();
        for (int i = 0; i < n-1; i++){
            for (int j = 0; j < n-i-1; j++){
                if (playersCopy.get(j).getPoints() < playersCopy.get(j + 1).getPoints())
                {
                    Player temp = playersCopy.get(j);
                    playersCopy.set(j, playersCopy.get(j + 1));
                    playersCopy.set(j + 1, temp);
                }
            }
        }
        return playersCopy;
    }

    /**
     * Metodo che ritorna se una partita è piena
     * (massimo 10 giocatori)
     * @return se la partita è piena
     */
    public boolean isFull() {
        return players.size() >= 10;
    }
}
