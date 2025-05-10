package roomescape.domain.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record ReservationSearchFilter(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {

    public boolean isNeeded() {
        return themeId != null && memberId != null && dateFrom != null && dateTo != null;
    }

    public List<Reservation> doFilter(List<Reservation> reservations) {
        if (isNeeded()) {
            throw new IllegalArgumentException("[ERROR] 필터링이 불가능한 요청입니다.");
        }
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getTimeId(), themeId))
                .filter(reservation -> Objects.equals(reservation.getThemeId(), themeId))
                .filter(reservation -> reservation.getDate().isBefore(dateTo))
                .filter(reservation -> reservation.getDate().isAfter(dateFrom))
                .toList();
    }
}
