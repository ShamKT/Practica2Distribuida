import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class Main {


    public static void main(String[] args) {

        SharedData.getInstance().setSelf(new Contact(Integer.parseInt(args[0]), "test"));



        SharedData.getInstance().setState(StandByState.getInstance());

        new Thread(() ->{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    SharedData.getInstance().getQueue().add(bufferedReader.readLine().split(" "));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "INPUT-THREAD").start();

        while (true)
            SharedData.getInstance().getState().handle();



    }
}
