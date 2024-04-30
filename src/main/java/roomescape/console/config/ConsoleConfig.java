package roomescape.console.config;

import roomescape.console.controller.FrontController;
import roomescape.console.controller.ReservationConsoleController;
import roomescape.console.controller.ReservationTimeConsoleController;
import roomescape.console.repository.ReservationConsoleRepository;
import roomescape.console.repository.ReservationTimeConsoleRepository;
import roomescape.console.view.RequestBodyMapper;
import roomescape.core.service.ReservationService;
import roomescape.core.service.ReservationTimeService;

public class ConsoleConfig {
    private static final ReservationConsoleRepository reservationConsoleRepository = new ReservationConsoleRepository();
    private static final ReservationTimeConsoleRepository reservationTimeConsoleRepository = new ReservationTimeConsoleRepository();

    private ConsoleConfig() {
    }

    public static FrontController frontController() {
        return new FrontController(reservationConsoleController(), reservationTimeConsoleController(),
                requestBodyMapper());
    }

    private static ReservationConsoleController reservationConsoleController() {
        return new ReservationConsoleController(reservationService());
    }

    private static ReservationTimeConsoleController reservationTimeConsoleController() {
        return new ReservationTimeConsoleController(reservationTimeService());
    }

    private static RequestBodyMapper requestBodyMapper() {
        return new RequestBodyMapper();
    }

    private static ReservationService reservationService() {
        return new ReservationService(reservationConsoleRepository(), reservationTimeConsoleRepository());
    }

    private static ReservationTimeService reservationTimeService() {
        return new ReservationTimeService(reservationTimeConsoleRepository(), reservationConsoleRepository());
    }

    private static ReservationConsoleRepository reservationConsoleRepository() {
        return reservationConsoleRepository;
    }

    private static ReservationTimeConsoleRepository reservationTimeConsoleRepository() {
        return reservationTimeConsoleRepository;
    }
}
