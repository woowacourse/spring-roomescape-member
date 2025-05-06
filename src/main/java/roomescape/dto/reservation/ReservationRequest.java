package roomescape.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank(message = "예약자 이름은 필수 입력값입니다.") String name,

        @NotNull(message = "예약 날짜는 필수 입력값입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate date,

        @NotNull(message = "예약 시간은 필수 입력값입니다.") Long timeId,

        @NotNull(message = "테마는 필수 입력값입니다.") Long themeId
) {
}
