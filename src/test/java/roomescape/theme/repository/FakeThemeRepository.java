package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;

public class FakeThemeRepository implements ThemeRepository {
    private final Map<Long, Theme> themes = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1);
    private final ReservationRepository reservationRepository;

    public FakeThemeRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Theme> findAll() {
        return themes.entrySet().stream()
                .map(entry -> {
                    Theme value = entry.getValue();
                    return Theme.of(entry.getKey(), value.getName(), value.getDescription(), value.getThumbnail());
                })
                .toList();
    }

    @Override
    public Theme save(final Theme theme) {
        long id = index.getAndIncrement();
        themes.put(id, theme);
        return Theme.of(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public boolean deleteById(final long id) {
        return themes.remove(id) != null;
    }

    @Override
    public Optional<Theme> findById(final long id) {
        return Optional.ofNullable(themes.get(id));
    }

    @Override
    public List<Theme> findTop10PopularThemesWithinLastWeek(final LocalDate nowDate) {
        LocalDate from = nowDate.minusDays(7);
        LocalDate to = nowDate.minusDays(1);

        Map<Long, Long> themeCount = reservationRepository.findAll().stream()
                .filter(r -> {
                    LocalDate d = r.getDate();
                    return !d.isBefore(from) && !d.isAfter(to);
                })
                .collect(java.util.stream.Collectors.groupingBy(
                        r -> r.getTheme().getId(),
                        java.util.stream.Collectors.counting()
                ));

        return themeCount.entrySet().stream()
                .sorted((a, b) -> {
                    int countCompare = Long.compare(b.getValue(), a.getValue());
                    if (countCompare != 0) {
                        return countCompare;
                    }

                    String nameA = themes.get(a.getKey()).getName();
                    String nameB = themes.get(b.getKey()).getName();
                    return nameA.compareTo(nameB);
                })
                .limit(10)
                .map(entry -> {
                    Theme t = themes.get(entry.getKey());
                    return Theme.of(entry.getKey(), t.getName(), t.getDescription(), t.getThumbnail());
                })
                .toList();
    }
}
