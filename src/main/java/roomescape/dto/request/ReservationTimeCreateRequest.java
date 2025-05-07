package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeCreateRequest(

        @NotNull(message = "시간은 필수이며 공백만 있으면 안 됩니다.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {
}
