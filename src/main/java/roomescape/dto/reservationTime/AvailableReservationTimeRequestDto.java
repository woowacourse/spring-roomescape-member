package roomescape.dto.reservationTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AvailableReservationTimeRequestDto(
    @NotNull(message = "날짜는 필수 입력값입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date,

    @NotNull(message = "테마 ID는 필수 입력값입니다.")
    Long themeId
) {
}
