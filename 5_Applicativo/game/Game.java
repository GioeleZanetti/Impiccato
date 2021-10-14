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
    private int lengthInSeconds;
    private long beginning;

    public Game(String gameName) {
        this.name = gameName;
        this.players = new ArrayList<>();
        this.hasStarted = false;
        this.word = Word.getRandomWord();
        this.admin = "";
        this.lengthInSeconds = 60;
        this.beginning = 0;
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
    
    public boolean gameEnd(){
        boolean gameEnd = true;
        for(int i=0;i<players.size();i++){
            gameEnd = gameEnd && players.get(i).hasFinished();
        }
        return gameEnd;
    }
    
    public byte[] add(byte[] array, int index){
        byte[] newIndexes = new byte[array.length + 1];
        for(int i=0;i<array.length;i++){
            newIndexes[i] = array[i];
        }
        newIndexes[array.length] = (byte)index;
        return newIndexes;
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
