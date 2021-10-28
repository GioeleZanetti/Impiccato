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
    private boolean hasFinished;
    private int errors;
    private int currentTurn;

    public Client(App a) throws IOException {
        server = new Socket(SERVER_IP, SERVER_PORT);
        out = new DataOutputStream(server.getOutputStream());
        this.a = a;
        this.userName = "";
        this.gameName = null;
        this.hasStarted = false;
        this.hasFinished = false;
        this.word = null;
        this.errors = 0;
        this.currentTurn = 1;
        serverConnection = new ServerHandler(server, this);
        new Thread(serverConnection).start();
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void write(byte[] msg) throws IOException {
        out.write(msg);
    }

    private void setHasStarted(boolean b) {
        this.hasStarted = b;
    }

    private void setWord(String string) {
        this.word = string;
    }

    public void setMaskedWord() {
        StringBuilder mw = new StringBuilder();
        if (word != null) {
            for (int i = 0; i < word.length(); i++) {
                mw.append("*");
            }
            maskedWord = mw.toString();
        }
    }

    public void revealIndexes(byte[] indexes, String letter) {
        StringBuilder nmw = new StringBuilder(this.maskedWord);
        for (int i = 0; i < indexes.length; i++) {
            nmw.replace(indexes[i], indexes[i] + 1, letter);
        }
        this.maskedWord = nmw.toString();
    }

    public void elaborateRequest(String msg) throws IOException {
        if (!hasStarted) {
            if (msg.toLowerCase().equals("create game")) {
                createGame(a.getKeyboard());
            } else if (msg.toLowerCase().contains("join game")) {
                joinGame(msg);
            } else if (msg.toLowerCase().equals("get players")) {
                getPlayers();
            } else if (msg.toLowerCase().equals("leave game")) {
                leaveGame();
            } else if (msg.toLowerCase().equals("start game")) {
                startGame();
            } else if (msg.toLowerCase().contains("change username")) {
                changeUsername(a.getKeyboard());
            }
        } else if (this.errors < 10 && this.maskedWord.contains("*")) {
            sendLetter(msg);
        } else {
            a.writeOnConsole("You have to wait until the end of the turn to play again!");
        }
    }

    private void sendLetter(String msg) throws IOException {
        if (!msg.trim().isEmpty()) {
            write(ProtocolCodes.buildSendLetterPacket(gameName, msg.charAt(0)));
        }
    }

    public void changeUsername(BufferedReader k) {
        boolean done = false;
        while (!done) {
            try {
                System.out.print("Type your new name: ");
                String newUserName = k.readLine();
                if (UsernameChecker.isUsernameValid(newUserName)) {
                    done = true;
                    this.userName = newUserName;
                    System.out.printf("Username changed to %s!\n", userName);
                }
            } catch (IllegalArgumentException iae) {
                System.out.println(iae.getMessage());
            } catch (IOException ioe) {
                this.userName = "NoKeyboardUser";
                done = true;
            }
        }

    }

    private void startGame() throws IOException {
        if (gameName != null) {
            write(ProtocolCodes.buildStartGamePacket(gameName, userName));
        } else {
            System.out.println("You are not in a game!");
        }
    }

    private void leaveGame() throws IOException {
        if (gameName != null) {
            write(ProtocolCodes.buildLeaveGamePacket(gameName, userName));
        } else {
            System.out.println("You have to be in a game to leave the game!");
        }
    }

    private void getPlayers() throws IOException {
        if (gameName != null) {
            write(ProtocolCodes.buildGetPlayerListPacket(gameName));
        } else {
            System.out.println("You have to be in a game to get the player's list!");
        }
    }

    private void joinGame(String msg) throws IOException {
        if (gameName == null) {
            String gameToken = msg.substring("join game".length() + 1);
            write(ProtocolCodes.buildJoinGamePacket(gameToken, userName));
        } else {
            System.out.println("You are already in a game!");
        }
    }

    private void createGame(BufferedReader k) throws IOException {
        if (gameName == null) {
            int turns = getParameter(k, "turn");
            int length = getParameter(k, "length");
            write(ProtocolCodes.buildCreateGamePacket(userName, turns, length));
        } else {
            System.out.println("You are already in a game!");
        }
    }
    
    private int getParameter(BufferedReader k, String parameterName){
        boolean inserted = false;
        a.writeOnConsole("Insert " + parameterName + " number:");
        while(!inserted){
            try{
                return Integer.parseInt(k.readLine());
            }catch(NumberFormatException nfe){
                a.writeOnConsole("Number not valid, try again");
            }catch(IOException ioe){
                return 1;
            }
        }
        return -1;
    }

    public void elaborateResponse(byte[] response) throws IOException {
        switch (response[0]) {
            case ProtocolCodes.GAME_CREATED_SUCCESSFULLY:
                gameCreatedSuccessfully(response);
                break;
            case ProtocolCodes.GAME_JOINED_SUCCESSFULLY:
                gameJoinedSuccessfully(response);
                break;
            case ProtocolCodes.GAME_JOINED_UNSUCCESSFULLY:
                gameJoinedUnsuccessfully();
                break;
            case ProtocolCodes.USERNAME_ALREADY_USED:
                usernameAlreadyUsed();
                break;
            case ProtocolCodes.PLAYER_LIST_RETURNED:
                playerListReturned(response);
                break;
            case ProtocolCodes.GAME_LEAVED_SUCCESSFULLY:
                gameLeavedSuccessfully();
                break;
            case ProtocolCodes.GAME_LEAVED_UNSUCCESSFULLY:
                gameLeavedUnsuccessfully();
                break;
            case ProtocolCodes.GAME_STARTED_SUCCESSFULLY:
                gameStartedSuccessfully();
                break;
            case ProtocolCodes.GAME_STARTED_UNSUCCESSFULLY:
                gameStartedUnsuccessfully();
                break;
            case ProtocolCodes.LETTER_INDEXES:
                letterIndexes(response);
                break;
            case ProtocolCodes.RETURN_GAME_WORD:
                returnGameWord(response);
                break;
            case ProtocolCodes.NOTIFY_TURN_WON:
            case ProtocolCodes.NOTIFY_TURN_LOST:
                notifyTurnWonOrLost(response);
                break;
            case ProtocolCodes.END_GAME:
                endGame();
                break;
            case ProtocolCodes.END_TURN:
                endTurn();
                break;
            default:
                break;
        }
    }

    private void endTurn() throws IOException {
        this.errors = 0;
        this.currentTurn++;
        getPlayers();
        printWord();
        a.writeOnConsole("--------------------------------------------------------");
        write(ProtocolCodes.buildRequestGameWordPacket(this.gameName));
    }

    private void endGame() throws IOException {
        a.writeOnConsole("Every player has finished the game!");
        this.hasFinished = true;
        getPlayers();
        printWord();
        write(ProtocolCodes.buildDeleteGamePacket(gameName));
    }

    private void notifyTurnWonOrLost(byte[] response) {
        a.writeOnConsole(new String(ProtocolCodes.getDataFromPacket(response)));
    }

    private void returnGameWord(byte[] response) {
        setWord(new String(ProtocolCodes.getDataFromPacket(response)));
        setMaskedWord();
        if (hasStarted) {
            printGameInfo();
        }
    }

    private void letterIndexes(byte[] response) throws IOException {
        byte[] indexes = ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(response), 1, response.length - 1);
        if (indexes.length > 0) {
            byte[] letter = ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(response), 0, 1);
            revealIndexes(indexes, new String(letter));
        } else {
            this.errors++;
            write(ProtocolCodes.buildAddErrorPacket(this.gameName, this.userName));
            if (this.errors == 10) {
                write(ProtocolCodes.buildPlayerLostPacket(gameName, userName));
            }
        }
        printGameInfo();
        if (!maskedWord.contains("*")) {
            write(ProtocolCodes.buildPlayerWonPacket(gameName, userName));
        }
    }

    private void gameStartedUnsuccessfully() {
        a.writeOnConsole("Couldn't start game, you are not the admin");
    }

    private void gameStartedSuccessfully() {
        a.writeOnConsole("Game started by the admin!");
        setHasStarted(true);
        printGameInfo();
    }

    private void gameLeavedUnsuccessfully() {
        a.writeOnConsole("Error while leaving game");
    }

    private void gameLeavedSuccessfully() {
        setGameName(null);
        a.writeOnConsole("Game leaved!");
    }

    private void playerListReturned(byte[] response) {
        byte[] players = ProtocolCodes.getDataFromPacket(response);
        a.writeOnConsole(new String(players));
        if (hasFinished) {
            setHasStarted(false);
            System.exit(0);
        }
    }

    private void usernameAlreadyUsed() {
        a.writeOnConsole("Your username is already used in the game that you are trying to join in, please change it!");
    }

    private void gameJoinedUnsuccessfully() {
        a.writeOnConsole("Game unavailable, check token again");
    }

    private void gameJoinedSuccessfully(byte[] response) throws IOException {
        a.writeOnConsole("Game joined!");
        String gameToken = new String(ProtocolCodes.getDataFromPacket(response));
        setGameName(gameToken);
        write(ProtocolCodes.buildRequestGameWordPacket(this.gameName));
    }

    private void gameCreatedSuccessfully(byte[] response) throws IOException {
        String gameToken = new String(ProtocolCodes.getDataFromPacket(response));
        setGameName(gameToken);
        write(ProtocolCodes.buildRequestGameWordPacket(this.gameName));
        a.writeOnConsole("Game " + gameName + " created!");
    }

    private void printGameInfo() {
        a.writeOnConsole("Current turn: " + this.currentTurn);
        a.writeOnConsole("Current word: " + this.maskedWord);
        a.writeOnConsole("Errors: " + this.errors + "/10");
    }


    private void printWord() {
        a.writeOnConsole("The word was " + this.word + "!");
    }

}
