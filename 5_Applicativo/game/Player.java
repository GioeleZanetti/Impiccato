/**
 * @version 14 set 2021
 * @author Gioele Zanetti
 */

package game;

public class Player {

    private String name;
    private int points;
    private boolean hasFinished;
    private int errors;
    
    public Player(String name, int points){
        this.name = name;
        this.points = points;
        this.hasFinished = false;
        this.errors = 0;
    }
    
    public Player(String name){
        this(name, 0);
    }
    
    public String getName(){
        return name;
    }
    
    public int getPoints(){
        return points;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setPoints(int points){
        if(points > 0){
            this.points = points;
        }
    }
    
    public boolean hasFinished(){
        return hasFinished;
    }
    
    public void setHasFinished(boolean state){
        this.hasFinished = state;
    }
    
    public void addError(){
        this.errors += 1;
    }
    
    public void addPoints(int points){
        if(this.errors != 10){
            this.points += points;
        }
    }
    
    public int getErrors(){
        return this.errors;
    }
    
    public void setErrorsToZero(){
        this.errors = 0;
    }
    
    public String toString(){
        return name + " : " + points;
    }
}
