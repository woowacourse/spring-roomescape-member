package roomescape.domain.time.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record TimeCreateRequestDto(
    @NotNull @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

}
