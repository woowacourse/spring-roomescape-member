package roomescape.reservation.repository;

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
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public List<Reservation> findAllByTimeId(Long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getTimeId().equals(id))
                .toList();
    }
}
