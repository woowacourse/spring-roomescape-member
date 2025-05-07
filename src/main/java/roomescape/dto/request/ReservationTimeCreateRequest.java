package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

public record ReservationTimeCreateRequest(

        @NotBlank
        @JsonFormat(pattern = "HH:mm")
        String startAt
) {
}
