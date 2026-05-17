package roomescape.domain.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationCreateRequestDto(@NotBlank(message = "예약자 이름을 입력해주세요.") String name,
                                          @NotNull(message = "예약 날짜를 입력해주세요.")
                                          @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                          @NotNull(message = "예약 시간을 선택해주세요.")
                                          @Min(value = 1, message = "예약 시간 id는 1 이상이어야 합니다.")
                                          Long timeId,
                                          @NotNull(message = "테마를 선택해주세요.")
                                          @Min(value = 1, message = "테마 id는 1 이상이어야 합니다.")
                                          Long themeId) {

}
