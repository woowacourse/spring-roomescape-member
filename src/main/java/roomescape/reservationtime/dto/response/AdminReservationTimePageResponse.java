package roomescape.reservationtime.dto.response;

import java.time.LocalTime;
import java.util.List;
import roomescape.reservationtime.domain.ReservationTime;

public record AdminReservationTimePageResponse(
        int totalPages,
        List<AdminReservationTimePageElementResponse> reservationTimes
) {

    public record AdminReservationTimePageElementResponse(Long id, LocalTime startAt) {

        public static AdminReservationTimePageElementResponse fromEntity(ReservationTime reservationTime) {
            return new AdminReservationTimePageElementResponse(reservationTime.getId(), reservationTime.getStartAt());
        }
    }
}
