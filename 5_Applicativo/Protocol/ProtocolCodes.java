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
    
    /**
     * Codice per eliminare una partita
     */
    public static final int DELETE_GAME = 36;
    
    /**
     * Codice per forzare la fine della turno
     */
    public static final int FORCE_TURN_END = 37;
    
    /**
     * Codice per notificare che un giocatore è
     * entrato nella partita
     */
    public static final int PLAYER_JOINED_GAME = 38;
    
    /**
     * Codice per notificare che un giocatore ha
     * lasciato la partita
     */
    public static final int PLAYER_LEFT_GAME = 39;
    
    /**
     * Codice per notificare che l'admin ha
     * lasciato la partita
     */
    public static final int ADMIN_LEFT_GAME = 40;
    
    /**
     * Codice per ottenere la classifica dei giocatori
     * in base al punteggio fatto
     */
    public static final int GET_LEADERBOARD = 41;
    
    /**
     * Ritorna la parte di dati di un pacchetto, 
     * tralasciando il codice identificativo
     * @param packet il pacchetto da cui prendere i dati
     * @return i dati del pacchetto
     */
    public static byte[] getDataFromPacket(byte[] packet){
        byte[] data = new byte[packet.length - 1];
        for(int i=0;i< data.length;i++){
            data[i] = packet[i + 1];
        }
        return data;
    }
    
    /**
     * Costruisce il pacchetto per creare una partita
     * @param userName il nome dell'admin
     * @param turns il numero di turni della partita
     * @param length la lughezza in secondi della partita
     * (questa può essere cambiata automaticamente se troppo lunga o corta)
     * @return pacchetto per creare una partita
     */
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
    
    /**
     * Costriusce il pacchetto che conferma la
     * creazione della partita
     * @param gameName il token della partita
     * @param length la lunghezza effettiva della partita
     * @return il pacchetto che conferma la creazione della
     * partita
     */
    public static byte[] buildGameCreatedSuccessfullyPacket(String gameName, int length){
        byte[] packet = new byte[2];
        byte[] data = {(byte)length};
        packet[0] = (byte) (packet.length - 1);
        packet[1] = GAME_CREATED_SUCCESSFULLY;
        packet = addDataToPacket(packet, gameName.getBytes());
        packet = addDataToPacket(packet, data);
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che permette di unirsi
     * ad una partita
     * @param gameToken il token della partita a cui ci si 
     * vuole unire
     * @param userName il nome del giocatore
     * @return il pacchetto che permette di unirsi ad una partita
     */
    public static byte[] buildJoinGamePacket(String gameToken, String userName){
        byte[] packet = new byte[2];
        packet[0] = (byte) (packet.length - 1);
        packet[1] = JOIN_GAME;
        packet = addDataToPacket(packet, gameToken.getBytes());
        packet = addDataToPacket(packet, userName.getBytes());
        return packet;
    }
    
    /**
     * Costuisce il pacchetto che conferma l'unione di
     * un giocatore ad una partita
     * @param gameName il token della partita a cui il 
     * giocatore si è unito
     * @return il pacchetto che conferma l'unione di
     * un giocatore ad una partita
     */
    public static byte[] buildGameJoinedSuccessfullyPacket(String gameName, int length){
        byte[] packet = new byte[2];
        byte[] data = {(byte)length};
        packet[0] = (byte) (packet.length - 1);
        packet[1] = GAME_JOINED_SUCCESSFULLY;
        packet = addDataToPacket(packet, gameName.getBytes());
        packet = addDataToPacket(packet, data);
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che fa capire al
     * client che non è stato possibile unirsi
     * ad una partita
     * @return il pacchetto
     */
    public static byte[] buildGameJoinedUnsuccessfullyPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte) (packet.length - 1);
        packet[1] = GAME_JOINED_UNSUCCESSFULLY;
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che dice che l'username
     * scelto dal giocatore è già presente nella partita
     * @return il pacchetto
     */
    public static byte[] buildUsernameAlreadyUsedPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte) (packet.length - 1);
        packet[1] = USERNAME_ALREADY_USED;
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che richiede al server
     * la lista dei giocatori presenti in una partita
     * @param gameName il token della partita da cui
     * prendere la lista
     * @return il pacchetto completo
     */
    public static byte[] buildGetPlayerListPacket(String gameName){
        byte[] packet = new byte[2];
        packet[0] = (byte) (packet.length - 1);
        packet[1] = GET_PLAYER_LIST;
        packet = addDataToPacket(packet, gameName.getBytes());
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che ritorna la lista
     * dei giocatori
     * @param playerList la lista dei giocatori
     * @return il pacchetto completo
     */
    public static byte[] buildPlayerListReturnedPacket(String playerList){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = PLAYER_LIST_RETURNED;
        packet = addDataToPacket(packet, playerList.getBytes());
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che permette ad un giocatore di 
     * lasciare una partita
     * @param gameName la partita da cui il giocatore vuole uscire
     * @param userName il giocatore che vuole uscire
     * @return il pacchetto completo
     */
    public static byte[] buildLeaveGamePacket(String gameName, String userName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = LEAVE_GAME;
        packet = addDataToPacket(packet, gameName.getBytes());
        packet = addDataToPacket(packet, userName.getBytes());
        return packet;
    }
    
    /**
     * Costrusce il pacchetto che conferma che un giocatore
     * ha lasciato la partita
     * @return il pacchetto completo
     */
    public static byte[] buildGameLeavedSuccessfullyPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = GAME_LEAVED_SUCCESSFULLY;
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che dice che non è stato
     * possibile lasciare la partita
     * @return il pachhetto completo
     */
    public static byte[] buildGameLeavedUnsuccessfullyPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = GAME_LEAVED_UNSUCCESSFULLY;
        return packet;
    }
    
    /**
     * Permette di aggiungere in modo dinamico dei dati
     * ad un pacchetto qualsiasi
     * @param packet il pacchetto senza i dati
     * @param data i dati da aggiungere
     * @return il pacchetto con i dati aggiunti
     */
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
    
    /**
     * Costrusce il pacchetto che dice al server
     * di cominciare la partita
     * @param gameToken il token della partita che deve cominciare
     * @param userName il nome del giocatore che ha inviato il comando
     * @return il pacchetto completo
     */
    public static byte [] buildStartGamePacket(String gameToken, String userName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = START_GAME;
        packet = addDataToPacket(packet, gameToken.getBytes());
        packet = addDataToPacket(packet, userName.getBytes());
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che conferma che la partita
     * è stata creata correttamente
     * @return il pacchetto completo
     */
    public static byte [] buildGameStartedSuccessfullyPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = GAME_STARTED_SUCCESSFULLY;
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che dice che la partita
     * non è stata avviata
     * @return il pacchetto completo
     */
    public static byte [] buildGameStartedUnsuccessfullyPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = GAME_STARTED_UNSUCCESSFULLY;
        return packet;
    }
    
    /**
     * Specifica che il pacchetto deve essere mandato in broadcast
     * @param message il pacchetto da mandare 
     * @return il pacchetto completo
     */
    public static byte [] buildBroadcastMessagePacket(byte[] message){
        return message;
    }
    
    /**
     * Costruisce il pacchetto che manda le lettere digitate
     * dai giocatori al server
     * @param gameName il token della partita in cui è il giocatore
     * @param c il carattere inviato
     * @return il pacchetto completo
     */
    public static byte[] buildSendLetterPacket(String gameName, char c){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = SEND_LETTER;
        byte[] letter = {(byte)c};
        packet = addDataToPacket(packet, gameName.getBytes());
        packet = addDataToPacket(packet, letter);
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che dice al client quali sono gli
     * indici delle lettere che ha indovinato
     * @param indexes gli indici
     * @param letter la lettera scritta
     * @return il pacchetto completo
     */
    public static byte[] buildLetterIndexesPacket(byte[] indexes, byte letter){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = LETTER_INDEXES;
        byte[] character = {letter};
        packet = addDataToPacket(packet, character);
        packet = addDataToPacket(packet, indexes);
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che richiede la parola
     * corrente al server
     * @param gameToken la partita da cui prendere la parola
     * @return il pacchetto completo
     */
    public static byte[] buildRequestGameWordPacket(String gameToken){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = REQUEST_GAME_WORD;
        packet = addDataToPacket(packet, gameToken.getBytes());
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che ritorna la parola 
     * di una partita
     * @param word la parola
     * @return il pacchetto completo
     */
    public static byte[] buildGameWordPacket(String word){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = RETURN_GAME_WORD;
        packet = addDataToPacket(packet, word.getBytes());
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che conferma che
     * un giocatore ha indovinato la parola
     * @param gameToken il token della partita 
     * @param playerName il nome del giocatore
     * @return il pacchetto completo
     */
    public static byte[] buildPlayerWonPacket(String gameToken, String playerName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = PLAYER_WON_TURN;
        packet = addDataToPacket(packet, gameToken.getBytes());
        packet = addDataToPacket(packet, playerName.getBytes());
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che conferma che
     * un giocatore ha fatto 10 errori
     * @param gameToken il token della partita 
     * @param playerName il nome del giocatore
     * @return il pacchetto completo
     */
    public static byte[] buildPlayerLostPacket(String gameToken, String playerName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = PLAYER_LOST_TURN;
        packet = addDataToPacket(packet, gameToken.getBytes());
        packet = addDataToPacket(packet, playerName.getBytes());
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che notifica che un giocatore
     * ha indovinato la parola (funzionalità per chat)
     * @param playerName il nome del giocatore
     * @return il pacchetto completo
     */
    public static byte[] buildNotifyPlayerWonTurnPacket(String playerName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = NOTIFY_TURN_WON;
        String msg = "Player " + playerName + " guessed the word!";
        packet = addDataToPacket(packet, msg.getBytes());
        return packet;
    }    
    
    /**
     * Costruisce il pacchetto che notifica che un giocatore
     * ha persoil turno (funzionalità per chat)
     * @param playerName il nome del giocatore
     * @return il pacchetto completo
     */
    public static byte[] buildNotifyPlayerLostTurnPacket(String playerName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = NOTIFY_TURN_LOST;
        String msg = "Player " + playerName + " made 10 errors!";
        packet = addDataToPacket(packet, msg.getBytes());
        return packet;
    } 
    
    /**
     * Costruisce il pacchetto che termina la partita
     * @return il pacchetto completo
     */
    public static byte[] buildEndGamePacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = END_GAME;
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che aggiunge un errore
     * al contatore di un giocatore
     * @param gameName il token della partita in cui è il giocatore
     * @param userName il nome del giocatore
     * @return il pacchetto completo
     */
    public static byte[] buildAddErrorPacket(String gameName, String userName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = ADD_ERROR;
        packet = addDataToPacket(packet, gameName.getBytes());
        packet = addDataToPacket(packet, userName.getBytes());
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che termina il turno corrente
     * @return il pacchetto completo
     */
    public static byte[] buildEndTurnPacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = END_TURN;
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che rimuove
     * la partita dalla lista di partite disponibili
     * @param gameName il token della partita da togliere
     * @return il pacchetto completo
     */
    public static byte[] buildDeleteGamePacket(String gameName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = DELETE_GAME;
        packet = addDataToPacket(packet, gameName.getBytes());
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che forza la fine del turno
     * @param gameName il token della partita
     * @return il pacchetto completo
     */
    public static byte[] buildForceTurnEndPacket(String gameName){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = FORCE_TURN_END;
        packet = addDataToPacket(packet, gameName.getBytes());
        return packet;
    }
    
    /**
     * Costruisce il pacchetto per notificare che un giocatore
     * si è unito ad una partita
     * @param username il nome del giocatore
     * @return il pacchetto completo
     */
    public static byte[] buildPlayerJoinedGamePacket(String username){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = PLAYER_JOINED_GAME;
        packet = addDataToPacket(packet, username.getBytes());
        return packet;
    }
    
    /**
     * Costruisce il pacchetto per notificare che un giocatore
     * è uscito ad una partita
     * @param username il nome del giocatore
     * @return il pacchetto completo
     */
    public static byte[] buildPlayerLeftGamePacket(String username){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = PLAYER_LEFT_GAME;
        packet = addDataToPacket(packet, username.getBytes());
        return packet;
    }
    
    /**
     * Costruisce il pacchetto che dice che l'admin
     * ha lasciato la partita
     * @return il pacchetto completo
     */
    public static byte[] buildAdminLeftGamePacket(){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = ADMIN_LEFT_GAME;
        return packet;
    }
    
    /**
     * Costriusce il pacchetto che richiede la classifica dei giocatori
     * @param gameToken la parita da cui ricevere la classifica
     * @return 
     */
    public static byte[] buildGetLeaderboardPacket(String gameToken){
        byte[] packet = new byte[2];
        packet[0] = (byte)(packet.length - 1);
        packet[1] = GET_LEADERBOARD;
        packet = addDataToPacket(packet, gameToken.getBytes());
        return packet;
    }
    
    /**
     * Rimuove il numero di byte da leggere in un pacchetto
     * @param packet il pacchetto a cui bisonga togliere 
     * la lunghezza
     * @return il pacchetto senza numero di byte da leggere 
     */
    public static byte[] removeLengthFromPacket(byte[] packet){
        byte[] newPacket = new byte[packet.length - 1];
        for(int i=0;i<newPacket.length;i++){
            newPacket[i] = packet[i+1];
        }
        return newPacket;
    }
    
    /**
     * Stampa i dati di un pacchetto
     * (utile per debugging)
     * @param data il pacchetto da stampare
     */
    public static void printPacket(byte[] data){
        for(int i=0;i<data.length;i++){
            System.out.println(data[i]);
        }
    }
    
    /**
     * Metodo utile per leggere un pacchetto 
     * da un certo indice fino ad un altro
     * @param packet il pacchetto da leggere
     * @param start l'indice dell'inizio
     * @param end l'indice della fine
     * @return 
     */
    public static byte[] readFromTo(byte[] packet, int start, int end) {
        byte[] data = new byte[end - start];
        for (int i = 0; i < data.length; i++) {
            data[i] = packet[start + i];
        }
        return data;
    }
    
}
