import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase servidor que controla la entrada de mensajes desde otros peer.
 *
 * @author Orlando Ledesma Rincon
 */
public class Server implements Runnable {

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    /**
     * Metodo run del servidor, espera a recibir alguna coneccion, despues recive un mensaje, verifica que sea de tipo
     * connect y que se encuentre en standby antes de aceptar o rechazar la conexion.
     */
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket peerA = serverSocket.accept();
                Connection connection = new Connection(peerA);
                Message message = (Message) connection.getObjectInputStream().readObject();
                Message answer = null;

                // si el mensaje es connect y la aplicacion se encuentra en StandBy, cambia el estado a chatting,
                // inicia el hilo de la conexion y envia una respuesta de confirmacion al otro peer.
                if (message.getType().equals(Message.Type.CONNECT) && SharedData.getInstance().getState().getClass().equals(StandByState.class)) {
                    SharedData.getInstance().setState(ChattingState.getInstance());
                    int port = (Integer) message.getData();
                    ChattingState.getInstance().setChattingPort(port);
                    SharedData.getInstance().getConnections().put(port, connection);
                    answer = new Message(Message.Type.ACCEPT, port, SharedData.getInstance().getSelf());
                    connection.getObjectOutputStream().writeObject(answer);
                    new Thread(connection, port + "-THREAD").start();
                    System.out.println("\nConectado con el puerto " + port);
                    System.out.println("Puedes usar los comandos:\n   @desconectar\n   @salir\n");


                    // si el mensaje no es connect o no se encuentra en StaddBy, se envia un mensaje reject de regreso y
                // se cierra la conexion.
                } else {
                    answer = new Message(Message.Type.REJECT, (Integer) message.getData(), null);
                    connection.getObjectOutputStream().writeObject(answer);
                    connection.close();
                }

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
