package roomescape.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AdminReservationPageResponse(int totalPages, List<AdminReservationPageElementResponse> reservations) {

    public record AdminReservationPageElementResponse(
            Long id,
            AdminReservationPageMemberElementResponse member,
            AdminReservationPageThemeElementResponse theme,
            AdminReservationPageTimeElementResponse time,
            LocalDate date
    ) {

        public record AdminReservationPageMemberElementResponse(Long id, String name) {
        }

        public record AdminReservationPageThemeElementResponse(Long id, String name) {
        }

        public record AdminReservationPageTimeElementResponse(Long id, LocalTime startAt) {
        }
    }
}
