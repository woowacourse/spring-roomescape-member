package roomescape.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.LocalDate;

public record ReservationDate(
        LocalDate value
) {
    public static ReservationDate from(LocalDate date) {
        return new ReservationDate(date);
    }
}
