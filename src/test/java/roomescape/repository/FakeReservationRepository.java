package roomescape.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.domain.Reservation;

public class FakeReservationRepository implements ReservationRepository {

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
    public List<Reservation> findByName(String name) {
        return storage.values().stream()
                .filter(reservation -> reservation.name().equals(name))
                .toList();
    }

    @Override
    public List<Long> findByThemeIdAndDate(long themeId, LocalDate date) {
        return storage.values().stream()
                .filter(reservation -> reservation.theme().id().equals(themeId) && reservation.date().equals(date))
                .map(reservation -> reservation.timeSlot().id())
                .toList();
    }

    @Override
    public Reservation save(Reservation reservation) {
        long id = sequence++;
        Reservation savedReservation = new Reservation(id, reservation.name(), reservation.date(),
                reservation.timeSlot(),
                reservation.theme());
        storage.put(id, savedReservation);
        return savedReservation;
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }

    @Override
    public Optional<Reservation> findByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        return storage.values().stream()
                .filter(reservation -> isDuplicate(reservation, date, timeId, themeId))
                .findAny();
    }

    @Override
    public int update(Reservation reservation) {
        if (!storage.containsKey(reservation.id())) {
            return 0;
        }
        storage.put(reservation.id(), reservation);
        return 1;
    }

    private boolean isDuplicate(Reservation reservation, LocalDate date, Long timeId, Long themeId) {
        return reservation.date().equals(date)
                && reservation.timeSlot().id().equals(timeId)
                && reservation.theme().id().equals(themeId);
    }
}
