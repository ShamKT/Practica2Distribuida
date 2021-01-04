import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

/**
 * Clase main del cliente peer.
 * Inicializa los valores iniciales, un hilo para la entrada del teclado y otro para el hilo del servidor.
 *
 * @author Orlando Ledesma Rincon
 *
 */
public class Main {


    public static void main(String[] args) {

        // Asigna el nombre y el Estado como StandBy.
        SharedData.getInstance().setSelf(new Contact(Integer.parseInt(args[0]), args[1]));
        SharedData.getInstance().setState(StandByState.getInstance());

        // Crea un hilo para la entrada del teclado.
        new Thread(() ->{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (SharedData.getInstance().doLoop()) {
                try {
                    SharedData.getInstance().getInputQueue().add(bufferedReader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "INPUT-THREAD").start();

        // Crea un hilo para el servidor.
        new Thread(new Server(Integer.parseInt(args[0])),"SERVER-THREAD").start();

        System.out.println("Bienvenido " + args[1]);
        System.out.println("Puedes usar los comandos:\n   @conecta PUERTO\n   @conecta NOMBRE\n   @contactos\n   @sobrenombre\n   @salir\n");

        // Bucle para ejecutar el comportamiento correspondiente al estado en el que se encuentra la aplicacion.
        while (SharedData.getInstance().doLoop())
            SharedData.getInstance().getState().handle();



    }
}
