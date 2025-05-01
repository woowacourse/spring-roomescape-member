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
    private final List<Reservation> reservations;

    public FakeThemeRepository(final List<Theme> themes) {
        this.themes = themes;
        reservations = new ArrayList<>();
    }

    @Override
    public Optional<Theme> save(Theme theme) {
        long count = themes.stream()
                .filter(t -> t.name().equals(theme.name()) && t.description().equals(theme.description()) && t.thumbnail().equals(theme.thumbnail()))
                .count();
        if (count != 0) {
            throw new IllegalStateException("Reservation time already exists");
        }

        Theme newTheme = new Theme(themeId.getAndIncrement(), theme.name(), theme.description(), theme.thumbnail());
        themes.add(newTheme);
        return findById(newTheme.id());
    }

    @Override
    public List<Theme> findAll() {
        return themes;
    }

    @Override
    public Optional<Theme> findById(long id) {
        return themes.stream()
                .filter(theme -> Objects.equals(theme.id(), id))
                .findFirst();
    }

    @Override
    public List<Theme> findPopular(LocalDate start, LocalDate end) {
        Map<Theme, Long> themeCounts = reservations.stream()
                .filter(r -> !r.date().isBefore(start) && r.date().isBefore(end))
                .collect(Collectors.groupingBy(Reservation::theme, Collectors.counting()));

        return themeCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();

//        List<Reservation> reservations1 = reservations.stream()
//                .filter(reservation -> reservation.date().isBefore(end) && reservation.date().isAfter(start))
//                .toList();
//
//        for (Reservation reservation : reservations1) {
//            counts.computeIfPresent(reservation.theme().id(), (k, v) -> v + 1);
//            counts.putIfAbsent(reservation.theme().id(), 1);
//        }
//
////        counts.entrySet().stream().sorted(Comparator.comparing((entry1) -> ));
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

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }
}
