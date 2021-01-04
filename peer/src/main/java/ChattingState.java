import java.io.IOException;

/**
 * Clase que modela el comportamiento de la aplicacion mientras se enucnetra en conversando con otro usuario, permite
 * ingresar los comandos @desconectar y @salir
 *
 * @author Orlando Ledesma Rincon
 */
public class ChattingState implements State {

    private static final ChattingState instance = new ChattingState();

    private int chattingPort;

    private ChattingState() {

    }

    @Override
    public void handle() {

        if (!SharedData.getInstance().getInputQueue().isEmpty()) {

            Message message;
            String input = SharedData.getInstance().getInputQueue().poll();


            if (input != null && input.length() > 0 && input.charAt(0) == '@') {
                String[] inputArr = input.split(" ");

                switch (inputArr[0]) {
                    case "@desconectar":
                        // Envia un mensaje de tipo Disconnect al otro peer
                        message = new Message(Message.Type.DISCONNECT, chattingPort, SharedData.getInstance().getSelf().getPort());
                        try {
                            SharedData.getInstance().getConnections().get(chattingPort).getObjectOutputStream().writeObject(message);
                            SharedData.getInstance().getConnections().get(chattingPort).getObjectOutputStream().flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Cierra la conexion, elimina la conexion del hashmap y regresa al estado de standby
                        SharedData.getInstance().getConnections().get(chattingPort).close();
                        SharedData.getInstance().getConnections().remove(chattingPort);
                        SharedData.getInstance().setState(StandByState.getInstance());
                        System.out.println("Te desconectaste.");

                        break;
                    case "@salir":
                        // envia un mensaje de tipo disconnect y cierra la aplicacion
                        message = new Message(Message.Type.DISCONNECT, chattingPort, null);
                        try {
                            SharedData.getInstance().getConnections().get(chattingPort).getObjectOutputStream().writeObject(message);
                            SharedData.getInstance().getConnections().get(chattingPort).getObjectOutputStream().flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                        break;
                    default:
                        break;

                }


            } else {
                // Si o se ingreso ningun comando, se envia la entrada como mensaje al otro peer junto con el nombre de usuario.
                message = new Message(Message.Type.MESSAGE, chattingPort, SharedData.getInstance().getSelf().getName() + ": " + input);
                try {
                    SharedData.getInstance().getConnections().get(chattingPort).getObjectOutputStream().writeObject(message);
                    SharedData.getInstance().getConnections().get(chattingPort).getObjectOutputStream().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public int getChattingPort() {
        return chattingPort;
    }

    public void setChattingPort(int chattingPort) {
        this.chattingPort = chattingPort;
    }

    public static ChattingState getInstance() {
        return instance;
    }
}
