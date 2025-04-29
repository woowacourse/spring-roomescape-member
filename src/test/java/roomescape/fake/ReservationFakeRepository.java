package roomescape.fake;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.repository.ReservationRepository;

public class ReservationFakeRepository implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new HashMap<>();
    private final Map<Long, ReservationTime> reservationTimes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ReservationFakeRepository() {
        ReservationTime defaultTime = new ReservationTime(1L, LocalTime.now());
        reservationTimes.put(1L, defaultTime);

        Reservation defaultReservation = new Reservation(
                idGenerator.getAndIncrement(),
                "브라운",
                LocalDate.of(2025, 1, 1),
                defaultTime);
        reservations.put(1L, defaultReservation);
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    @Override
    public Reservation save(Reservation reservation, long timeId) {
        if (!reservationTimes.containsKey(timeId)) {
            throw new EntityNotFoundException("예약 시간을 찾을 수 없습니다: " + timeId);
        }

        ReservationTime time = reservationTimes.get(timeId);

        long newId = idGenerator.getAndIncrement();

        Reservation savedReservation = new Reservation(
                newId,
                reservation.name(),
                reservation.date(),
                time);

        reservations.put(newId, savedReservation);

        return savedReservation;
    }

    @Override
    public void deleteById(long id) {
        if (!reservations.containsKey(id)) {
            throw new EntityNotFoundException("예약 데이터를 찾을 수 없습니다:" + id);
        }

        reservations.remove(id);
    }
}
