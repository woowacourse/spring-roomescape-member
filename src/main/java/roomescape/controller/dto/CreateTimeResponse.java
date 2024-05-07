package roomescape.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.Reservation;

public record CreateTimeResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt) {

    public static CreateTimeResponse from(Reservation reservation) {
        return new CreateTimeResponse(reservation.getTime().getId(), reservation.getTime().getStartAt());
    }
}
