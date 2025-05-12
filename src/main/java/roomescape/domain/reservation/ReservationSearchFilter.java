package roomescape.domain.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record ReservationSearchFilter(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {

    public boolean isNeeded() {
        return themeId != null || memberId != null || (dateFrom != null && dateTo != null);
    }

    public List<Reservation> doFilter(List<Reservation> reservations) {
        return reservations.stream()
                .filter(this::matchesTheme)
                .filter(this::matchesMember)
                .filter(this::matchesDateRange)
                .toList();
    }

    private boolean matchesTheme(Reservation reservation) {
        return themeId == null || Objects.equals(reservation.getThemeId(), themeId);
    }

    private boolean matchesMember(Reservation reservation) {
        return memberId == null || Objects.equals(reservation.getMemberId(), memberId);
    }

    private boolean matchesDateRange(Reservation reservation) {
        if (dateFrom == null || dateTo == null) {
            return true;
        }
        LocalDate date = reservation.getDate();
        return !date.isBefore(dateFrom) && !date.isAfter(dateTo);
    }
}
