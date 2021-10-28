/**
 * @version 7 set 2021
 * @author Gioele Zanetti
 */

package game;

import java.util.ArrayList;

public class Game {
    
    private String name;
    private ArrayList<Player> players;
    private boolean hasStarted;
    private String word;
    private String admin;
    private final int LENGTH_IN_SECONDS;
    private long beginning;
    private int turn;
    private final int TURN_NUMBER;

    public Game(String gameName) {
        this(gameName, 5, 60);
    }
    
    public Game(String gameName, int turns, int lengthInSeconds){
        this.name = gameName;
        this.players = new ArrayList<>();
        this.hasStarted = false;
        this.word = Word.getRandomWord();
        this.admin = "";
        if(lengthInSeconds > 300 || lengthInSeconds < 30)
            lengthInSeconds = 60;
        this.LENGTH_IN_SECONDS = lengthInSeconds;
        this.beginning = 0;
        this.turn = 0;
        if(turns > 10 || turns < 0)
            turns = 2;
        this.TURN_NUMBER = turns;
    }
    
    public void startGame(){
        hasStarted = true;
        this.beginning = System.currentTimeMillis();
    }
    
    public void stopGame(){
        hasStarted = false;
    }
    
    public void setAdmin(String userName){
        admin = userName;
    }
    
    public String getAdmin(){
        return admin;
    }
    
    public String getName(){
        return name;
    }
    
    public String getWord(){
        return word;
    }
    
    public int getLengthInSeconds(){
        return this.LENGTH_IN_SECONDS;
    }
   
    public void addPlayer(String name){
        players.add(new Player(name));
    }
    
    public void removePlayer(String name){
        for(int i=0;i<players.size();i++){
            if(players.get(i).getName().equals(name)){
                players.remove(i);
            }
        }
    }
    
    public Player getPlayer(String playerName){
        for(int i=0;i<players.size();i++){
            if(players.get(i).getName().equals(playerName)){
                return players.get(i);
            }
        }
        return null;
    }
    
    public boolean containsName(String userName){
        for(int i=0;i<players.size();i++){
            if(players.get(i).getName().equals(userName)){
                return true;
            }
        }
        return false;
    }
    
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
    
    public void nextTurn(){
        this.turn++;
        this.word = Word.getRandomWord();
        this.beginning = System.currentTimeMillis();
        setPlayerErrorsToZero();
        setPlayerHasFinished(false);
    }
    
    public void setPlayerErrorsToZero(){
        for(Player p : players){
            p.setErrorsToZero();
        }
    }
    
    public void setPlayerHasFinished(boolean state){
        for(Player p : players){
            p.setHasFinished(state);
        }
    }
    
    public byte[] isLetterRight(byte c){
        byte[] indexes = new byte[0];
        for(int i=0;i<word.length();i++){
            if(word.charAt(i) == c){
                indexes = add(indexes, i);
            }
        }
        return indexes;
    }
    
    public byte[] add(byte[] array, int index){
        byte[] newIndexes = new byte[array.length + 1];
        for(int i=0;i<array.length;i++){
            newIndexes[i] = array[i];
        }
        newIndexes[array.length] = (byte)index;
        return newIndexes;
    }
        
    public void addPointsToPlayer(String playerName){
        double timeFormBeginning = (System.currentTimeMillis() - this.beginning) / 1000;
        if(timeFormBeginning <= this.LENGTH_IN_SECONDS){
            double points = ((this.LENGTH_IN_SECONDS - timeFormBeginning) / this.LENGTH_IN_SECONDS) * 500.0;
            getPlayer(playerName).addPoints((int)points);
        }
    }

    public String toString(){
        String ris = name + ":\n";
        for(Player p : players){
            ris += p + ", ";
        }
        ris += "\n\n";
        return ris;
    }
    
    public String getPlayers(){
        String ris = "";
        for(int i=0;i<players.size() - 1;i++){
            ris += players.get(i) + "\n";
        }
        ris += players.get(players.size() - 1).toString();
        return ris;
    }
}
