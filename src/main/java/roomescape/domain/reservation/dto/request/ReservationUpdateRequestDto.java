package roomescape.domain.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationUpdateRequestDto(@NotNull(message = "변경할 예약 날짜를 입력해주세요.")
                                          @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                          @NotNull(message = "변경할 예약 시간을 선택해주세요.") Long timeId) {

}
