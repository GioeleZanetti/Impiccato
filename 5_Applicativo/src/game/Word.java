/**Estrae parole casuali
 * @version 17 set 2021
 * @author Gioele Zanetti
 */

package game;

import java.nio.file.*;
import java.io.*;
import java.util.List;

public class Word {
    
    /**
     * Il percorso del file con le parole
     */
    private static Path path = Paths.get("files","words.txt");
    
    /**
     * Ritorna una parola casuale
     * @return una parola casuale
     */
    public static String getRandomWord(){
        try{
            List<String> words = Files.readAllLines(path);
            return words.get((int)(Math.random() * words.size()));
        }catch(IOException e){
            return null;
        }
    }
}
