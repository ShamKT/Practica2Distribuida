import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StandByState implements State {

    private static final StandByState instance = new StandByState();

    private StandByState() {

    }

    @Override
    public void handle() {

        if (!SharedData.getInstance().getQueue().isEmpty()) {
            String[] input = SharedData.getInstance().getQueue().poll();


            switch (input[0]) {
                case "@sobrenombre":
                    if (input.length > 1) {
                        SharedData.getInstance().getSelf().setName(input[1]);
                        System.out.println("Se cambio el nombre a " + input[1]);
                    }

                    break;

                case "@contactos":
                    System.out.println("Contactos:");
                    SharedData.getInstance().getSelf().getContacts().forEach((contact) -> System.out.println(contact.getName() + " " + contact.getPort()));

                    break;

                case "@conecta":


                    break;
                case "":
                    break;
                default:
                    System.out.println("Comando no valido");
                    break;
            }
        }

    }

    public static State getInstance() {
        return instance;
    }

}
