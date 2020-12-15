import java.util.HashMap;
import java.util.LinkedList;

public class SharedData {

    private static final SharedData instance = new SharedData();

    private Contact self;
    private State state;
    private final HashMap<Integer, Connection> connections = new HashMap<>();
    private volatile LinkedList<String[]> queue = new LinkedList<>();

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

    public HashMap<Integer, Connection> getSockets() {
        return connections;
    }

    public LinkedList<String[]> getQueue(){
        return queue;
    }

    public static SharedData getInstance() {
        return instance;
    }

}
