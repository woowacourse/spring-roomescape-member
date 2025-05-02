package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import roomescape.reservation.entity.Reservation;
import roomescape.theme.entity.Theme;

public class FakeThemeRepository implements ThemeRepository {

    private final List<Theme> themes = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public Theme save(Theme theme) {
        themes.add(theme);
        return theme;
    }

    @Override
    public List<Theme> findAll() {
        return Collections.unmodifiableList(themes);
    }

    @Override
    public boolean deleteById(Long id) {
        return themes.removeIf(entity -> entity.getId().equals(id));
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return themes.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Theme> findByName(String name) {
        return themes.stream()
                .filter(entity -> entity.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<Theme> findPopularDescendingUpTo(LocalDate startDate, LocalDate endDate, final int limit) {
        Map<Long, Long> reservationCountByTheme = reservations.stream()
                .filter(r -> !r.getDate().isBefore(startDate) && !r.getDate().isAfter(endDate))
                .collect(Collectors.groupingBy(
                        Reservation::getThemeId,
                        Collectors.counting()
                ));

        return themes.stream()
                .sorted((a, b) -> {
                    long countA = reservationCountByTheme.getOrDefault(a.getId(), 0L);
                    long countB = reservationCountByTheme.getOrDefault(b.getId(), 0L);
                    if (countA != countB) {
                        return Long.compare(countB, countA);
                    }
                    return Long.compare(b.getId(), a.getId());
                })
                .limit(limit)
                .toList();
    }
}
