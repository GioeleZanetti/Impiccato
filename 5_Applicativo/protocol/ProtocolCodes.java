/**
 * @version 7 set 2021
 * @author Gioele Zanetti
 */

package protocol;

public class ProtocolCodes {

    /**
     * codice per broadcast
     */
    public static final int BROADCAST = 1;
    
    /**
     * codice per creare una paritita
     */
    public static final int CREATE_GAME = 10;
    
    /**
     * codice per partita creata correttamente
     */
    public static final int GAME_CREATED_SUCCESSFULLY = 11;
    
    /**
     * codice per quando ci si unisce ad una partita
     */
    public static final int JOIN_GAME = 12;
    
    /**
     * codice per quando non ci si unisce correttamente
     * ad una partita
     */
    public static final int GAME_JOINED_SUCCESSFULLY = 13;
    
    /**
     * codice per qunado ci si unisce correttamente
     * ad una partita
     */
    public static final int GAME_JOINED_UNSUCCESSFULLY = 14;
    
    /**
     * Codice per quando il nome utente è
     * già utilizzato
     */
    public static final int USERNAME_ALREADY_USED = 15;
    
    /**
     * codice per aggiungere un giocatore
     */
    public static final int ADD_PLAYER = 16;
    
    /**
     * codice per ritornare la lista dei giocatori
     * di una partita
     */
    public static final int GET_PLAYER_LIST = 17;
    
    /**
     * codice per quando la lista dei giocaotri
     * è stata ritornata correttamente
     */
    public static final int PLAYER_LIST_RETURNED = 18;
    
    /**
     * codice per uscire da una partita
     */
    public static final int LEAVE_GAME = 19;
    
    /**
     * codice per quando non si esce dalla partita
     */
    public static final int GAME_LEAVED_SUCCESSFULLY = 20;
    
    /**
     * codice per quando si esce correttamente dalla partita
     */
    public static final int GAME_LEAVED_UNSUCCESSFULLY = 21;
    
    /**
     * codice per quando si comincia una partita
     */
    public static final int START_GAME = 22;
    
    /**
     * codice per quando la partita viene cominciata correttamente
     */
    public static final int GAME_STARTED_SUCCESSFULLY = 23;
    
    /**
     * codice per quando la partita non viene cominciata correttamente
     */
    public static final int GAME_STARTED_UNSUCCESSFULLY = 24;
    
    /**
     * codice per quando si invia una lettera
     */
    public static final int SEND_LETTER = 25;
    
    /**
     * codice per qunado vengono ritornati gli indici
     * delle lettere corrette
     */
    public static final int LETTER_INDEXES = 26;
    
    /**
     * codice per quando si richiede la parola corrente
     */
    public static final int REQUEST_GAME_WORD = 27;
    
    /**
     * codice per quando si ritorna la parola corrente
     */
    public static final int RETURN_GAME_WORD = 28;
    
    /**
     * codice per quando un giocatore vince un turno
     */
    public static final int PLAYER_WON_TURN = 29;
    
    /**
     * Codice per qunado bisogna finire la partita
     */
    public static final int END_GAME = 30;
    
    /**
     * codice per notificare che qualcuno ha vinto il turno
     */
    public static final int NOTIFY_TURN_WON = 31;
    
    /**
     * codice per quandoqualcuno perde il turno
     */
    public static final int PLAYER_LOST_TURN = 32;
    
    /**
     * codice per notificare che qualcuno ha perso il turno
     */
    public static final int NOTIFY_TURN_LOST = 33;
    
    /**
     * codice per aggiungere errori
     */
    public static final int ADD_ERROR = 34;
    
    /**
     * codice per finire il turno
     */
    public static final int END_TURN = 35;
    public static final int DELETE_GAME = 36;
    public static final int FORCE_TURN_END = 37;
    public static final int PLAYER_JOINED_GAME = 38;
    public static final int PLAYER_LEFT_GAME = 39;
    public static final int ADMIN_LEFT_GAME = 40;
    public static final int GET_LEADERBOARD = 41;
    
    public static byte[] getDataFromPacket(byte[] packet){
        byte[] data = new byte[packet.length - 1];
        for(int i=0;i< data.length;i++){
            data[i] = packet[i + 1];
        }
        return data;
    }
    
    public static byte[] buildCreateGamePacket(String userName, int turns, int length){
        byte[] packet = new byte[2];
        packet[0] = (byte) (packet.length - 1);
        packet[1] = CREATE_GAME;
        byte[] turnNumber = {(byte)turns};
        byte[] lengthNumber = {(byte)length};
        packet = addDataToPacket(packet, turnNumber);
        packet = addDataToPacket(packet, lengthNumber);
        packet = addDataToPacket(packet, userName.getBytes());
        return packet;
    }
    
    public static byte[] buildGameCreatedSuccessfullyPacket(String gameName, int length){
        byte[] packet = new byte[2];
        byte[] data = {(byte)length};
        packet[0] = (byte) (packet.length - 1);
        packet[1] = GAME_CREATED_SUCCESSFULLY;
        packet = addDataToPacket(packet, gameName.getBytes());
         packet = addDataToPacket(packet, data);
        return packet;
    }
    
    public static byte[] buildJoinGamePacket(String gameToken, String userName){
        byte[] packet = new byte[2];
        packet[0] = (byte) (packet.length - 1);
        packet[1] = JOIN_GAME;
        packet = addDataToPacket(packet, gameToken.getBytes());
        packet = addDataToPacket(packet, userName.getBytes());
        return packet;
    }
    
    public static byte[] buildGameJoinedSuccessfullyPacket(String gameName){
        byte[] packet = new byte[2];
        packet[0] = (byte) (packet.length - 1);
        packet[1] = GAME_JOINED_SUCCESSFULLY;
        packet = addDataToPacket(packet, gameName.getBytes());
        return packet;
    }
    
    public static byte[] buildGameJoinedUnsuccessfullyPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte) (packet.length - 1);
        packet[1] = GAME_JOINED_UNSUCCESSFULLY;
        return packet;
    }
    
    public static byte[] buildUsernameAlreadyUsedPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte) (packet.length - 1);
        packet[1] = USERNAME_ALREADY_USED;
        return packet;
    }
    
    public static byte[] buildGetPlayerListPacket(String gameName){
        byte[] packet = new byte[2];
        packet[0] = (byte) (packet.length - 1);
        packet[1] = GET_PLAYER_LIST;
        packet = addDataToPacket(packet, gameName.getBytes());
        return packet;
    }
    
    public static byte[] buildPlayerListReturnedPacket(String playerList){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = PLAYER_LIST_RETURNED;
        packet = addDataToPacket(packet, playerList.getBytes());
        return packet;
    }
    
    public static byte[] buildLeaveGamePacket(String gameName, String userName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = LEAVE_GAME;
        packet = addDataToPacket(packet, gameName.getBytes());
        packet = addDataToPacket(packet, userName.getBytes());
        return packet;
    }
    
    public static byte[] buildGameLeavedSuccessfullyPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = GAME_LEAVED_SUCCESSFULLY;
        return packet;
    }
    
    public static byte[] buildGameLeavedUnsuccessfullyPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = GAME_LEAVED_UNSUCCESSFULLY;
        return packet;
    }
    
    public static byte[] addDataToPacket(byte[] packet, byte[] data){
        byte[] newPacket = new byte[packet.length + data.length];
        for(int i=0;i<packet.length;i++){
            newPacket[i] = packet[i];
        }
        for(int i=0;i<data.length;i++){
            newPacket[i + packet.length] = data[i];
        }
        newPacket[0] = (byte) (newPacket.length - 1);
        return newPacket;
    }
    
    public static byte [] buildStartGamePacket(String gameToken, String userName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = START_GAME;
        packet = addDataToPacket(packet, gameToken.getBytes());
        packet = addDataToPacket(packet, userName.getBytes());
        return packet;
    }
    
    public static byte [] buildGameStartedSuccessfullyPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = GAME_STARTED_SUCCESSFULLY;
        return packet;
    }
    
    public static byte [] buildGameStartedUnsuccessfullyPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = GAME_STARTED_UNSUCCESSFULLY;
        return packet;
    }
    
    public static byte [] buildBroadcastMessagePacket(byte[] message){
        return message;
    }
    
    public static byte[] buildSendLetterPacket(String gameName, char c){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = SEND_LETTER;
        byte[] letter = {(byte)c};
        packet = addDataToPacket(packet, gameName.getBytes());
        packet = addDataToPacket(packet, letter);
        return packet;
    }
    
    public static byte[] buildLetterIndexesPacket(byte[] indexes, byte letter){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = LETTER_INDEXES;
        byte[] character = {letter};
        packet = addDataToPacket(packet, character);
        packet = addDataToPacket(packet, indexes);
        return packet;
    }
    
    public static byte[] buildRequestGameWordPacket(String gameToken){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = REQUEST_GAME_WORD;
        packet = addDataToPacket(packet, gameToken.getBytes());
        return packet;
    }
    
    public static byte[] buildGameWordPacket(String word){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = RETURN_GAME_WORD;
        packet = addDataToPacket(packet, word.getBytes());
        return packet;
    }
    
    public static byte[] buildPlayerWonPacket(String gameToken, String playerName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = PLAYER_WON_TURN;
        packet = addDataToPacket(packet, gameToken.getBytes());
        packet = addDataToPacket(packet, playerName.getBytes());
        return packet;
    }
    
    public static byte[] buildPlayerLostPacket(String gameToken, String playerName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = PLAYER_LOST_TURN;
        packet = addDataToPacket(packet, gameToken.getBytes());
        packet = addDataToPacket(packet, playerName.getBytes());
        return packet;
    }
    
    public static byte[] buildNotifyPlayerWonTurnPacket(String playerName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = NOTIFY_TURN_WON;
        String msg = "Player " + playerName + " guessed the word!";
        packet = addDataToPacket(packet, msg.getBytes());
        return packet;
    }    
    
    public static byte[] buildNotifyPlayerLostTurnPacket(String playerName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = NOTIFY_TURN_LOST;
        String msg = "Player " + playerName + " made 10 errors!";
        packet = addDataToPacket(packet, msg.getBytes());
        return packet;
    } 
    
    public static byte[] buildEndGamePacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = END_GAME;
        return packet;
    }
    
    public static byte[] buildAddErrorPacket(String gameName, String userName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = ADD_ERROR;
        packet = addDataToPacket(packet, gameName.getBytes());
        packet = addDataToPacket(packet, userName.getBytes());
        return packet;
    }
    
    public static byte[] buildEndTurnPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = END_TURN;
        return packet;
    }
    
    public static byte[] buildDeleteGamePacket(String gameName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = DELETE_GAME;
        packet = addDataToPacket(packet, gameName.getBytes());
        return packet;
    }
    
    public static byte[] buildForceTurnEndPacket(String gameName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = FORCE_TURN_END;
        packet = addDataToPacket(packet, gameName.getBytes());
        return packet;
    }
    
    public static byte[] buildPlayerJoinedGamePacket(String username){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = PLAYER_JOINED_GAME;
        packet = addDataToPacket(packet, username.getBytes());
        return packet;
    }
    
    public static byte[] buildPlayerLeftGamePacket(String username){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = PLAYER_LEFT_GAME;
        packet = addDataToPacket(packet, username.getBytes());
        return packet;
    }
    
    public static byte[] buildAdminLeftGamePacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = ADMIN_LEFT_GAME;
        return packet;
    }
    
    public static byte[] buildGetLeaderboardPacket(String gameToken){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = GET_LEADERBOARD;
        packet = addDataToPacket(packet, gameToken.getBytes());
        return packet;
    }
    
    public static byte[] removeLengthFromPacket(byte[] packet){
        byte[] newPacket = new byte[packet.length - 1];
        for(int i=0;i<newPacket.length;i++){
            newPacket[i] = packet[i+1];
        }
        return newPacket;
    }
    
    public static void printPacket(byte[] data){
        for(int i=0;i<data.length;i++){
            System.out.println(data[i]);
        }
    }
    
    public static byte[] readFromTo(byte[] packet, int start, int end) {
        byte[] data = new byte[end - start];
        for (int i = 0; i < data.length; i++) {
            data[i] = packet[start + i];
        }
        return data;
    }
    
    public static void main(String[] args) {
        byte[] a = removeLengthFromPacket(buildForceTurnEndPacket("aaaaaaaa"));
        printPacket(a);
        /*String s = "è";
        byte[] b = s.getBytes();
        for(int i=0;i<b.length;i++){
            System.out.println(b[i]);
        }*/
    }
}
