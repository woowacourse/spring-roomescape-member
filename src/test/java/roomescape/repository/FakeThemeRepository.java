package roomescape.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;

public class FakeThemeRepository implements ThemeRepository {

    private final List<Theme> themes;
    private final AtomicLong themeId = new AtomicLong(1);
    private final List<Reservation> reservations = new ArrayList<>();

    public FakeThemeRepository(final List<Theme> themes) {
        this.themes = new ArrayList<>(themes);
    }

    @Override
    public Optional<Theme> save(Theme theme) {
        long count = themes.stream()
                .filter(t -> t.getName().equals(theme.getName()) && t.getDescription().equals(theme.getDescription()) && t.getThumbnail().equals(theme.getThumbnail()))
                .count();
        if (count != 0) {
            throw new IllegalStateException("Reservation time already exists");
        }

        Theme newTheme = new Theme(themeId.getAndIncrement(), theme.getName(), theme.getDescription(), theme.getThumbnail());
        themes.add(newTheme);
        return findById(newTheme.getId());
    }

    @Override
    public List<Theme> findAll() {
        return themes;
    }

    @Override
    public Optional<Theme> findById(long id) {
        return themes.stream()
                .filter(theme -> Objects.equals(theme.getId(), id))
                .findFirst();
    }

    @Override
    public List<Theme> findMostReservedByDateRange(LocalDate start, LocalDate end) {
        Map<Theme, Long> themeCounts = reservations.stream()
                .filter(r -> !r.getDate().isBefore(start) && r.getDate().isBefore(end))
                .collect(Collectors.groupingBy(Reservation::getTheme, Collectors.counting()));

        return themeCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public int deleteById(long id) {
        Theme deleteTheme = themes.stream()
                .filter(theme -> Objects.equals(theme.getId(), id))
                .findFirst()
                .orElse(new Theme(null, "A", "b", "http://"));

        if (deleteTheme.getId() != null) {
            if (reservations.stream()
                    .filter(reservation -> reservation.getTheme().equals(deleteTheme))
                    .count() != 0) {
                throw new IllegalStateException();
            }
            int affectedRows = (int) themes.stream()
                    .filter(theme -> Objects.equals(theme.getId(), id))
                    .count();
            themes.remove(deleteTheme);
            return affectedRows;
        }

        return 0;
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }
}
