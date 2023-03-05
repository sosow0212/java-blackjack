import controller.BlackJackController;
import service.BlackJackService;
import view.InputView;
import view.OutputView;

public class main {

    public static void main(String[] args) {
        BlackJackController blackJackController = new BlackJackController(new InputView(), new OutputView(), new BlackJackService());
        blackJackController.run();
    }
}
