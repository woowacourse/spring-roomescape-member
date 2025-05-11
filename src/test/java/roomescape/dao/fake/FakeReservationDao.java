package roomescape.dao.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;

public class FakeReservationDao implements ReservationDao {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    public List<Reservation> findAllReservations() {
        return Collections.unmodifiableList(reservations);
    }

    public boolean existReservationByDateTimeAndTheme(LocalDate date, Long timeId, Long themeId) {
        return reservations.stream()
            .anyMatch(r -> r.getDate().equals(date)
                && r.getTime().getId().equals(timeId)
                && r.getTheme().getId().equals(themeId));
    }

    public Reservation addReservation(Reservation reservation) {
        Reservation newReservation = new Reservation(
            index.getAndIncrement(),
            reservation.getMember(),
            reservation.getDate(),
            reservation.getTime(),
            reservation.getTheme());

        reservations.add(newReservation);
        return newReservation;
    }

    public void removeReservationById(Long id) {
        reservations.removeIf(r -> r.getId().equals(id));
    }
}
