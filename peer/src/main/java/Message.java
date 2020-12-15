import java.io.Serializable;

public class Message  implements Serializable {

    private static final long serialVersionUID = 8866525391119224781L;

    public enum Type {
        CONNECT, DISCONNECT, CONFIRMATION, MIDDLEMAN, MESSAGE
    }

    private final Type type;
    private final int destPort;
    private final Object data;

    public Message(Type type, int destPort, Object data){
        this.type = type;
        this.destPort = destPort;
        this.data = data;
    }

    public Type getType() {
        return type;
    }

    public int getDestPort() {
        return destPort;
    }

    public Object getData() {
        return data;
    }


}
