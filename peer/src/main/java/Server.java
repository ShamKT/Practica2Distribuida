import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket peerA = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(peerA.getInputStream());
                Message message = (Message) objectInputStream.readObject();

                switch (message.getType()) {
                    case CONNECT:
                        if (SharedData.getInstance().getState().getClass().equals(StandByState.class)) {
                            int portPeerA = (Integer) message.getData();
                            Connection connectionPeerA = new Connection(peerA);
                            connectionPeerA.setObjectInputStream(objectInputStream);
                            SharedData.getInstance().getSockets().put(portPeerA, connectionPeerA);
                        } else {
                            objectInputStream.close();
                            peerA.close();
                        }

                        break;

                    case MIDDLEMAN:
                        /*
                        if (SharedData.getInstance().getState().getClass().equals(StandByState.class)) {
                            int portPeerA = (Integer) message.getData();
                            Connection connectionPeerA = new Connection(peerA);
                            connectionPeerA.setObjectInputStream(objectInputStream);
                            SharedData.getInstance().getSockets().put(portPeerA, connectionPeerA);

                            int portPeerC = message.getDestPort();
                            Socket peerC = new Socket("localhost", message.getDestPort());
                            Connection connectionPeerC =  new Connection(peerC);
                            SharedData.getInstance().getSockets().put(portPeerC, connectionPeerC);

                        } else {
                            peerA.close();
                        }
                        */
                        break;

                    case DISCONNECT:
                    case CONFIRMATION:
                    case MESSAGE:
                        break;
                }



            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
