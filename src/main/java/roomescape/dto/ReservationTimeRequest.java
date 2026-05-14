package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.ReservationTime;

public record ReservationTimeRequest(
        @JsonFormat(pattern = "HH:mm")
        @NotNull(message = "예약 시간은 필수입니다.")
        LocalTime startAt
) {
    public ReservationTime toEntity() {
        return new ReservationTime(startAt);
    }
}
