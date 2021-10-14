/**
 * @version 7 set 2021
 * @author Gioele Zanetti
 */
package client;

import application.App;
import game.UsernameChecker;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import protocol.ProtocolCodes;

public class Client {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 3000;

    private Socket server;
    private App a;
    private DataOutputStream out;
    private ServerHandler serverConnection;
    private String userName;
    private String gameName;
    private String word;
    private String maskedWord;
    private boolean hasStarted;
    private int errors;

    public Client(App a, String userName) throws IOException {
        server = new Socket(SERVER_IP, SERVER_PORT);
        out = new DataOutputStream(server.getOutputStream());
        this.a = a;
        this.userName = userName;
        this.gameName = null;
        this.hasStarted = false;
        this.word = null;
        this.errors = 0;
        serverConnection = new ServerHandler(server, this);
        new Thread(serverConnection).start();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void write(byte[] msg) throws IOException {
        out.write(msg);
    }

    public void closeConnection() throws IOException {
        this.server.close();
    }

    private void setHasStarted(boolean b) {
        this.hasStarted = b;
    }
    
    private void setWord(String string) {
        this.word = string;
    }


    public void elaborateRequest(String msg) throws IOException {
        
    }

    public void changeUsername(BufferedReader k) {
        boolean done = false;
        while(!done){
            try{
                System.out.print("Type your new name: ");
                String newUserName = k.readLine();
                if(UsernameChecker.isUsernameValid(newUserName)){
                    done = true;
                    this.userName = newUserName;
                    System.out.printf("Username changed to %s!\n", userName);
                }
            }catch(IllegalArgumentException iae){
                System.out.println(iae.getMessage());
            }catch(IOException ioe){
                this.userName = "NoKeyboardUser";
                done = true;
            }
        }
        
    }


    public void elaborateResponse(byte[] response) throws IOException {
        
    }
    
    private void printGameInfo(){
        a.writeOnConsole("Current word: " + this.maskedWord);
        a.writeOnConsole("Errors: " + this.errors + "/10");
    }
    
    private byte[] readFromTo(byte[] packet, int start, int end){
        byte[] data = new byte[end - start];
        for(int i=0;i<data.length;i++){
            data[i] = packet[start + i];
        }
        return data;
    }

}
