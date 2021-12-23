/**
 * @version 7 set 2021
 * @author Gioele Zanetti
 */
package server;

import game.GameHoster;
import game.Player;
import game.UsernameChecker;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import protocol.ProtocolCodes;
import java.awt.event.*;
import javax.swing.*;

public class ClientHandler implements Runnable {

    /**
     * Il socket con il client
     */
    private Socket client;
    
    /**
     * Lo stream di output con il client
     */
    private DataOutputStream out;
    
    /**
     * Lo stream in input con il client
     */
    private DataInputStream in;
    
    /**
     * La lista con tutti i client connessi
     */
    private List<ClientHandler> clients;
    
    /**
     * Il token della partita attuale
     */
    private String gameToken;
    
    /**
     * L'username del giocatore
     */
    private String username;
    
    /**
     * Il timer che regola la durata dei turni
     */
    private Timer timer;

    /**
     * Crea un nuovo oggetto ClientHandler
     * @param client il socket con il client
     * @param clients la lista di tutti i client
     * @throws IOException eccezione generata se la comunicazione con il client
     * non va a buon fine
     */
    public ClientHandler(Socket client, List<ClientHandler> clients) throws IOException {
        this.client = client;
        this.clients = clients;
        gameToken = "";
        username = "";
        this.timer = new Timer(0, action);

        out = new DataOutputStream(client.getOutputStream());
        in = new DataInputStream(client.getInputStream());

    }
    
    /**
     * Imposta tutti i client disponibili
     * @param clients la lista di client
     */
    public void setClients(List<ClientHandler> clients){
        this.clients = clients;
    }

    /**
     * Metodo per impostare il token della partita
     * @param gameToken il token della partita
     */
    private void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }

    /**
     * Metodo per ritornare il token della partita
     * @return il token della partita
     */
    private String getGameToken() {
        return gameToken;
    }

    private void setUsername(String username){
        this.username = username;
    }
    
    @Override
    public void run() {
        try {
            int current;
            //leggo quanti byte devo leggere
            while ((current = in.read()) != -1) {
                byte[] packet = new byte[current];
                //leggo il numero di byte specificato
                in.read(packet, 0, current);
                //elaboro la richiesta
                elaborateRequest(packet);
                //controllo se la thread è stata fermata
                if(Thread.interrupted()){
                    return;
                }
            }
        } catch (IOException ex) {
            try {
                in.close();
                clients.remove(this);
                this.clients = Server.getClients();
                var game = GameHoster.getGame(gameToken);
                if(game != null){
                    if(game.getAdmin().equals(username)){
                        game.removePlayer(username);
                        broadcastToGame(gameToken, ProtocolCodes.buildAdminLeftGamePacket());
                        GameHoster.removeGame(gameToken);
                        System.out.println("Removing " + gameToken);
                    }else{
                        game.removePlayer(username);
                        broadcastToGame(gameToken, ProtocolCodes.buildPlayerLeftGamePacket(username));
                    }
                }
            } catch (IOException ioe) {

            }
        }
    }

    /**
     * Metodo che gestisce le richieste dal client
     * @param request la richiesta del client
     * @throws IOException eccezione generata se la comunicazione con il client
     * non va a buon fine
     */
    private void elaborateRequest(byte[] request) throws IOException {
        switch (request[0]) {
            case ProtocolCodes.CREATE_GAME:
                createGame(request);
                break;
            case ProtocolCodes.JOIN_GAME:
                joinGame(request);
                break;
            case ProtocolCodes.GET_PLAYER_LIST:
                getPlayerList(request);
                break;
            case ProtocolCodes.LEAVE_GAME:
                leaveGame(request);
                break;
            case ProtocolCodes.START_GAME:
                startGame(request);
                break;
            case ProtocolCodes.SEND_LETTER:
                sendLetter(request);
                break;
            case ProtocolCodes.REQUEST_GAME_WORD:
                requestGameWord(request);
                break;
            case ProtocolCodes.PLAYER_WON_TURN:
            case ProtocolCodes.PLAYER_LOST_TURN:
                playerLostOrWonTurn(request);
                break;
            case ProtocolCodes.ADD_ERROR:
                addError(request);
                break;
            case ProtocolCodes.DELETE_GAME:
                deleteGame(request);
                break;
            case ProtocolCodes.FORCE_TURN_END:
                forceTurnEnd(request);
                break;
            case ProtocolCodes.GET_LEADERBOARD:
                getLeaderboard(request);
                break;
            default:
                System.out.println("Non conosco la richiesta " + request[0]);
                break;
        }
    }

    /**
     * Ferma il timer per poi farlo ripartire
     * @param length il tempo da aspettare
     */
    private void setTimer(int length) {
        timer.stop();
        this.timer = new Timer(length * 1000, action);
        this.timer.setRepeats(false);
        this.timer.start();
    }

    /**
     * L'azione da compiere quando il timer finisce
     */
    ActionListener action = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            try {
                byte[] packet = ProtocolCodes.removeLengthFromPacket(
                    ProtocolCodes.buildForceTurnEndPacket(getGameToken())
                );
                elaborateRequest(packet);
            } catch (IOException ioe) {

            }
        }
    };
    
    /**
     * Metodo utile a ottenere la classifica di
     * una partita
     * @param request la richiesta del client
     * @throws IOException 
     */
    private void getLeaderboard(byte[] request) throws IOException{
        String gameName = new String(ProtocolCodes.getDataFromPacket(request));
        out.write(ProtocolCodes.buildPlayerListReturnedPacket(GameHoster.getLeaderboard(gameName)));
    }

    /**
     * Metodo per eliminare una partita
     * @param request la richiesta del client
     */
    private void deleteGame(byte[] request) {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            GameHoster.removeGame(requestGameToken);
            System.out.println("Removing " + requestGameToken);
        }
    }

    /**
     * Metodo per aggiungere un errore al counter di un giocatore
     * @param request la richiesta del client
     */
    private void addError(byte[] request) {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        String playerName = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            game.getPlayer(playerName).addError();
        }
    }

    /**
     * Metodo per indicare se un giocatore ha vinto o
     * perso un turno
     * @param request la richiesta del client
     * @throws IOException eccezione generata se la comunicazione con il client
     * non va a buon fine
     */
    private void playerLostOrWonTurn(byte[] request) throws IOException {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        String playerName = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            if (game.getPlayer(playerName) != null) {
                game.getPlayer(playerName).setHasFinished(true);
                game.addPointsToPlayer(playerName);
                if (request[0] == ProtocolCodes.PLAYER_WON_TURN) {
                    broadcastToGame(requestGameToken, ProtocolCodes.buildNotifyPlayerWonTurnPacket(playerName));
                } else {
                    broadcastToGame(requestGameToken, ProtocolCodes.buildNotifyPlayerLostTurnPacket(playerName));
                }
            }
            if (game.turnEnd()) {
                game.nextTurn();
                setTimer(GameHoster.getGame(requestGameToken).getLengthInSeconds());
                broadcastToGame(requestGameToken, ProtocolCodes.buildEndTurnPacket());
            } else if (game.gameEnd()) {
                broadcastToGame(requestGameToken, ProtocolCodes.buildEndGamePacket());
            }
        }
    }

    /**
     * Metodo per forzare la fine del turno
     * @param request la richiesta del client
     * @throws IOException eccezione generata se la comunicazione con il client
     * non va a buon fine
     */
    private void forceTurnEnd(byte[] request) throws IOException {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            game.setPlayerHasFinished(true);
            if (game.turnEnd()) {
                game.nextTurn();
                setTimer(GameHoster.getGame(requestGameToken).getLengthInSeconds());
                broadcastToGame(requestGameToken, ProtocolCodes.buildEndTurnPacket());
            } else if (game.gameEnd()) {
                broadcastToGame(requestGameToken, ProtocolCodes.buildEndGamePacket());
            }
        }
    }

    /**
     * Metodo per ritornare la parola corrente
     * al client
     * @param request la richiesta del client
     * @throws IOException eccezione generata se la comunicazione con il client
     * non va a buon fine
     */
    private void requestGameWord(byte[] request) throws IOException {
        String requestGameToken = new String(ProtocolCodes.getDataFromPacket(request));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            out.write(ProtocolCodes.buildGameWordPacket(game.getWord()));
        }
    }

    /**
     * Metodo per controllare se la lettera inviata 
     * dal client è corretta
     * @param request la richiesta del client
     * @throws IOException eccezione generata se la comunicazione con il client
     * non va a buon fine
     */
    private void sendLetter(byte[] request) throws IOException {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            byte letter = ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 8, 9)[0];
            String username = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 9, request.length - 1));
            byte[] index = game.isLetterRight(letter);
            out.write(ProtocolCodes.buildLetterIndexesPacket(index, letter));
            broadcastToGame(requestGameToken, ProtocolCodes.buildLetterSentPacket(username, letter));
        }
    }

    /**
     * Metodo per far cominciare una partita
     * @param request la richiesta del client
     * @throws IOException eccezione generata se la comunicazione con il client
     * non va a buon fine
     */
    private void startGame(byte[] request) throws IOException {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        String userName = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1));
        if (GameHoster.getGame(requestGameToken).getAdmin().equals(userName)) {
            GameHoster.getGame(requestGameToken).startGame();
            setTimer(GameHoster.getGame(requestGameToken).getLengthInSeconds());
            broadcastToGame(requestGameToken, ProtocolCodes.buildGameStartedSuccessfullyPacket());
        } else {
            out.write(ProtocolCodes.buildGameStartedUnsuccessfullyPacket());
        }
    }

    /**
     * Metodo per lasciare una partita
     * @param request la richiesta del client
     * @throws IOException eccezione generata se la comunicazione con il client
     * non va a buon fine
     */
    private void leaveGame(byte[] request) throws IOException {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            String username = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1));
            if (!GameHoster.getGame(requestGameToken).getAdmin().equals(username)) {
                game.removePlayer(username);
                out.write(ProtocolCodes.buildGameLeavedSuccessfullyPacket());
                broadcastToGame(requestGameToken, ProtocolCodes.buildPlayerLeftGamePacket(username));
                if(game.getPlayerList().size() == 0){
                    deleteGame(ProtocolCodes.removeLengthFromPacket(ProtocolCodes.buildDeleteGamePacket(requestGameToken)));
                }
            }else{
                broadcastToGame(requestGameToken, ProtocolCodes.buildAdminLeftGamePacket());
                GameHoster.removeGame(requestGameToken);
                System.out.println("Removing " + requestGameToken);
            }
            System.out.println("Player " + username + " left game " + requestGameToken);
            
        } else {
            out.write(ProtocolCodes.buildGameLeavedUnsuccessfullyPacket());
        }
    }

    /**
     * Metodo per ritornare la lista dei giocatori
     * di una partita
     * @param requestla richiesta del client
     * @throws IOException eccezione generata se la comunicazione con il client
     * non va a buon fine
     */
    private void getPlayerList(byte[] request) throws IOException {
        String gameName = new String(ProtocolCodes.getDataFromPacket(request));
        out.write(ProtocolCodes.buildPlayerListReturnedPacket(GameHoster.getGameList(gameName)));
    }

    /**
     * Metodo per entrare in una partita
     * @param request la richiesta del client
     * @throws IOException eccezione generata se la comunicazione con il client
     * non va a buon fine
     */
    private void joinGame(byte[] request) throws IOException {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        String userName = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            if (!game.containsName(userName)) {
                if(!game.isFull()){
                    setGameToken(requestGameToken);
                    setUsername(userName);
                    game.addPlayer(userName);
                    System.out.printf("Player %s joined game %s\n", userName, requestGameToken);
                    out.write(ProtocolCodes.buildGameJoinedSuccessfullyPacket(requestGameToken, GameHoster.getGame(requestGameToken).getLengthInSeconds()));
                    broadcastToGame(requestGameToken, ProtocolCodes.buildPlayerJoinedGamePacket(userName));
                }else{
                    out.write(ProtocolCodes.buildGameFullPacket());
                }
            } else {
                out.write(ProtocolCodes.buildUsernameAlreadyUsedPacket());
            }
        } else {
            out.write(ProtocolCodes.buildGameJoinedUnsuccessfullyPacket());
        }
    }

    /**
     * Metodo per creare una partita
     * @param request la richiesta del client
     * @throws IOException eccezione generata se la comunicazione con il client
     * non va a buon fine
     */
    private void createGame(byte[] request) throws IOException {
        String newGameToken = GameHoster.generateRandomGameName();
        int turns = ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 1)[0];
        int length = ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 1, 2)[0];
        String userName = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 2, request.length - 1));
        GameHoster.addGame(newGameToken, turns, length);
        GameHoster.getGame(newGameToken).addPlayer(userName);
        GameHoster.getGame(newGameToken).setAdmin(userName);
        setGameToken(newGameToken);
        setUsername(userName);
        out.write(ProtocolCodes.buildGameCreatedSuccessfullyPacket(newGameToken, GameHoster.getGame(newGameToken).getLengthInSeconds()));
        System.out.printf("Creating %s, %d turns, %d length\n", newGameToken, turns, length);
    }

    /**
     * Metodo per mandare il broadcast ai client che fanno
     * parte della stessa partita un pacchetto
     * @param gameToken il token della partita
     * @param packet il pacchetto da inviare
     * @throws IOException eccezione generata se la comunicazione con il client
     * non va a buon fine
     */
    private void broadcastToGame(String gameToken, byte[] packet) throws IOException {
        for (ClientHandler c : clients) {
            try{
                if (c.getGameToken().equals(gameToken)) {
                    c.out.write(packet);
                }
            }catch(NullPointerException npe){}
        }
    }
}
