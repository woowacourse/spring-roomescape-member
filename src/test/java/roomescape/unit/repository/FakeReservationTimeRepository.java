package roomescape.unit.repository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.InvalidReservationTimeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final AtomicLong index = new AtomicLong(1L);
    private final List<ReservationTime> reservationTimes = new ArrayList<>();

    private final ReservationRepository reservationRepository;

    public FakeReservationTimeRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public ReservationTime add(ReservationTime reservationTime) {
        long id = index.getAndIncrement();
        ReservationTime addReservationTime = new ReservationTime(id, reservationTime.getTime());
        reservationTimes.add(addReservationTime);
        return addReservationTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return Collections.unmodifiableList(reservationTimes);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return reservationTimes.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findAny();
    }

    @Override
    public boolean existsByTime(LocalTime time) {
        return reservationTimes.stream()
                .anyMatch((reservationTime) -> reservationTime.getTime().equals(time));
    }

    @Override
    public boolean existsReservationByTimeId(Long id) {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .anyMatch(reservation -> reservation.getReservationTime().getId().equals(id));
    }

    @Override
    public void deleteById(Long id) {
        Optional<ReservationTime> findReservationTimes = reservationTimes.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findAny();
        findReservationTimes.orElseThrow(() -> new InvalidReservationTimeException("존재하지 않는 예약 시간 id입니다." + id));
        reservationTimes.remove(findReservationTimes.get());
    }
}
