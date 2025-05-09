package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import roomescape.reservation.entity.Reservation;

public class FakeReservationRepository implements ReservationRepository {

    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public Reservation save(Reservation reservation) {
        reservations.add(reservation);
        return reservation;
    }

    @Override
    public boolean deleteById(Long id) {
        return reservations.removeIf(entity -> entity.getId().equals(id));
    }

    @Override
    public boolean existsByDateAndTimeId(LocalDate date, Long timeId) {
        return findAll().stream()
                .anyMatch(reservation ->
                        reservation.getDate().equals(date) &&
                                reservation.getTime().getId().equals(timeId)
                );
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public List<Reservation> findAllByTimeId(Long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getTime().getId().equals(id))
                .toList();
    }
}
