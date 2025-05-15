package roomescape.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public record AdminReservationPageResponse(int totalPages, List<AdminReservationPageElementResponse> reservations) {

    public record AdminReservationPageElementResponse(
            Long id,
            AdminReservationPageMemberElementResponse member,
            AdminReservationPageThemeElementResponse theme,
            AdminReservationPageTimeElementResponse time,
            LocalDate date
    ) {

        public static AdminReservationPageElementResponse from(Reservation reservation) {
            return new AdminReservationPageElementResponse(
                    reservation.getId(),
                    new AdminReservationPageMemberElementResponse(
                            reservation.getUser().getId(), reservation.getUser().getName()
                    ),
                    new AdminReservationPageThemeElementResponse(
                            reservation.getTheme().getId(), reservation.getTheme().getName()
                    ),
                    new AdminReservationPageTimeElementResponse(
                            reservation.getReservationTime().getId(), reservation.getReservationTime().getStartAt()
                    ),
                    reservation.getDate()
            );
        }

        public record AdminReservationPageMemberElementResponse(Long id, String name) {
        }

        public record AdminReservationPageThemeElementResponse(Long id, String name) {
        }

        public record AdminReservationPageTimeElementResponse(Long id, LocalTime startAt) {
        }
    }
}
