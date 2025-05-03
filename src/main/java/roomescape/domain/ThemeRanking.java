package roomescape.domain;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemeRanking {

    private static final int RANKING_SIZE = 10;

    private final Map<Theme, Integer> reservationCountByTheme;

    public ThemeRanking(List<Reservation> reservations) {
        Map<Theme, Integer> reservationCountByTheme = new HashMap<>();
        for (Reservation reservation : reservations) {
            Theme theme = reservation.getTheme();
            reservationCountByTheme.put(theme, reservationCountByTheme.getOrDefault(theme, 0) + 1);
        }
        this.reservationCountByTheme = reservationCountByTheme;
    }

    public List<Theme> getAscendingRanking() {
        return reservationCountByTheme.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(RANKING_SIZE)
                .toList();
    }
}
