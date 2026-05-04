package roomescape.repository.reservationTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.ReservationTime.ReservationTimeCommand;

@Repository
@Profile("console")
public class MemoryReservationTimeRepository implements ReservationTimeRepository {
    private final List<ReservationTime> reservationTimes = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(0);

    @Override
    public ReservationTime addReservationTime(ReservationTimeCommand reservationTimeCommand) {
        ReservationTime reservationTime = new ReservationTime(index.incrementAndGet(), reservationTimeCommand.startAt());
        reservationTimes.add(reservationTime);
        return reservationTime;
    }

    @Override
    public Optional<ReservationTime> getReservationTime(long id) {
        return reservationTimes.stream()
                .filter(reservationTime -> reservationTime.id() == id)
                .findFirst();
    }

    @Override
    public List<ReservationTime> getAllReservationTime() {
        return Collections.unmodifiableList(reservationTimes);
    }

    @Override
    public void deleteReservationTime(long id) {
        Optional<ReservationTime> deletedReservationTime = reservationTimes.stream().filter(reservation -> reservation.id() == id).findFirst();

        if(deletedReservationTime.isEmpty()) {
            return;
        }

        reservationTimes.remove(deletedReservationTime.get());
    }
}
