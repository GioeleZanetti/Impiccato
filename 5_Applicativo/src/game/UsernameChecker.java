/**Classe per controllare l'username inserito
 * dai giocatori
 *
 * @author gioele.zanetti
 * @version 17 set 2021
 */
package game;

import java.nio.file.*;
import java.io.*;
import java.util.List;

public class UsernameChecker {
    
    /**
     * Controlla che un unsername sia valido
     * Non deve contenere parolacce e non deve essere
     * più lungo di 20 carateri
     * @param username il nome inserito dall'utente
     * @throws IllegalArgumentException se il nome non è valido
     */
    public static void validateUsername(String username) 
    throws IllegalArgumentException{
        try{
            List<String> badWords = Files.readAllLines(Paths.get("files","badwords.txt"));
            if(containsBadWords(badWords, username.replaceAll(" ", ""))){
                throw new IllegalArgumentException("Name contains words that are not allowed");
            }else if(username.length() > 20){
                throw new IllegalArgumentException("Name must contain a maximum of 20 characters");
            }
        }catch(IOException ioe){
            return;
        }
    }
    
    /**
     * Controlla che il nome utente non contenga parolaccie
     * @param words le parolacce
     * @param word l'username
     * @return true se contiene parolacce, false altrimenti
     */
    private static boolean containsBadWords(List<String> words, String word){
        for(int i=0;i<words.size();i++){
            if(word.toLowerCase().contains(words.get(i))){
                return true;
            }
        }
        return false;
    }
    
}
