package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.domain.ReservationTime;

public record ReservationTimeRequest(
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public ReservationTimeRequest {
        validateNonNull();
    }

    private void validateNonNull() {
        try {
            Objects.requireNonNull(startAt);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("null 값이 될 수 없습니다.");
        }
    }


    public ReservationTime toEntity() {
        return new ReservationTime(startAt);
    }
}
