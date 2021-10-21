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

public class ClientHandler implements Runnable{

    private Socket client;
    private DataOutputStream out;
    private DataInputStream in;
    private List<ClientHandler> clients;
    private String gameToken;

    public ClientHandler(Socket client, List<ClientHandler> clients) throws IOException {
        this.client = client;
        this.clients = clients;
        gameToken = "";
        
        out = new DataOutputStream(client.getOutputStream());
        in = new DataInputStream(client.getInputStream());
    }
    
    private void setGameToken(String gameToken){
        this.gameToken = gameToken;
    }
    
    private String getGameToken(){
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
        } catch (IOException ex) {}
        finally{
            try {
                in.close();
            } catch (IOException ex) {}
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
            default:
                break;
        }
    }
    
    private void deleteGame(byte[] request){
        String requestGameToken = new String(readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        var game = GameHoster.getGame(requestGameToken);
        if(game != null){
            GameHoster.removeGame(requestGameToken);
            System.out.println("Removing " + requestGameToken);
        }
    }

    private void addError(byte[] request) {
        String requestGameToken = new String(readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        String playerName = new String(readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1));
        var game = GameHoster.getGame(requestGameToken);
        if(game != null){
            game.getPlayer(playerName).addError();
        }
    }

    private void playerLostOrWonTurn(byte[] request) throws IOException {
        String requestGameToken = new String(readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        String playerName = new String(readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1));
        var game = GameHoster.getGame(requestGameToken);
        if(game != null){
            if(game.getPlayer(playerName) != null){
                game.getPlayer(playerName).setHasFinished(true);
                game.addPointsToPlayer(playerName);
                if(request[0] == ProtocolCodes.PLAYER_WON_TURN)
                    broadcastToGame(requestGameToken, ProtocolCodes.buildNotifyPlayerWonTurnPacket(playerName));
                else
                    broadcastToGame(requestGameToken, ProtocolCodes.buildNotifyPlayerLostTurnPacket(playerName));
            }
            if(game.turnEnd()){
                game.nextTurn();
                broadcastToGame(requestGameToken, ProtocolCodes.buildEndTurnPacket());
            }else if(game.gameEnd()){
                broadcastToGame(requestGameToken, ProtocolCodes.buildEndGamePacket());
            }
        }
    }

    private void requestGameWord(byte[] request) throws IOException {
        String requestGameToken = new String(ProtocolCodes.getDataFromPacket(request));
        var game = GameHoster.getGame(requestGameToken);
        if(game != null){
            out.write(ProtocolCodes.buildGameWordPacket(game.getWord()));
        }
    }

    private void sendLetter(byte[] request) throws IOException {
        String requestGameToken = new String(readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        var game = GameHoster.getGame(requestGameToken);
        if(game != null){
            byte letter = readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1)[0];
            byte[] index = game.isLetterRight(letter);
            out.write(ProtocolCodes.buildLetterIndexesPacket(index, letter));
        }
    }

    private void startGame(byte[] request) throws IOException {
        String requestGameToken = new String(readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        String userName = new String(readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1));
        if(GameHoster.getGame(requestGameToken).getAdmin().equals(userName)){
            GameHoster.getGame(requestGameToken).startGame();
            broadcastToGame(requestGameToken, ProtocolCodes.buildGameStartedSuccessfullyPacket());
        }else{
            out.write(ProtocolCodes.buildGameStartedUnsuccessfullyPacket());
        }
    }

    private void leaveGame(byte[] request) throws IOException {
        String requestGameToken = new String(readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            game.removePlayer(new String(readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1)));
            out.write(ProtocolCodes.buildGameLeavedSuccessfullyPacket());
        }else {
            out.write(ProtocolCodes.buildGameLeavedUnsuccessfullyPacket());
        }
    }

    private void getPlayerList(byte[] request) throws IOException {
        String gameName = new String(ProtocolCodes.getDataFromPacket(request));
        out.write(ProtocolCodes.buildPlayerListReturnedPacket(GameHoster.getGameList(gameName)));
    }

    private void joinGame(byte[] request) throws IOException {
        String requestGameToken = new String(readFromTo(ProtocolCodes.getDataFromPacket(request), 0, 8));
        String userName = new String(readFromTo(ProtocolCodes.getDataFromPacket(request), 8, request.length - 1));
        var game = GameHoster.getGame(requestGameToken);
        if (game != null) {
            if(!game.containsName(userName)){
                setGameToken(requestGameToken);
                game.addPlayer(userName);
                out.write(ProtocolCodes.buildGameJoinedSuccessfullyPacket(requestGameToken));
            }else {
                out.write(ProtocolCodes.buildUsernameAlreadyUsedPacket());
            }
        }else {
            out.write(ProtocolCodes.buildGameJoinedUnsuccessfullyPacket());
        }
    }

    private void createGame(byte[] request) throws IOException {
        String requestGameToken = GameHoster.generateRandomGameName();
        GameHoster.addGame(requestGameToken);
        String userName = new String(ProtocolCodes.getDataFromPacket(request));
        GameHoster.getGame(requestGameToken).addPlayer(userName);
        GameHoster.getGame(requestGameToken).setAdmin(userName);
        setGameToken(requestGameToken);
        out.write(ProtocolCodes.buildGameCreatedSuccessfullyPacket(requestGameToken));
        System.out.println("Creating " + requestGameToken);
    }
    
    private byte[] readFromTo(byte[] packet, int start, int end){
        byte[] data = new byte[end - start];
        for(int i=0;i<data.length;i++){
            data[i] = packet[start + i];
        }
        return data;
    }
    
    private void broadcastToGame(String gameToken, byte [] packet) throws IOException{
        for(ClientHandler c : clients){
            if(c.getGameToken().equals(gameToken)){
                c.out.write(ProtocolCodes.buildBroadcastMessagePacket(packet));
            }
        }
    }
}
