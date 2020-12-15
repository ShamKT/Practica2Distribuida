public class ChattingState implements State {

    private static final ChattingState instance = new ChattingState();

    private ChattingState() {

    }

    @Override
    public void handle() {

    }

    public static State getInstance() {
        return instance;
    }
}
