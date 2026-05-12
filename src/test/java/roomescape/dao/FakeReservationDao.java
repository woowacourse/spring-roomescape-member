package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public class FakeReservationDao implements ReservationDao {

    public static final String RESERVATION_TABLE = "reservation";

    private final FakeDatabase fakeDatabase;
    private Long currentId = 0L;

    public FakeReservationDao(FakeDatabase fakeDatabase) {
        this.fakeDatabase = fakeDatabase;
    }

    @Override
    public Reservation create(Reservation reservationWithoutId) {
        Reservation reservation = Reservation.of(++currentId, reservationWithoutId);
        fakeDatabase.create(RESERVATION_TABLE, reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public List<Reservation> readAll() {
        return fakeDatabase.readAll(RESERVATION_TABLE, Reservation.class).stream()
                .toList();
    }

    @Override
    public void delete(Long id) {
        fakeDatabase.delete(RESERVATION_TABLE, id);
    }

    @Override
    public boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        List<Reservation> reservations = fakeDatabase.readAll(RESERVATION_TABLE, Reservation.class);

        return reservations.stream()
                .anyMatch(reservation -> reservation.getDate().equals(date)
                        && reservation.getTime().getId().equals(timeId)
                        && reservation.getTheme().getId().equals(themeId));
    }

    @Override
    public boolean existByTimeId(Long timeId) {
        List<Reservation> reservations = fakeDatabase.readAll(RESERVATION_TABLE, Reservation.class);

        return reservations.stream()
                .anyMatch(reservation -> reservation.getTime().getId().equals(timeId));
    }

    @Override
    public boolean existByThemeId(Long themeId) {
        List<Reservation> reservations = fakeDatabase.readAll(RESERVATION_TABLE, Reservation.class);

        return reservations.stream()
                .anyMatch(reservation -> reservation.getTheme().getId().equals(themeId));

    }
}
