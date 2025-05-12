package roomescape.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeCreateRequest(
        @NotNull(message = "시간은 필수입니다.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

}
