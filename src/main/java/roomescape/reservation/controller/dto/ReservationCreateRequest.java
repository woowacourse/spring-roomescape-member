package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationCreateRequest(

        @NotBlank(message = "이름은 비어 있을 수 없습니다.")
        String name,

        @NotNull(message = "예약 날짜는 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "시간 지정은 필수입니다.")
        Long timeId
) {
}
