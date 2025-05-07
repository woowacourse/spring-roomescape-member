package roomescape.persistence.fakerepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import roomescape.business.ReservationTheme;
import roomescape.persistence.ReservationThemeRepository;

@Repository
public class FakeReservationThemeRepository implements ReservationThemeRepository, FakeRepository {

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
        themes.add(savedReservationTheme);
        return savedReservationTheme.getId();
    }

    @Override
    public boolean existByName(String name) {
        return themes.stream()
                .anyMatch(theme -> theme.getName().equals(name));
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
    public List<ReservationTheme> findByStartDateAndEndDateOrderByReservedDesc(LocalDate start,
                                                                               LocalDate end,
                                                                               int limit) {
        return themes;
    }

    @Override
    public void clear() {
        themes.clear();
        idGenerator.set(1);
    }
}
