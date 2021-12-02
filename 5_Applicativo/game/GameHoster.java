/**Oggetto che contiene tutte le partite
 * @version 7 set 2021
 * @author Gioele Zanetti
 */

package game;

import java.util.Map;
import java.util.HashMap;

public class GameHoster {

    /**
     * Mappa che contiene le partite: queste sono caratterizzate 
     * dal loro token
     */
    private static Map<String, Game> games = new HashMap<>();
    
    /**
     * Genera il token della partita
     * @return un token casuale
     */
    public static String generateRandomGameName(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder token = new StringBuilder();
        for(int i=0;i<8;i++){
            token.append(characters.charAt((int)(Math.random() * characters.length())));
        }
        return token.toString();
    }
    
    /**
     * Controlla se una partita esiste già
     * @param gameToken il token da controllare
     * @return true se la partita esiste, false altrimenti
     */
    public static boolean containsGame(String gameToken){
        for(String gameName : games.keySet()){
            if(gameName.equals(gameToken)){
                return true;
            }
        }
        return false;
    }

    /**
     * aggiunge una partita alla lista
     * @param gameName il token della partita
     * @param turns i turni
     * @param length la lunghezza dei turni
     */
    public static void addGame(String gameName, int turns, int length){
        games.put(gameName, new Game(gameName, turns, length));
    }
    
    /**
     * Rimuove una partita
     * @param gameName il token della partita
     */
    public static void removeGame(String gameName){
        games.remove(gameName);
    }

    /**
     * Ritorna una partita
     * @param gameName il token della partita
     * @return una partita
     */
    public static Game getGame(String gameName)  {
        return games.get(gameName);
    }
    
    /**
     * Ritorna la lista formattata dei giocatori di ogni partita
     * @return la lista formattata dei giocatori di ogni partita
     */
    public static String getGamesList(){
        StringBuilder ris = new StringBuilder();
        for (String game : games.keySet()) {
            ris.append(games.get(game).toString());
        }
        return ris.toString();
    }
    
    /**
     * Ritorna la lista formattata dei giocatori di una parita
     * @param name il nome della partita
     * @return la lista formattata dei giocatori di una parita
     */
    public static String getGameList(String name){
        return games.containsKey(name) ?
            games.get(name).getPlayers():
            null;
    }
    
    /**
     * Ritorna la classifica di una partita
     * @param name il nome della partita
     * @return la classifica di una partita
     */
    public static String getLeaderboard(String name){
        return games.containsKey(name) ?
            games.get(name).getLeaderboard():
            null;
    }
}
