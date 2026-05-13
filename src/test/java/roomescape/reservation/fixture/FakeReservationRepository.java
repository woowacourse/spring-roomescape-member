package roomescape.reservation.fixture;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    private final Map<Long, Reservation> store = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Reservation> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public List<Reservation> findAllByNameOrderByDateAndTime(String name) {
        return store.values().stream()
                .filter(reservation -> reservation.name().equals(name))
                .toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Reservation save(Reservation reservation) {
        Long id = idGenerator.getAndIncrement();
        Reservation saved = Reservation.load(id, reservation.name(), reservation.date(), reservation.time(),
                reservation.theme(), reservation.status());
        store.put(id, saved);
        return saved;
    }

    public List<Reservation> saveAll(List<Reservation> reservations) {
        List<Reservation> savedReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            savedReservations.add(save(reservation));
        }
        return savedReservations;
    }

    @Override
    public boolean existsByDateAndTimeAndThemeId(Long dateId, Long timeId, Long themeId) {
        return store.values().stream()
                .anyMatch(reservation ->
                        reservation.date().id().equals(dateId) &&
                                reservation.time().id().equals(timeId) &&
                                reservation.theme().id().equals(themeId)
                );
    }

    @Override
    public boolean existsByNameAndDateAndTime(String name, Long dateId, Long timeId) {
        return store.values().stream()
                .anyMatch(reservation ->
                        reservation.name().equals(name) &&
                                reservation.date().id().equals(dateId) &&
                                reservation.time().id().equals(timeId)
                );
    }

    @Override
    public boolean existsByDateId(Long dateId) {
        return store.values().stream()
                .anyMatch(reservation -> reservation.date().id().equals(dateId));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        return store.values().stream()
                .anyMatch(reservation -> reservation.time().id().equals(timeId));
    }

    @Override
    public boolean updateStatus(Reservation reservation) {
        Optional<Reservation> findReservation = findById(reservation.id());
        if (findReservation.isEmpty()) {
            return false;
        }

        findReservation.get().updateStatus(reservation.status());
        return true;
    }
}
