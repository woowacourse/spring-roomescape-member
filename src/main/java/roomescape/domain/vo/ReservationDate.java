package roomescape.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.LocalDate;

public record ReservationDate(
        @JsonValue LocalDate value
) {
    @JsonCreator
    public ReservationDate {
    }
}
