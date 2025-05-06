package roomescape.fake;

import roomescape.domain.Reservation;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class FakeThemeDao {

    private static final AtomicLong IDX = new AtomicLong();
    private static final Map<Long, Theme> THEMES = new HashMap<>();

    public Theme findById(Long id) {
        return THEMES.get(id);
    }

    public Theme save(Theme theme) {
        Long id = IDX.getAndIncrement();
        Theme newTheme = new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
        THEMES.put(id, newTheme);
        return newTheme;
    }

    public List<Theme> findAll() {
        return THEMES.values().stream().toList();
    }

    public int deleteById(Long id) {
        if (THEMES.containsKey(id)) {
            THEMES.remove(id);
            return 1;
        }
        return 0;
    }

    public boolean existByName(String name) {
        long count = THEMES.values().stream()
                .filter(theme -> theme.getName().equals(name))
                .count();
        return count != 0;
    }

    public List<Theme> findPopular(List<Reservation> reservations, int count) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today.minusDays(1);

        Map<Theme, Long> reservationCount = reservations.stream()
                .filter(r -> !r.getDate().isBefore(startDate) && !r.getDate().isAfter(endDate))
                .collect(Collectors.groupingBy(Reservation::getTheme, Collectors.counting()));

        return reservationCount.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getValue().intValue()))
                .limit(count)
                .map(Map.Entry::getKey)
                .toList();
    }

    public void clear() {
        THEMES.clear();
        IDX.set(0);
    }
}
