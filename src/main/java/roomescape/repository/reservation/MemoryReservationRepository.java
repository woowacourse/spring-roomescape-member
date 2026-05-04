/*
package roomescape.repository.reservation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.Reservation.ReservationCommand;
import roomescape.domain.ReservationTime.ReservationTime;

@Repository
@Profile("console")
public class MemoryReservationRepository implements ReservationRepository {
    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(0);

    @Override
    public List<Reservation> getAllReservation() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public Reservation addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime) {
        Reservation reservation = new Reservation(index.incrementAndGet(), reservationCommand.name(), reservationCommand.date(), reservationTime);
        reservations.add(reservation);
        return reservation;
    }

    @Override
    public void deleteReservation(long id) {
        Optional<Reservation> deletedReservation = reservations.stream()
                .filter(reservation -> reservation.id() == id)
                .findFirst();

        if(deletedReservation.isEmpty()) {
            return;
        }

        reservations.remove(deletedReservation.get());
    }

    @Override
    public boolean existsByTimeId(long timeId) {
        return reservations.stream().anyMatch(reservation -> reservation.time().id() == timeId);
    }
}
*/
