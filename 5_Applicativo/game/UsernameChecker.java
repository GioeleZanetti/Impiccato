/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.nio.file.*;
import java.io.*;
import java.util.List;

/**
 *
 * @author gioele.zanetti
 */
public class UsernameChecker {
    
    public static boolean isUsernameValid(String username) 
    throws IllegalArgumentException{
        try{
            List<String> badWords = Files.readAllLines(Paths.get("files","badwords.txt"));
            if(containsBadWords(badWords, username.replaceAll(" ", ""))){
                throw new IllegalArgumentException("Name contains words that are not allowed");
            }else if(username.length() > 20){
                throw new IllegalArgumentException("Name must contain a maximum of 20 characters");
            }else{
                return true;
            }
        }catch(IOException ioe){
            return false;
        }
    }
    
    private static boolean containsBadWords(List<String> words, String word){
        for(int i=0;i<words.size();i++){
            if(word.toLowerCase().contains(words.get(i))){
                return true;
            }
        }
        return false;
    }
    
}
