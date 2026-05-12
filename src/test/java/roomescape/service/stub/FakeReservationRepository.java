package roomescape.service.stub;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    private long sequence = 1L;
    private final List<Reservation> storage = new ArrayList<>();

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public List<Reservation> findAllByName(String name) {
        return List.of();
    }

    @Override
    public Optional<Reservation> findById(final long id) {
        return storage.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst();
    }

    @Override
    public void deleteById(final long id) {
        storage.removeIf(reservation -> reservation.getId().equals(id));
    }

    @Override
    public Reservation save(final Reservation reservation) {
        Reservation saved = reservation;
        if (reservation.getId() == null) {
            saved = reservation.withId(sequence++);
        }
        storage.add(saved);
        return saved;
    }

    @Override
    public boolean existsByDateAndTimeId(final LocalDate date, final long timeId) {
        return storage.stream()
                .anyMatch(reservation ->
                        reservation.getDate().equals(date)
                                && reservation.getTime().getId() == timeId
                );
    }

    @Override
    public boolean existsByTimeId(final long timeId) {
        return storage.stream()
                .anyMatch(reservation -> reservation.getTime().getId() == timeId);
    }

    @Override
    public boolean existsByThemeId(long themeId) {
        return storage.stream()
                .anyMatch(reservation -> reservation.getTime().getTheme().getId() == themeId);
    }

    @Override
    public List<Long> findAllByDateAndThemeId(final LocalDate date, final long themeId) {
        return storage.stream()
                .filter(reservation -> reservation.getDate().equals(date))
                .filter(reservation -> reservation.getTime().getTheme().getId() == themeId)
                .map(reservation -> reservation.getTime().getId())
                .toList();
    }

    @Override
    public void update(Reservation reservation) {
            storage.stream()
                    .filter(r -> r.getId().equals(reservation.getId()))
                    .findFirst()
                    .ifPresent(r -> {
                        storage.remove(r);
                        storage.add(reservation);
                    });
    }

}
