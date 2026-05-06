package roomescape.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roomescape.domain.Reservation;

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
        Reservation savedReservation = new Reservation(id, reservation.name(), reservation.date(), reservation.time());
        storage.put(id, savedReservation);
        return savedReservation;
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }
}
