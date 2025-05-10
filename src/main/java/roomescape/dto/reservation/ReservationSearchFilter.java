package roomescape.dto.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import roomescape.entity.reservation.Reservation;

public record ReservationSearchFilter(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {

    public boolean isNeeded() {
        return themeId != null && memberId != null && dateFrom != null && dateTo != null;
    }

    public List<Reservation> doFilter(List<Reservation> reservations) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getTimeId(), themeId))
                .filter(reservation -> Objects.equals(reservation.getThemeId(), themeId))
                .filter(reservation -> reservation.getDate().isBefore(dateTo))
                .filter(reservation -> reservation.getDate().isAfter(dateFrom))
                .toList();
    }
}
