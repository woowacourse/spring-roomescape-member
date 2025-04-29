package roomescape.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record TimeResponse(

        long id,

        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {
}
