package roomescape;

import roomescape.console.ConsoleController;
import roomescape.console.dao.InMemoryReservationDao;
import roomescape.console.dao.InMemoryReservationTimeDao;
import roomescape.console.dao.InMemoryRoomThemeDao;
import roomescape.console.db.InMemoryReservationDb;
import roomescape.console.db.InMemoryReservationTimeDb;
import roomescape.console.db.InMemoryRoomThemeDb;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.RoomThemeDao;
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
        InMemoryRoomThemeDb inMemoryRoomThemeDb = new InMemoryRoomThemeDb();
        ReservationDao fakeReservationDao
                = new InMemoryReservationDao(inMemoryReservationDb);
        ReservationTimeDao fakeReservationTimeDao
                = new InMemoryReservationTimeDao(inMemoryReservationDb, inMemoryReservationTimeDb);
        RoomThemeDao fakeRoomThemeDao = new InMemoryRoomThemeDao(inMemoryRoomThemeDb);
        ReservationService reservationService
                = new ReservationService(fakeReservationDao, fakeReservationTimeDao, fakeRoomThemeDao);
        ReservationTimeService reservationTimeService
                = new ReservationTimeService(fakeReservationTimeDao, fakeReservationDao);
        return new ConsoleController(reservationService, reservationTimeService);
    }
}
