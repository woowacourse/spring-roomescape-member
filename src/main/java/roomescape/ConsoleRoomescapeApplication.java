package roomescape;

import roomescape.console.ConsoleController;
import roomescape.console.dao.InMemoryReservationDao;
import roomescape.console.dao.InMemoryReservationTimeDao;
import roomescape.console.db.InMemoryReservationDb;
import roomescape.console.db.InMemoryReservationTimeDb;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;

public class ConsoleRoomescapeApplication {

    public static void main(String[] args) {
        ConsoleController consoleController = initConsoleController();
        consoleController.run();
    }

    private static ConsoleController initConsoleController() {
        InMemoryReservationDb inMemoryReservationDb = new InMemoryReservationDb();
        InMemoryReservationTimeDb inMemoryReservationTimeDb = new InMemoryReservationTimeDb();
        ReservationDao fakeReservationDao
                = new InMemoryReservationDao(inMemoryReservationDb);
        ReservationTimeDao fakeReservationTimeDao
                = new InMemoryReservationTimeDao(inMemoryReservationDb, inMemoryReservationTimeDb);
        ReservationService reservationService
                = new ReservationService(fakeReservationDao, fakeReservationTimeDao);
        ReservationTimeService reservationTimeService
                = new ReservationTimeService(fakeReservationTimeDao);
        return new ConsoleController(reservationService, reservationTimeService);
    }
}
