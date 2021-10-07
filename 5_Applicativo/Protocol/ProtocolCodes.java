package Protocol;

/**
 *
 * @author gioele.zanetti
 */
public class ProtocolCodes {
    public static final int BROADCAST = 1;
    public static final int CREATE_GAME = 10;
    public static final int GAME_CREATED_SUCCESSFULLY = 11;
    public static final int JOIN_GAME = 12;
    public static final int GAME_JOINED_SUCCESSFULLY = 13;
    public static final int GAME_JOINED_UNSUCCESSFULLY = 14;
    public static final int USERNAME_ALREADY_USED = 15;
    public static final int ADD_PLAYER = 16;
    public static final int GET_PLAYER_LIST = 17;
    public static final int PLAYER_LIST_RETURNED = 18;
    public static final int LEAVE_GAME = 19;
    public static final int GAME_LEAVED_SUCCESSFULLY = 20;
    public static final int GAME_LEAVED_UNSUCCESSFULLY = 21;
    public static final int START_GAME = 22;
    public static final int GAME_STARTED_SUCCESSFULLY = 23;
    public static final int GAME_STARTED_UNSUCCESSFULLY = 24;
    public static final int SEND_LETTER = 25;
    public static final int LETTER_INDEXES = 26;
    public static final int REQUEST_GAME_WORD = 27;
    public static final int RETURN_GAME_WORD = 28;
    
    public static byte[] getDataFromPacket(byte[] packet){
        byte[] data = new byte[packet.length - 1];
        for(int i=0;i< data.length;i++){
            data[i] = packet[i + 1];
        }
        return data;
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
    
    public static byte[] buildCreateGamePacket(String userName){
        byte[] packet = new byte[2];
        packet[0] = (byte) (packet.length - 1);
        packet[1] = CREATE_GAME;
        packet = addDataToPacket(packet, userName.getBytes());
        return packet;
    }
    
    public static void main(String[] args) {
        StringBuilder s = new StringBuilder("mi piace nutella");
        s.replace(1,2, "a");
        System.out.println(s);
    }
}
