package roomescape.service.stub;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> storage = new ArrayList<>();
    private long sequence = 1L;

    @Override
    public List<ReservationTime> findAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public List<ReservationTime> findAllByThemeId(final long themeId) {
        return storage.stream()
                .filter(time -> time.getTheme().getId() == themeId)
                .toList();
    }

    @Override
    public Optional<ReservationTime> findById(final long timeId) {
        return storage.stream()
                .filter(time -> time.getId().equals(timeId))
                .findFirst();
    }

    @Override
    public List<ReservationTime> findAvailableTimes(LocalDate date, Long themeId) {
        return storage.stream()
                .filter(time -> Objects.equals(time.getTheme().getId(), themeId))
                .toList();
    }

    @Override
    public void deleteById(final long timeId) {
        storage.removeIf(time -> time.getId().equals(timeId));
    }

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        ReservationTime saved = reservationTime;
        if (reservationTime.getId() == null) {
            saved = reservationTime.withId(sequence++);
        }
        storage.add(saved);
        return saved;
    }

    @Override
    public boolean existsByStartAtAndThemeId(final LocalTime startAt, final long themeId) {
        return storage.stream()
                .anyMatch(time ->
                        time.getStartAt().equals(startAt)
                                && time.getTheme().getId() == themeId
                );
    }

}
