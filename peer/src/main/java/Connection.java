import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Clase que administra la conexion entre entre dos peers
 * Almacena el socket y sus output e input streams y contiene el comportamiento de un hilo para la entrada de mensajes
 *
 * @author Orlando Ledesma Rincon
 */
public class Connection implements Runnable {

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private volatile ObjectInputStream objectInputStream;
    private volatile boolean stop;

    /**
     * Constructor de una conexion, recibe el socket al que esta conectado el otro peer
     *
     * @param socket
     */
    public Connection(Socket socket){
        this.socket = socket;
        stop = false;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que cierra la conexion, cierra los input e output streams, asi como el socket
     */
    public void close(){
        stop = true;
        try {
            if (objectInputStream != null)
                objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream() {
        if (objectInputStream == null) {
            try {
                objectInputStream = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return objectInputStream;
    }

    /**
     * Metodo que ejecuta el hilo correspondiente a la conexion, se encarga de recibir los mensajes
     * entrantes desde el otro peer durante una conversacion.
     */
    @Override
    public void run() {

        try {
            while (true){
                if (objectInputStream == null) {
                    try {
                        objectInputStream = new ObjectInputStream(socket.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try{
                    Message message = (Message) objectInputStream.readObject();
                    switch (message.getType()) {
                        case DISCONNECT:
                            close();
                            SharedData.getInstance().getConnections().remove((Integer) message.getData());
                            SharedData.getInstance().setState(StandByState.getInstance());
                            System.out.println(ChattingState.getInstance().getChattingPort() + " se desconecto.");
                            break;
                        case MESSAGE:
                            System.out.println((String) message.getData());
                            break;
                        default:
                            break;
                    }
                } catch (SocketException ignored){
                }


                if (stop){
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
