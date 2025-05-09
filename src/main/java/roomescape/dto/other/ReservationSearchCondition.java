package roomescape.dto.other;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationSearchCondition(
        @NotNull(message = "비어있는 테마ID는 허용하지 않습니다.")
        Long themeId,
        @NotNull(message = "비어있는 회원ID는 허용하지 않습니다.")
        Long memberId,
        @NotNull(message = "비어있는 시작날짜는 허용하지 않습니다.")
        LocalDate dateFrom,
        @NotNull(message = "비어있는 종료날짜는 허용하지 않습니다.")
        LocalDate dateTo
) {

}
