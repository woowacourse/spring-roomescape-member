package roomescape.dao;

import static roomescape.dao.FakeReservationDao.RESERVATION_TABLE;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public class FakeReservationTimeDao implements ReservationTimeDao {

    public static final String RESERVATION_TIME_TABLE = "reservationTime";

    private final FakeDatabase fakeDatabase;
    private Long currentId = 0L;

    public FakeReservationTimeDao(FakeDatabase fakeDatabase) {
        this.fakeDatabase = fakeDatabase;
    }

    @Override
    public ReservationTime create(ReservationTime reservationTimeWithoutId) {
        ReservationTime reservationTime = ReservationTime.of(++currentId, reservationTimeWithoutId);
        fakeDatabase.create(RESERVATION_TIME_TABLE, reservationTime.getId(), reservationTime);

        return reservationTime;
    }

    @Override
    public Optional<ReservationTime> read(Long id) {
        return Optional.ofNullable(fakeDatabase.read(RESERVATION_TIME_TABLE, id, ReservationTime.class));
    }

    @Override
    public List<ReservationTime> readAll() {
        return fakeDatabase.readAll(RESERVATION_TIME_TABLE, ReservationTime.class);
    }

    @Override
    public void delete(Long id) {
        fakeDatabase.delete(RESERVATION_TIME_TABLE, id);
    }

    @Override
    public List<Long> reservedTimeIdByDateAndTheme(LocalDate date, Long themeId) {
        List<Long> reservedTimeId = fakeDatabase.readAll(RESERVATION_TABLE, Reservation.class).stream()
                .filter(reservation -> reservation.getDate().equals(date) && reservation.getTheme().getId()
                        .equals(themeId))
                .map(reservation -> reservation.getTime().getId())
                .toList();

        return fakeDatabase.readAll(RESERVATION_TIME_TABLE, ReservationTime.class).stream()
                .map(ReservationTime::getId)
                .filter(reservedTimeId::contains)
                .toList();
    }
}
