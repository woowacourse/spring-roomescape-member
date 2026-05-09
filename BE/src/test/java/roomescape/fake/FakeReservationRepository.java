package roomescape.fake;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    private final Map<Long, Reservation> store = new HashMap<>();
    private Long sequence = 1L;

    @Override
    public Reservation save(Reservation reservation) {
        if (reservation.id() == null) {
            Reservation saved = Reservation.createWithId(
                    sequence++,
                    reservation.name(),
                    reservation.date(),
                    reservation.time(),
                    reservation.theme()
            );
            store.put(saved.id(), saved);
            return saved;
        }

        store.put(reservation.id(), reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Reservation> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public boolean existsByReservationTimeId(Long reservationTimeId) {
        return store.values().stream()
                .anyMatch(reservation -> reservation.time()
                        .id()
                        .equals(reservationTimeId)
                );
    }

    @Override
    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        return store.values().stream()
                .filter(reservation -> date == null || reservation.date().equals(date))
                .filter(reservation -> themeId == null || reservation.theme().id().equals(themeId))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }
}
