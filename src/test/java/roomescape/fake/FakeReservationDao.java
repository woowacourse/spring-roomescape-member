package roomescape.fake;

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
            reservation.getTime()
        );
        reservations.add(saved);
        return saved;
    }

    public void removeReservationById(Long id) {
        reservations.removeIf(reservation -> reservation.getId().equals(id));
    }
}
