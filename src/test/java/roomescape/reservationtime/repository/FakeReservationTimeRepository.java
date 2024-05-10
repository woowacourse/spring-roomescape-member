package roomescape.reservationtime.repository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.model.ReservationTime;

public class FakeReservationTimeRepository implements ReservationTimeRepository {
    private final List<ReservationTime> reservationTimes = new ArrayList<>();

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        ReservationTime newReservationTime = ReservationTime.of((long) reservationTimes.size() + 1, reservationTime);
        reservationTimes.add(newReservationTime);
        return newReservationTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return Collections.unmodifiableList(reservationTimes);
    }

    @Override
    public Optional<ReservationTime> findById(final Long timeId) {
        int index = timeId.intValue() - 1;
        if (reservationTimes.size() > index) {
            return Optional.of(reservationTimes.get(index));
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(final Long id) {
        return reservationTimes.stream()
                .anyMatch(reservationTime -> reservationTime.isSameTo(id));
    }

    @Override
    public boolean existsByStartAt(final LocalTime time) {
        return reservationTimes.stream()
                .anyMatch(reservationTime -> reservationTime.isSameStartAt(time));
    }

    @Override
    public void deleteById(final Long id) {
        reservationTimes.remove(id.intValue() - 1);
    }
}
