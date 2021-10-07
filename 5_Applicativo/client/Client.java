package client;

/**
 *
 * @author gioele.zanetti
 */
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 3000;

    private Socket server;
    private DataOutputStream out;
    private ServerHandler serverConnection;

    public Client() throws IOException {
        server = new Socket(SERVER_IP, SERVER_PORT);
        out = new DataOutputStream(server.getOutputStream());
        serverConnection = new ServerHandler(server, this);
        new Thread(serverConnection).start();
    }
}
