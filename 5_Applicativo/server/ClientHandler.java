/**
 * @version 7 set 2021
 * @author Gioele Zanetti
 */
package server;

import game.GameHoster;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import protocol.ProtocolCodes;
import java.awt.event.*;
import java.io.BufferedReader;
import javax.swing.*;

public class ClientHandler implements Runnable {

    private Socket client;
    private DataOutputStream out;
    private DataInputStream in;
    private List<ClientHandler> clients;
    private String gameToken;
    private Timer timer;

    public ClientHandler(Socket client, List<ClientHandler> clients) throws IOException {
        this.client = client;
        this.clients = clients;
        gameToken = "";
        this.timer = new Timer(0, action);

        out = new DataOutputStream(client.getOutputStream());
        in = new DataInputStream(client.getInputStream());

    }

    private void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }

    private String getGameToken() {
        return gameToken;
    }

    @Override
    public void run() {
        try {
            int current;
            while ((current = in.read()) != -1) {
                byte[] packet = new byte[current];
                in.read(packet, 0, current);

                elaborateRequest(packet);
            }
        } catch (IOException ex) {
            try {
                in.close();
            } catch (IOException ioe) {

            }
        }
    }

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
                System.out.println("Forzo fine turno");
                forceTurnEnd(request);
                break;
            default:
                System.out.println("Non conosco la richiesta " + request[0]);
                break;
        }
    }

    private void setTimer(int length) {
        timer.stop();
        this.timer = new Timer(length * 1000, action);
        this.timer.setRepeats(false);
        this.timer.start();
        System.out.println("Timer partito");
    }

    ActionListener action = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            try {
                byte[] packet = ProtocolCodes.removeLengthFromPacket(ProtocolCodes.buildForceTurnEndPacket(getGameToken()));
                System.out.println("Azione eseguita");
                elaborateRequest(packet);
            } catch (IOException ioe) {

            }
        }
    };

    private void deleteGame(byte[] request) {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            GameHoster.removeGame(requestGameToken);
            System.out.println("Removing " + requestGameToken);
        }
    }

    private void addError(byte[] request) {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        String playerName = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            game.getPlayer(playerName).addError();
        }
    }

    private void playerLostOrWonTurn(byte[] request) throws IOException {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        String playerName = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            if (game.getPlayer(playerName) != null) {
                game.getPlayer(playerName).setHasFinished(true);
                //timer.stop
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
            System.out.println("Fermo timer");

        }
    }

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

    private void requestGameWord(byte[] request) throws IOException {
        String requestGameToken = new String(ProtocolCodes.getDataFromPacket(request));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            out.write(ProtocolCodes.buildGameWordPacket(game.getWord()));
        }
    }

    private void sendLetter(byte[] request) throws IOException {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            byte letter = ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1)[0];
            byte[] index = game.isLetterRight(letter);
            out.write(ProtocolCodes.buildLetterIndexesPacket(index, letter));
        }
    }

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

    private void leaveGame(byte[] request) throws IOException {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            game.removePlayer(new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1)));
            out.write(ProtocolCodes.buildGameLeavedSuccessfullyPacket());
        } else {
            out.write(ProtocolCodes.buildGameLeavedUnsuccessfullyPacket());
        }
    }

    private void getPlayerList(byte[] request) throws IOException {
        String gameName = new String(ProtocolCodes.getDataFromPacket(request));
        out.write(ProtocolCodes.buildPlayerListReturnedPacket(GameHoster.getGameList(gameName)));
    }

    private void joinGame(byte[] request) throws IOException {
        String requestGameToken = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        String userName = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            if (!game.containsName(userName)) {
                setGameToken(requestGameToken);
                game.addPlayer(userName);
                out.write(ProtocolCodes.buildGameJoinedSuccessfullyPacket(requestGameToken));
            } else {
                out.write(ProtocolCodes.buildUsernameAlreadyUsedPacket());
            }
        } else {
            out.write(ProtocolCodes.buildGameJoinedUnsuccessfullyPacket());
        }
    }

    private void createGame(byte[] request) throws IOException {
        String requestGameToken = GameHoster.generateRandomGameName();
        int turns = ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 1)[0];
        int length = ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 1, 2)[0];
        String userName = new String(ProtocolCodes.readFromTo(ProtocolCodes.getDataFromPacket(request), 2, request.length - 1));
        GameHoster.addGame(requestGameToken, turns, length);
        GameHoster.getGame(requestGameToken).addPlayer(userName);
        GameHoster.getGame(requestGameToken).setAdmin(userName);
        setGameToken(requestGameToken);
        out.write(ProtocolCodes.buildGameCreatedSuccessfullyPacket(requestGameToken));
        System.out.println("Creating " + requestGameToken);
    }

    private void broadcastToGame(String gameToken, byte[] packet) throws IOException {
        for (ClientHandler c : clients) {
            if (c.getGameToken().equals(gameToken)) {
                c.out.write(ProtocolCodes.buildBroadcastMessagePacket(packet));
            }
        }
    }
}
