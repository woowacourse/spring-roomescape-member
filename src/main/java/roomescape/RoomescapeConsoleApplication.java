package roomescape;

import roomescape.console.config.ConsoleConfig;
import roomescape.console.controller.FrontController;

public class RoomescapeConsoleApplication {
    public static void main(String[] args) {
        final FrontController frontController = ConsoleConfig.frontController();
        frontController.run();
    }
}
