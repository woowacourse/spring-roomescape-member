package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationSearchRequest(

        @NotNull(message = "테마는 필수 항목입니다.")
        Long themeId,

        @NotNull(message = "멤버는 필수 항목입니다.")
        Long memberId,

        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "시간은 필수 항목입니다.")
        LocalDate dateFrom,

        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "시간은 필수 항목입니다.")
        LocalDate dateTo
) {
}
