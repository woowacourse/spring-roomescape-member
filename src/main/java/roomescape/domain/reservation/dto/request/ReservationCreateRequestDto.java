package roomescape.domain.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.domain.reservation.validator.ReservationValidator;

public record ReservationCreateRequestDto(String name,
                                          @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                          Long timeId, Long themeId) {

    public ReservationCreateRequestDto {
        ReservationValidator.validate(name);
    }
}
