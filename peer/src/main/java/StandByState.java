import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Clase que modela el comportamiento de la aplicacion mientras se enucnetra en espera de alguna conexion, permite
 * ingresar los comandos @conecta, @contactos, @sobrenombre y @salir
 *
 * @author Orlando Ledesma Rincon
 */
public class StandByState implements State {

    private static final StandByState instance = new StandByState();

    private StandByState() {

    }

    @Override
    public void handle() {

        if (!SharedData.getInstance().getInputQueue().isEmpty()) {

            String input = SharedData.getInstance().getInputQueue().poll();


            if (input != null && input.length() > 0 && input.charAt(0) == '@') {
                String[] inputArr = input.split(" ");

                switch (inputArr[0]) {
                    case "@sobrenombre":
                        if (inputArr.length > 1) {
                            SharedData.getInstance().getSelf().setName(inputArr[1]);
                            System.out.println("Se cambio el nombre a " + inputArr[1]);
                        } else{
                            System.out.println("Debes ingresar un nombre");
                        }

                        break;

                    case "@contactos":
                        System.out.println("Contactos:");
                        SharedData.getInstance().getSelf().getContacts().forEach((key, value) -> System.out.println(value.getName() + " " + value.getPort()));
                        break;

                    case "@conecta":
                        int connectPort = -1;
                        if (inputArr.length > 1) {
                            try {
                                // intenta convertir el argunmetno del comando a un entero
                                connectPort = Integer.parseInt(inputArr[1]);
                            } catch (NumberFormatException e) {
                                // si no se puedfe convertir a un entero se busca en el hashmap de los contactos
                                Contact connectContact = SharedData.getInstance().getSelf().getContacts().get(inputArr[1]);
                                if (connectContact != null)
                                    connectPort = connectContact.getPort();
                                else
                                    System.out.println("No se encontro al contacto.");
                            }
                        } else {
                            System.out.println("Debes ingresar el puerto o el nombre del contacto@");
                        }
                        if (connectPort != -1) {
                            try {
                                Connection peerB = new Connection(new Socket("localhost", connectPort));
                                SharedData.getInstance().getConnections().put(connectPort, peerB);
                                peerB.getObjectOutputStream().writeObject(new Message(Message.Type.CONNECT, connectPort, SharedData.getInstance().getSelf().getPort()));
                                Message message = (Message) peerB.getObjectInputStream().readObject();
                                if (message.getType() == Message.Type.ACCEPT) {
                                    SharedData.getInstance().setState(ChattingState.getInstance());
                                    SharedData.getInstance().getConnections().put(connectPort, peerB);
                                    ChattingState.getInstance().setChattingPort(connectPort);
                                    Contact contact = (Contact) message.getData();
                                    SharedData.getInstance().getSelf().getContacts().put(contact.getName(), contact);
                                    new Thread(peerB, connectPort + "-THREAD").start();
                                    System.out.println("Conectado con el puerto " + connectPort);
                                    System.out.println("Puedes usar los comandos:\n   @desconectar\n   @salir\n");

                                } else if (message.getType() == Message.Type.REJECT) {
                                    System.out.println("No se pudo conectar.(REJECT)");
                                }
                            } catch (ConnectException e) {
                                System.out.println("No se pudo conectar.(ConnectException)");
                            } catch (IllegalArgumentException e){
                                System.out.println("El puerto se encuentra fuera de rango");
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case "@salir":
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Comando no valido");
                        break;
                }
            }
        }

    }

    public static StandByState getInstance() {
        return instance;
    }

}
