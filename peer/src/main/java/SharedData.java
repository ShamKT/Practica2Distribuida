import java.util.HashMap;
import java.util.LinkedList;

/**
 * Clase servidor que controla la entrada de mensajes desde otros peer.
 *
 * @author Orlando Ledesma Rincon
 */
public class SharedData {

    private static final SharedData instance = new SharedData();

    private Contact self;
    private volatile State state;
    private final HashMap<Integer, Connection> connections = new HashMap<>();
    private volatile LinkedList<String> inputQueue = new LinkedList<>();
    private volatile boolean loop = true;

    private SharedData() {

    }

    public Contact getSelf() {
        return self;
    }

    public void setSelf(Contact self) {
        this.self = self;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public HashMap<Integer, Connection> getConnections() {
        return connections;
    }

    public LinkedList<String> getInputQueue(){
        return inputQueue;
    }

    public boolean doLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public static SharedData getInstance() {
        return instance;
    }

}
