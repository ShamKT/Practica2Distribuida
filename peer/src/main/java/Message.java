import java.io.Serializable;

/**
 * Clase cliente que modela los mensajes que se enviarn entre peers.
 *
 * @author Orlando Ledesma Rincon
 *
 */
public class Message  implements Serializable {

    private static final long serialVersionUID = 8866525391119224781L;

    /**
     * Enumerción con los tipos de mensajes que hay.
     */
    public enum Type {
        CONNECT, DISCONNECT, ACCEPT, REJECT, MIDDLEMAN, MESSAGE
    }

    private final Type type;
    private final int destPort;
    private final Object data;

    /**
     * Constructor de un mensaje
     * @param type el tipo de mensaje
     * @param destPort el puerto de destino del mensaje
     * @param data la informacion extra que se puede enviar.
     */
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
