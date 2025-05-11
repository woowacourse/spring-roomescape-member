package roomescape.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ReservationsWithTotalPageResponse(int totalPages, List<BriefReservationElement> reservations) {

    public record BriefReservationElement(
            Long id,
            BriefMemberElement member,
            BriefThemeElement theme,
            BriefTimeElement time,
            LocalDate date
    ) {

        public record BriefMemberElement(Long id, String name) {
        }

        public record BriefThemeElement(Long id, String name) {
        }

        public record BriefTimeElement(Long id, LocalTime startAt) {
        }
    }
}
