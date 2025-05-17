package roomescape.time.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import roomescape.time.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeRequest(
        @NotBlank @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul") LocalTime startAt
) {

    public ReservationTimeRequest {
        validateStartAt(startAt);
    }

    public ReservationTime toTimeWithoutId() {
        return new ReservationTime(null, startAt);
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시작 시각은 필수입니다.");
        }
    }

}
