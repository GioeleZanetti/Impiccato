/**
 * @version 17 set 2021
 * @author Gioele Zanetti
 */

package game;

import java.nio.file.*;
import java.io.*;
import java.util.List;

public class Word {
    private static Path path = Paths.get("files","words.txt");
    
    
    public static String getRandomWord(){
        try{
            List<String> words = Files.readAllLines(path);
            return words.get((int)(Math.random() * words.size()));
        }catch(IOException e){
            return null;
        }
    }
    
    public static void main(String[] args) {
        System.out.println(getRandomWord());
    }
}
