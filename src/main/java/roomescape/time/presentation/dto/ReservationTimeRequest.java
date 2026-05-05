package roomescape.time.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.time.domain.ReservationTime;

public record ReservationTimeRequest(
        @NotNull(message = "시간은 필수입니다.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {
    public static ReservationTime toEntity(ReservationTimeRequest request) {
        return ReservationTime.builder()
                .startAt(request.startAt)
                .build();
    }
}
