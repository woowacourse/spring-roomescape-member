package roomescape.repository;

import roomescape.domain.Reservation;
import roomescape.repository.reservation.ReservationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReservationFakeDao implements ReservationRepository {

    private final Map<Long, Reservation> reservations;
    private Long id;

    public ReservationFakeDao() {
        this.reservations = new HashMap<>();
        this.id = 1L;
    }

    @Override
    public Reservation save(Reservation reservation) {
        Reservation newReservation = new Reservation(id, reservation.getName(),
                reservation.getDate(), reservation.getTime());

        reservations.put(id, newReservation);
        id = Long.sum(id, 1L);

        return newReservation;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        if (reservations.containsKey(id)) {
            return Optional.of(reservations.get(id));
        }

        return Optional.empty();
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.values().stream()
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        reservations.remove(id);
    }
}
