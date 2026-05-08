package roomescape.domain.time.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record TimeCreateRequestDto(
    @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

}
