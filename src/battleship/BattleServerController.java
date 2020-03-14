package battleship;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.SocketController;

/**
 *
 * @author Cristian
 */
public class BattleServerController extends SocketController implements Runnable {
    
    private String username;
    
    CommandProcessor commandProcessor;
    
    public BattleServerController(Socket newSocket, CommandProcessor commandProcessor) throws IOException {
        super(newSocket);
        this.start(this);
        this.commandProcessor = commandProcessor;
        username = "";
    }

    @Override
    public void run() {
        String command = null;
        boolean quit = false;
        
        String usernames = "";
        writeText("Welcome to BattleShip Game! (version 1.0.0)");
        while (!quit) {
            command = readText();
            if (command != null) {

                if (command.trim().equalsIgnoreCase("QUIT")) {
                    try {
                        close();
                        quit = true;
                        System.exit(0);
                    } catch (IOException ex) {
                        Logger.getLogger(BattleServerController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                String response = commandProcessor.responseCommand(command,this);
                writeText(response);
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
