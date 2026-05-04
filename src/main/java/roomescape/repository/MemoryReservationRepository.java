package roomescape.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public class MemoryReservationRepository implements ReservationRepository {
    List<Reservation> reservations = new ArrayList<>();
    private AtomicLong index = new AtomicLong(0);

    @Override
    public List<Reservation> read() {
        return List.copyOf(reservations);
    }

    @Override
    public Reservation findById(final long id) {
        return reservations.stream()
                .filter(it -> it.isEqualTo(id))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public void delete(long id) {
        Reservation reservation = reservations.stream()
                .filter(it -> it.isEqualTo(id))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        reservations.remove(reservation);
    }

    @Override
    public Reservation create(final String name, final LocalDate date, final Long timeId) {
        Reservation newReservation = new Reservation(
                index.incrementAndGet(),
                name,
                date,
                new ReservationTime(timeId, null)
        );
        reservations.add(newReservation);
        return newReservation;
    }
}
