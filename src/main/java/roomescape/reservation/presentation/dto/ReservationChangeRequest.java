package roomescape.reservation.presentation.dto;

import java.time.LocalDate;
import roomescape.reservation.application.dto.ReservationChangeCommand;

public record ReservationChangeRequest(
        String username,
        Long timeId,
        Long themeId,
        LocalDate date
) {
    public ReservationChangeCommand toCommand() {
        return ReservationChangeCommand.builder()
                .username(username)
                .timeId(timeId)
                .themeId(themeId)
                .date(date)
                .build();
    }
}
