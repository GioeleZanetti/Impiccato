/**
 * @version 7 set 2021
 * @author Gioele Zanetti
 */

package game;

import java.util.Map;
import java.util.HashMap;

public class GameHoster {

    private static Map<String, Game> games = new HashMap<>();
    
    public static String generateRandomGameName(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder token = new StringBuilder();
        for(int i=0;i<8;i++){
            token.append(characters.charAt((int)(Math.random() * characters.length())));
        }
        return token.toString();
    }
    
    public static boolean containsGame(String gameToken){
        for(String gameName : games.keySet()){
            if(gameName.equals(gameToken)){
                return true;
            }
        }
        return false;
    }

    public static void addGame(String gameName){
        games.put(gameName, new Game(gameName));
    }
    
    public static void removeGame(String gameName){
        games.remove(gameName);
    }

    public static Game getGame(String gameName)  {
        return games.get(gameName);
    }
    
    public static String getGamesList(){
        StringBuilder ris = new StringBuilder();
        for (String game : games.keySet()) {
            ris.append(games.get(game).toString());
        }
        return ris.toString();
    }
    
    public static String getGameList(String name){
        return games.containsKey(name) ?
            games.get(name).getPlayers():
            null;
    }
}
