package roomescape.fake;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import roomescape.domain.PopularThemeRepository;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.ThemeSortType;

public class FakePopularThemeRepository implements PopularThemeRepository {

    private final List<Reservation> reservations;
    private final List<Theme> themes;

    public FakePopularThemeRepository(
            List<Reservation> reservations,
            List<Theme> themes
    ) {
        this.reservations = reservations;
        this.themes = themes;
    }

    @Override
    public List<Theme> findTopNByPeriod(
            LocalDate startAt,
            LocalDate endAt,
            ThemeSortType sortType,
            Long limit
    ) {
        if (sortType != ThemeSortType.POPULAR) {
            throw new IllegalArgumentException("지원하지 않는 정렬 방식입니다.");
        }

        Map<Long, Long> reservationCountByTheme = reservations.stream()
                .filter(reservation ->
                        !reservation.getDate().isBefore(startAt)
                                && !reservation.getDate().isAfter(endAt)
                )
                .collect(Collectors.groupingBy(
                        reservation -> reservation.getTheme().getId(),
                        Collectors.counting()
                ));

        return themes.stream()
                .sorted(Comparator.comparingLong(
                        (Theme theme) -> reservationCountByTheme.getOrDefault(theme.getId(), 0L)
                ).reversed())
                .limit(limit)
                .toList();
    }
}
