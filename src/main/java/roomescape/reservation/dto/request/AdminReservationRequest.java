package roomescape.reservation.dto.request;

import java.time.LocalDate;

public record AdminReservationRequest (LocalDate date, Long timeId, Long themeId, Long memberId) {

    public ReservationRequest toReservationRequest() {
        return new ReservationRequest(date, timeId, themeId);
    }
}
