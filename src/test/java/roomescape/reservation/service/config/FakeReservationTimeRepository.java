package roomescape.reservation.service.config;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    List<ReservationTime> reservationTimes = new ArrayList<>(List.of(
            new ReservationTime(1L, LocalTime.of(10, 0)),
            new ReservationTime(2L, LocalTime.of(11, 0))
    ));
    Long currentId = 3L;

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTime saved = new ReservationTime(
                currentId++,
                reservationTime.getStartAt()
        );
        reservationTimes.add(saved);
        return saved;
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes;
    }

    @Override
    public void deleteById(Long id) {
        reservationTimes.removeIf(reservationTime -> reservationTime.getId().equals(id));
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return reservationTimes.stream()
                .filter(reservationTime -> reservationTime.getId().equals(id))
                .findFirst();
    }
}