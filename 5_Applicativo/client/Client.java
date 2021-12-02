/**Rappresenta il client per giocare all'impiccato
 * @version 7 set 2021
 * @author Gioele Zanetti
 */
package client;

import application.App;
import game.GameHoster;
import game.UsernameChecker;
import graphic.MainFrame;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.invoke.VarHandle;
import java.net.Socket;
import protocol.ProtocolCodes;

public class Client {

    /**
     * Ip del server
     */
    private static final String SERVER_IP = "127.0.0.1";
    
    /**
     * porta in cui il server ascolta
     */
    private static final int SERVER_PORT = 3000;

    /**
     * socket per comunicare con il server
     */
    private Socket server;
    
    /**
     * applicazione da terminale
     */
    private App a;
    
    /**
     * applicatione grafica
     */
    private MainFrame frame;
    
    /**
     * Output per comunicare con il server
     */
    private DataOutputStream out;
    
    /**
     * Oggetto che gestisce l'ascolto dei messaggi dal server
     */
    private ServerHandler serverConnection;
    
    /**
     * L'username scelto dal giocatore
     */
    private String userName;
    
    /**
     * Il token della partita corrente
     */
    private String gameName;
    
    /**
     * La parola senza censure
     */
    private String word;
    
    /**
     * La parola composta da asterischi e le lettere
     * indovinate dal giocatore
     */
    private String maskedWord;
    
    /**
     * Indica se la partita è cominciata
     */
    private boolean hasStarted;
    
    /**
     * Indica se la partita è finita
     */
    private boolean hasFinished;
    
    /**
     * Indica quanti errori ha compiuto il giocatore in
     * questo turno
     */
    private int errors;
    
    /**
     * Indica il numero del truno corrente
     * (1, 2, 3, ...)
     */
    private int currentTurn;
    
    /**
     * Indica se viene utilizzata la GUI o la CLI
     */
    private boolean isGraphic;
    
    /**
     * La lunghezza in secondi del turno
     */
    private int lengthInSeconds;

    /**
     * Costruttore per un applicazione da terminale
     * @param a l'applicazione da terminale
     * @throws IOException eccezione sollevata se socket non disponibile
     */
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
        this.isGraphic = false;
        this.lengthInSeconds = 0;
        serverConnection = new ServerHandler(server, this);
        new Thread(serverConnection).start();
    }
    
    /**
     * Costruttore per un applicazione GUI
     * @param frame il frame che contiene la GUI
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    public Client(MainFrame frame) throws IOException {
        server = new Socket(SERVER_IP, SERVER_PORT);
        out = new DataOutputStream(server.getOutputStream());
        this.frame = frame;
        this.userName = "";
        this.gameName = null;
        this.hasStarted = false;
        this.hasFinished = false;
        this.word = null;
        this.errors = 0;
        this.currentTurn = 1;
        this.isGraphic = true;
        this.lengthInSeconds = 0;
        serverConnection = new ServerHandler(server, this);
        new Thread(serverConnection).start();
    }
    
    /**
     * Torna la parola censurata
     * @return parola censurata
     */
    public String getMaskedWord(){
        return this.maskedWord;
    }
    
    /**
     * Imposta l'username del player
     * @param username nome scelto dal player
     */
    public void setUsername(String username){
        this.userName = username;
    }
    
    /**
     * Ritorna il nome utente
     * @return il nome utente
     */
    public String getUsername(){
        return userName;
    }

    /**
     * Imposta il token della partita
     * @param gameName token della partita
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    
    /**
     * Ritorna il token della partita
     * @return il token della partita
     */
    public String getGameToken(){
        return gameName;
    }
    
    /**
     * Ritorna la lunghezza di un turno in secondi
     * @return la lunghezza di un turno in secondi
     */
    public int getLengthInSeconds(){
        return this.lengthInSeconds;
    }

    /**
     * Permette di mandare al server dei messaggi
     * @param msg il pacchetto da mandare
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    public void write(byte[] msg) throws IOException {
        out.write(msg);
    }

    /**
     * Imposta lo stato della partita
     * @param b lo stato della partita
     */
    private void setHasStarted(boolean b) {
        this.hasStarted = b;
    }

    /**
     * Imposta la parola senza censure
     * @param string la parola senza censure
     */
    private void setWord(String string) {
        this.word = string;
    }

    /**
     * Imposta la parola censurata in base alla
     * parola senza censure
     */
    public void setMaskedWord() {
        StringBuilder mw = new StringBuilder();
        if (word != null) {
            for (int i = 0; i < word.length(); i++) {
                mw.append("*");
            }
            maskedWord = mw.toString();
        }
    }

    /**
     * Rivela le lettere che sono state indovinate dall'utente
     * @param indexes gli indici delle lettere indovinate
     * @param letter la lettera indovinata
     */
    public void revealIndexes(byte[] indexes, String letter) {
        StringBuilder nmw = new StringBuilder(this.maskedWord);
        for (int i = 0; i < indexes.length; i++) {
            nmw.replace(indexes[i], indexes[i] + 1, letter);
        }
        this.maskedWord = nmw.toString();
    }

    /**
     * Metodo che permette di gestire le richieste del client
     * @param msg la richiesta
     * @param parameters eventuali parametri
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    public void elaborateRequest(String msg, Object[] parameters) throws IOException {
        if (!hasStarted) {
            if (msg.toLowerCase().contains("create game")) {
                if(!isGraphic){
                    createGame(a.getKeyboard());
                }else{
                    createGameGraphic(parameters);
                }
            } else if (msg.toLowerCase().contains("join game")) {
                joinGame(msg);
            } else if (msg.toLowerCase().equals("get players")) {
                getPlayers(false);
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
            if(!isGraphic){
                a.writeOnConsole("You have to wait until the end of the turn to play again!");
            }else{
                this.frame.getCurrentPanel().setData("You have to wait until the end of the turn to play again!", "error");
            }
        }
    }
    
    /**
     * metodo che permette di gestire le richieste del client
     * @param msg la richiesta del client
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    public void elaborateRequest(String msg) throws IOException {
        elaborateRequest(msg, new Object[0]);
    }

    /**
     * Metodo per inviare le lettere che l'utente digita
     * al server
     * @param msg quello che l'utente ha scritto
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    private void sendLetter(String msg) throws IOException {
        if (!msg.trim().isEmpty()) {
            write(ProtocolCodes.buildSendLetterPacket(gameName, msg.charAt(0)));
        }
    }

    /**
     * Metodo per cambiare il nome utente da un applicazione da terminale
     * @param k l'input della tastiera
     */
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

    /**
     * Metodo per cominciare una partita
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    private void startGame() throws IOException {
        if (gameName != null) {
            write(ProtocolCodes.buildStartGamePacket(gameName, userName));
        } else {
            System.out.println("You are not in a game!");
        }
    }

    /**
     * Server per lasciare una partita
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    private void leaveGame() throws IOException {
        if (gameName != null) {
            write(ProtocolCodes.buildLeaveGamePacket(gameName, userName));
        } else {
            System.out.println("You have to be in a game to leave the game!");
        }
    }

    /**
     * Richiede al server la lista dei giocatori con il loro punteggio
     * @param leaderboard se la lista deve essere ordinata per punteggio
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    private void getPlayers(boolean leaderboard) throws IOException {
        if (gameName != null) {
            if(!leaderboard){
                write(ProtocolCodes.buildGetPlayerListPacket(gameName));
            }else{
                write(ProtocolCodes.buildGetLeaderboardPacket(gameName));
            }
        } else {
            System.out.println("You have to be in a game to get the player's list!");
        }
    }

    /**
     * serve per entrare in una partita
     * @param msg il messaggio inviato
     * @throws IOException  eccezione sollevata se socket non disponibile
     */
    private void joinGame(String msg) throws IOException {
        if (gameName == null) {
            String gameToken = msg.substring("join game".length() + 1);
            write(ProtocolCodes.buildJoinGamePacket(gameToken, userName));
        } else {
            System.out.println("You are already in a game!");
        }
    }

    /**
     * Metodo per creare una partita da terminale
     * @param k l'input della tastiera
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    private void createGame(BufferedReader k) throws IOException {
        if (gameName == null) {
            int turns = getParameter(k, "turn");
            int length = getParameter(k, "length");
            write(ProtocolCodes.buildCreateGamePacket(userName, turns, length));
        } else {
            System.out.println("You are already in a game!");
        }
    }
    
    /**
     * Metodo per creare una partita da GUI
     * @param parameters la lughezza e i turni della partita
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    private void createGameGraphic(Object[] parameters) throws IOException {
        if (gameName == null) {
            int length = (int)parameters[0];
            int turns = (int)parameters[1];
            write(ProtocolCodes.buildCreateGamePacket(userName, turns, length));
        } else {
            System.out.println("You are already in a game!");
        }
    }
    
    /**
     * Metodo per ritornare dei parametri da terminale
     * @param k l'input della tastiera
     * @param parameterName il nome del parametro che verrà 
     * stampato sulla console
     * @return 
     */
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

    /**
     * Metodo che interpreta le risposte del server
     * @param response la risposta del server
     * @throws IOException eccezione sollevata se socket non disponibile
     */
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
            case ProtocolCodes.PLAYER_JOINED_GAME:
                playerJoinedGame(response);
                break;
            case ProtocolCodes.PLAYER_LEFT_GAME:
                playerLeftGame(response);
                break;
            case ProtocolCodes.ADMIN_LEFT_GAME:
                adminLeftGame();
                break;
            default:
                break;
        }
    }

    
    /**
     * Metodo che termina il turno
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    private void endTurn() throws IOException {
        this.errors = 0;
        this.currentTurn++;
        getPlayers(false);
        printWord();
        if(!isGraphic){
            a.writeOnConsole("--------------------------------------------------------");
        }
        write(ProtocolCodes.buildRequestGameWordPacket(this.gameName));
    }

    /**
     * Metodo che termina la parita
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    private void endGame() throws IOException {
        printWord();
        this.hasFinished = true;
        getPlayers(true);
        if(!isGraphic){
            a.writeOnConsole("Every player has finished the game!");
        }else{
            this.frame.getCurrentPanel().setData(true, "end game");
        }
        write(ProtocolCodes.buildDeleteGamePacket(gameName));
    }

    /**
     * Metodo che serve per notificare i giocatori
     * qunado un giocatore vince o perde una partita
     * @param response la risposta del server
     */
    private void notifyTurnWonOrLost(byte[] response) {
        if(!isGraphic){
            a.writeOnConsole(new String(ProtocolCodes.getDataFromPacket(response)));
        }
    }

    /**
     * Metodo che imposta la parola senza e con censure
     * @param response la risposta del server
     */
    private void returnGameWord(byte[] response) {
        setWord(new String(ProtocolCodes.getDataFromPacket(response)));
        setMaskedWord();
        if (hasStarted) {
            if(!isGraphic){
                printGameInfo();
            }else{
                this.frame.getCurrentPanel().setData(this.maskedWord, "masked word");
            }
        }
    }

    /**
     * Metodo che gestisce gli errori e le lettere indovinate
     * @param response la risposta del server con gli indici
     * delle lettere indovinate, se vuoto la lettera è sbagliata
     * @throws IOException eccezione sollevata se socket non disponibile
     */
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
        if(!isGraphic){
            printGameInfo();
        }else{
            this.frame.getCurrentPanel().setData(this.maskedWord, "masked word");
        }
        
        if (!maskedWord.contains("*")) {
            write(ProtocolCodes.buildPlayerWonPacket(gameName, userName));
        }
    }

    /**
     * Metodo per notificare che la partita non può iniziare
     * perché chi sta cercando di avviarla non è l'admin
     */
    private void gameStartedUnsuccessfully() {
        if(!isGraphic){
            a.writeOnConsole("Couldn't start game, you are not the admin");
        }
    }

    /**
     * Metodo per far cominciare la partita
     */
    private void gameStartedSuccessfully() {
        setHasStarted(true);
        if(!isGraphic){
            a.writeOnConsole("Game started by the admin!");
            printGameInfo();
        }else{
            this.frame.getCurrentPanel().setData(true, "start game");
        }
        
    }

    /**
     * Metodo per notificare che c'è stato un errore
     * mentre si cercava di uscire dalla partita
     */
    private void gameLeavedUnsuccessfully() {
        if(!isGraphic){
            a.writeOnConsole("Error while leaving game");
        }
    }

    /**
     * Metodo per uscire dalla parita
     */
    private void gameLeavedSuccessfully() {
        setGameName(null);
        if(!isGraphic){
            a.writeOnConsole("Game leaved!");
        }else{
            this.frame.getCurrentPanel().setData(true, "leave game");
        }
    }

    /**
     * Metodo che gestisce quando il server ritorna la 
     * lista dei giocatori
     * @param response la lista dei giocatori
     */
    private void playerListReturned(byte[] response) {
        byte[] players = ProtocolCodes.getDataFromPacket(response);
        if(!isGraphic){
            a.writeOnConsole(new String(players));
        }else{
            this.frame.getCurrentPanel().setData(new String(players), "playerList");
        }
        
        if (hasFinished) {
            setHasStarted(false);
            //System.exit(0);
        }
    }

    /**
     * Metodo che notifica il giocatore che nella partita 
     * in cui vuole entrare c'è già qualcuno con lo stesso nome
     */
    private void usernameAlreadyUsed() {
        if(!isGraphic){
            a.writeOnConsole("Your username is already used in the game that you are trying to join in, please change it!");
        }
    }

    /**
     * Metodo che notifica al giocatore che il token
     * inserito non è valido
     */
    private void gameJoinedUnsuccessfully() {
        if(!isGraphic){
            a.writeOnConsole("Game unavailable, check token again");
        }
    }

    /**
     * Metodo che serve per notificare che ci si
     * è uniti correttamente ad una partita
     * @param response
     * @throws IOException 
     */
    private void gameJoinedSuccessfully(byte[] response) throws IOException {
        String gameToken = new String(ProtocolCodes.getDataFromPacket(response));
        setGameName(gameToken);
        if(!isGraphic){
            a.writeOnConsole("Game joined!");
        }else{
            this.frame.getCurrentPanel().setData(gameToken, "join game");
        }
        write(ProtocolCodes.buildRequestGameWordPacket(this.gameName));
    }
    
    /**
     * Metodo per notificare che un giocatore si è unito alla partita
     * @param response il giocatore che si è unito
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    private void playerJoinedGame(byte[] response) throws IOException {
        String username = new String(ProtocolCodes.getDataFromPacket(response));
        if(!isGraphic){
            a.writeOnConsole("Player " + username + " joined the game!");
        }else{
            write(ProtocolCodes.buildGetPlayerListPacket(this.gameName));
        }
    }
    
    /**
     * Metodo per notificare che un giocatore è uscito dalla partita
     * @param response il giocatore che è uscito
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    private void playerLeftGame(byte[] response) throws IOException {

        String username = new String(ProtocolCodes.getDataFromPacket(response));
        if(!isGraphic){
            a.writeOnConsole("Player " + username + " left the game!");
        }else{
            if(this.gameName != null && !this.hasFinished){
                write(ProtocolCodes.buildGetPlayerListPacket(this.gameName));
            }
        }
    }
    
    /**
     * Metodo per notificare che l'admin è uscito dalla partita
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    private void adminLeftGame() throws IOException{
        
        hasFinished = true;
        if(!isGraphic){
            a.writeOnConsole("Admin left the game, please leave the game!");
        }else{
            frame.getCurrentPanel().setData(true, "adminLeft");
        }
    }

    /**
     * Metodo che notifica che la partita è stata creata
     * correttamente
     * @param response la risposta del server
     * @throws IOException eccezione sollevata se socket non disponibile
     */
    private void gameCreatedSuccessfully(byte[] response) throws IOException {
        String gameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(response), 0, response.length - 2));
        this.lengthInSeconds = ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(response), response.length - 2, response.length - 1)[0];
        setGameName(gameToken);
        write(ProtocolCodes.buildRequestGameWordPacket(this.gameName));
        if(!isGraphic){
            a.writeOnConsole("Game " + gameName + " created!");
        }else{
            this.frame.getCurrentPanel().setData(gameToken, "gameToken");
        }
    }

    /**
     * Metodo per stampare le info della partita in un 
     * app da terminale
     */
    private void printGameInfo() {
        a.writeOnConsole("Current turn: " + this.currentTurn);
        a.writeOnConsole("Current word: " + this.maskedWord);
        a.writeOnConsole("Errors: " + this.errors + "/10");
    }

    /**
     * Metodo per stampare la parola alla fine del turno
     * in un applicazione da terminale
     */
    private void printWord() {
        if(!isGraphic){
            a.writeOnConsole("The word was " + this.word + "!");
        }else{
            frame.getCurrentPanel().setData(this.word, "word");
        }
    }

}
