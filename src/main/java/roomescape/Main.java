package roomescape;

import roomescape.controller.console.RoomEscapeConsoleController;
import roomescape.dao.ConsoleReservationDao;
import roomescape.dao.ConsoleReservationTimeDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.view.InputView;
import roomescape.view.OutputView;

public class Main {

    public static void main(String[] args) {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();

        ReservationDao reservationDao = new ConsoleReservationDao();
        ReservationTimeDao reservationTimeDao = new ConsoleReservationTimeDao();

        ReservationService reservationService = new ReservationService(
                reservationDao,
                reservationTimeDao
        );

        ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeDao);

        RoomEscapeConsoleController roomEscapeConsoleController = new RoomEscapeConsoleController(
                inputView,
                outputView,
                reservationService,
                reservationTimeService
        );

        roomEscapeConsoleController.run();
    }
}
