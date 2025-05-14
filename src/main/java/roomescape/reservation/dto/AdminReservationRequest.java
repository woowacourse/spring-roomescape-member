package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminReservationRequest(
        @NotNull(message = "날짜는 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @NotNull(message = "시간은 필수입니다.")
        Long timeId,
        @NotNull(message = "테마는 필수입니다.")
        Long themeId,
        @NotNull(message = "예약자는 필수입니다.")
        Long memberId
) {

}
