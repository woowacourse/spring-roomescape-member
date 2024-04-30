package roomescape;

import roomescape.console.config.ConsoleConfig;
import roomescape.console.controller.ConsoleController;

public class ConsoleApplication {
    public static void main(String[] args) {
        ConsoleConfig consoleConfig = new ConsoleConfig();
        ConsoleController consoleController = new ConsoleController(
                consoleConfig.outputView(),
                consoleConfig.inputView(),
                consoleConfig.reservationTimeService(),
                consoleConfig.reservationService()
        );
        consoleController.run();
    }
}
