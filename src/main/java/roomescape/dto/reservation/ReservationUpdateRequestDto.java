package roomescape.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationDate;

import java.time.LocalDate;

public record ReservationUpdateRequestDto(
        @NotNull(message = "예약 ID는 필수 입력값입니다.")
        Long id,

        @NotNull(message = "이름은 필수 입력값입니다.")
        MemberName name,

        @NotNull(message = "날짜는 필수 입력값입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        ReservationDate date,

        @NotNull(message = "시간 ID는 필수 입력값입니다.")
        Long timeId,

        @NotNull(message = "테마 ID는 필수 입력값입니다.")
        Long themeId
) {
}
