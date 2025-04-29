package roomescape.service;

import roomescape.entity.Reservation;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationInMemoryDao implements ReservationDao {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong atomic = new AtomicLong(1L);

    @Override
    public List<Reservation> findAll() {
        return reservations.stream()
                .toList();
    }

    @Override
    public Reservation save(Reservation reservation) {
        Reservation generatedReservation = new Reservation(atomic.getAndIncrement(), reservation.getName(), reservation.getDate(), reservation.getTime());
        reservations.add(generatedReservation);
        return generatedReservation;
    }

    @Override
    public void deleteById(Long id) {
        Reservation findReservation = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 예약 엔티티가 존재하지 않습니다."));

        reservations.remove(findReservation);
    }

    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
            .filter(reservation -> reservation.isSameId(id))
            .findFirst();
    }

    @Override
    public Optional<Reservation> findByTimeId(Long timeId) {
        return Optional.empty();
    }
}
