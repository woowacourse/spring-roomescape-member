package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import roomescape.common.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;

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
    public Long save(Reservation reservation) {
        Long id = idGenerator.getAndIncrement();
        Reservation saved = Reservation.of(id, reservation.name(), reservation.date(), reservation.time(),
                reservation.theme(), reservation.status());
        store.put(id, saved);
        return id;
    }

    public List<Reservation> saveAll(List<Reservation> reservations) {
        List<Reservation> savedReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            savedReservations.add(saveV2(reservation));
        }
        return savedReservations;
    }

    public Reservation saveV2(Reservation reservation) {
        Long id = idGenerator.getAndIncrement();
        Reservation saved = Reservation.of(id, reservation.name(), reservation.date(), reservation.time(),
                reservation.theme(), reservation.status());
        store.put(id, saved);
        return saved;
    }

    @Override
    public boolean existsByDateAndTimeAndThemeId(LocalDate date, LocalTime time, Long themeId) {
        return store.values().stream()
                .anyMatch(reservation ->
                        reservation.date().equals(date) &&
                        reservation.time().equals(time) &&
                        reservation.theme().id().equals(themeId)
                );
    }

    @Override
    public boolean existsByNameAndDateAndTime(String name, LocalDate date, LocalTime time) {
        return store.values().stream()
                .anyMatch(reservation ->
                        reservation.name().equals(name) &&
                        reservation.date().equals(date) &&
                        reservation.time().equals(time)
                );
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
