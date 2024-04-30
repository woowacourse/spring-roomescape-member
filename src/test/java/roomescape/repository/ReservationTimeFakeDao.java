package roomescape.repository;

import roomescape.domain.ReservationTime;
import roomescape.repository.reservationtime.ReservationTimeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReservationTimeFakeDao implements ReservationTimeRepository {

    private final Map<Long, ReservationTime> reservationTimes;
    private Long id;

    public ReservationTimeFakeDao() {
        this.reservationTimes = new HashMap<>();
        this.id = 1L;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTime newReservationTIme = new ReservationTime(id, reservationTime.getStartAt());
        reservationTimes.put(id, newReservationTIme);

        id = Long.sum(id, 1L);
        return newReservationTIme;
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        if (reservationTimes.containsKey(id)) {
            return Optional.of(reservationTimes.get(id));
        }

        return Optional.empty();
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes.values().stream()
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        reservationTimes.remove(id);
    }
}
