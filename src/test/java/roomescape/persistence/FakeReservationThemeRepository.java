package roomescape.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.business.ReservationTheme;

public final class FakeReservationThemeRepository implements ReservationThemeRepository {

    private final List<ReservationTheme> themes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<ReservationTheme> findAll() {
        return themes;
    }

    @Override
    public Long add(ReservationTheme reservationTheme) {
        ReservationTheme savedReservationTheme = new ReservationTheme(
                idGenerator.getAndIncrement(),
                reservationTheme.getName(),
                reservationTheme.getDescription(),
                reservationTheme.getThumbnail()
        );
        if (themes.stream().anyMatch(theme -> theme.getName().equals(savedReservationTheme.getName()))) {
            throw new DataIntegrityViolationException("duplicated name");
        }
        themes.add(savedReservationTheme);
        return savedReservationTheme.getId();
    }

    @Override
    public void deleteById(Long id) {
        themes.removeIf(theme -> theme.getId().equals(id));
    }

    @Override
    public Optional<ReservationTheme> findById(Long id) {
        return themes.stream()
                .filter(theme -> theme.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<ReservationTheme> findByStartDateAndEndDateOrderByReservedDesc(LocalDate start, LocalDate end,
                                                                               int limit) {
        return themes;
    }
}
