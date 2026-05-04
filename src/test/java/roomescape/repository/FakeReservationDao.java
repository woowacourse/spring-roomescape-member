package roomescape.repository;

import roomescape.domain.Reservation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeReservationDao implements ReservationRepository {

    private final Map<Long, Reservation> storage = new HashMap<>();
    private long sequence = 1L;

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public Reservation findById(long id) {
        return storage.get(id);
    }

    @Override
    public Reservation save(Reservation reservation) {
        long id = sequence++;
        Reservation savedReservation = new Reservation(id, reservation.name(), reservation.date(), reservation.reservationTime());
        storage.put(id, savedReservation);
        return savedReservation;
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }
}
