package roomescape.controller.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReservationCreateRequest(
        @NotBlank(message = "RESERVATION_NAME_REQUIRED::예약자 이름은 비어 있을 수 없습니다.")
        @Size(max = 9, message = "RESERVATION_NAME_TOO_LONG::예약자 이름은 10자 미만이어야 합니다.")
        String name,

        @NotNull(message = "RESERVATION_DATE_REQUIRED::날짜는 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "THEME_ID_REQUIRED::themeId는 필수입니다.")
        Long themeId,

        @NotNull(message = "RESERVATION_TIME_ID_REQUIRED::timeId는 필수입니다.")
        Long timeId
) {
}
