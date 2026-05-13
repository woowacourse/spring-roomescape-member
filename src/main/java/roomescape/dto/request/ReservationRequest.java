package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(

        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @NotNull(message = "예약 날짜는 필수입니다.")
        @FutureOrPresent(message = "이전 날짜에는 예약할 수 없습니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate date,

        long timeId,
        long themeId
) {
}
