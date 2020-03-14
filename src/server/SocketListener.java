package server;

import battleship.BattleServerController;
import battleship.CommandProcessor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Cristian
 */
public class SocketListener {

    private int thePort = 0;
    private final CommandProcessor commandProcessor = new CommandProcessor();
    final int MAX_PLAYERS = 2;
    int numPlayers;

    public SocketListener(int newPort) {
        thePort = newPort;
        this.numPlayers = 0;
    }

    private boolean addPlayer() {
        if (this.numPlayers < MAX_PLAYERS) {
            numPlayers++;
            return true;
        }
        
        return false;
    }

    public void run() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        boolean quit = false;

        SocketController sc = null;
        try {
            serverSocket = new ServerSocket(thePort);
        } catch (IOException ex) {
            System.out.println("Error: Address alredy in use");
        }

        if (serverSocket != null) {
            System.out.println("");
            while (!quit) {
                try {
                    socket = serverSocket.accept();
                    if (addPlayer()) {
                        sc = new BattleServerController(socket,commandProcessor);
                    } else {
                        System.out.println("El máximo de jugadores permitos es "+ MAX_PLAYERS);
                    }
                    
                } catch (IOException ex) {
                    quit = true;
                }
            }
            System.out.println("Battleship finished");
        }
    }
}
