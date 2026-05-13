package roomescape.repository;

import java.time.LocalDate;
import java.util.*;

import roomescape.domain.Reservation;

public class FakeReservationDao implements ReservationRepository {

    private final Map<Long, Reservation> storage = new HashMap<>();
    private long sequence = 1L;

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public Optional<Reservation> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Reservation save(Reservation reservation) {
        long id = sequence++;
        Reservation savedReservation = new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme(),
                reservation.getReservationStatus()
        );
        storage.put(id, savedReservation);
        return savedReservation;
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }

    @Override
    public boolean isExistBy(Long themeId, LocalDate date, Long reservationTimeId) {
        return false;
    }

    @Override
    public List<Reservation> findByName(String name) {
        return storage.values().stream()
                .filter(reservation -> Objects.equals(reservation.getName(), name))
                .toList();
    }
}
