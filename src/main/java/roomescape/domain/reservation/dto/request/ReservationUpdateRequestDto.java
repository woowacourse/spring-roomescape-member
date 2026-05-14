package roomescape.domain.reservation.dto.request;

import java.time.LocalDate;
import roomescape.domain.reservation.validator.ReservationUpdateRequestValidator;

public record ReservationUpdateRequestDto(LocalDate date, Long timeId) {

    public ReservationUpdateRequestDto {
        ReservationUpdateRequestValidator.validate(date, timeId);
    }
}
