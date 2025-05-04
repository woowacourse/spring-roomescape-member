package roomescape.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record CreateReservationTimeRequest(
        @NotNull(message = "예약 시간은 필수입니다")
        @JsonFormat(pattern = "hh:mm")
        LocalTime startAt
) {
}
