package roomescape.domain.time.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record TimeCreateRequestDto(
    @NotNull(message = "예약 시간을 입력해주세요.")
    @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

}
