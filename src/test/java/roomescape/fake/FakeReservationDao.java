package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;

public class FakeReservationDao implements ReservationDao {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    public List<Reservation> findAllReservations() {
        return new ArrayList<>(reservations);
    }

    public Reservation addReservation(Reservation reservation) {
        Reservation saved = new Reservation(
            index.getAndIncrement(),
            reservation.getName(),
            reservation.getDate(),
            reservation.getTime(),
            reservation.getTheme()
        );
        reservations.add(saved);
        return saved;
    }

    public void removeReservationById(Long id) {
        reservations.removeIf(reservation -> reservation.getId().equals(id));
    }

    @Override
    public boolean existReservationByDateTimeAndTheme(LocalDate date, Long timeId, Long themeId) {
        return reservations.stream()
            .anyMatch(reservation -> reservation.getDate().isEqual(date)
                && reservation.getTime().getId().equals(timeId)
                && reservation.getTime().getId().equals(themeId));
    }
}
