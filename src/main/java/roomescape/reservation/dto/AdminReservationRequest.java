package roomescape.reservation.dto;

import java.time.LocalDate;

public record AdminReservationRequest(LocalDate date,
                                      Long themeId,
                                      Long timeId,
                                      Long memberId) {

    public ReservationCommand toCommand() {
        return new ReservationCommand(memberId, date, themeId, timeId);
    }
}
