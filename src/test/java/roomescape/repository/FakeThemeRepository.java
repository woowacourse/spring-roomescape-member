package roomescape.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;

public class FakeThemeRepository implements ThemeRepository {

    private final List<Theme> themes;
    private final AtomicLong themeId = new AtomicLong(1);
    private final List<Reservation> reservations;

    public FakeThemeRepository(final List<Theme> themes) {
        this.themes = themes;
        reservations = new ArrayList<>();
    }

    @Override
    public Optional<Theme> save(Theme theme) {
        Theme newTheme = new Theme(themeId.getAndIncrement(), theme.name(), theme.description(), theme.thumbnail());
        themes.add(newTheme);
        return findById(newTheme.id());
    }

    @Override
    public List<Theme> findAll() {
        return themes;
    }

    @Override
    public int deleteById(long id) {
        Theme deleteTheme = themes.stream()
                .filter(theme -> Objects.equals(theme.id(), id))
                .findFirst()
                .orElse(new Theme(null, "A", "b", "c"));

        if (deleteTheme.id() != null) {
            if (reservations.stream()
                    .filter(reservation -> reservation.theme().equals(deleteTheme))
                    .count() != 0) {
                throw new IllegalStateException();
            }
            int affectedRows = (int) themes.stream()
                    .filter(theme -> Objects.equals(theme.id(), id))
                    .count();
            themes.remove(deleteTheme);
            return affectedRows;
        }

        return 0;
    }

    @Override
    public List<Theme> findPopular(LocalDate start, LocalDate end) {
        return List.of();
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    @Override
    public Optional<Theme> findById(long id) {
        return themes.stream()
                .filter(theme -> Objects.equals(theme.id(), id))
                .findFirst();
    }
}
