package roomescape.unit.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Theme;
import roomescape.exception.InvalidThemeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {

    private final AtomicLong index = new AtomicLong(1L);
    private final List<Theme> themes = new ArrayList<>();

    private final ReservationRepository reservationRepository;

    public FakeThemeRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Theme add(Theme theme) {
        long id = index.getAndIncrement();
        Theme addTheme = new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
        themes.add(addTheme);
        return addTheme;
    }

    @Override
    public List<Theme> findAll() {
        return Collections.unmodifiableList(themes);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return themes.stream()
                .filter(theme -> theme.getId().equals(id))
                .findAny();
    }

    @Override
    public boolean existsReservationByThemeId(long id) {
        return reservationRepository.findAll()
                .stream()
                .anyMatch(reservation -> reservation.getTheme().getId().equals(id));
    }

    @Override
    public void deleteById(Long id) {
        Optional<Theme> findTheme = themes.stream()
                .filter(theme -> theme.getId().equals(id))
                .findAny();
        findTheme.orElseThrow(() -> new InvalidThemeException("존재하지 않는 테마 id입니다."));
        themes.remove(findTheme.get());
    }
}
