package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationCreateRequest(

        @NotNull(message = "날짜는 필수이며 공백만 있으면 안 됩니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "시간은 필수 항목입니다.")
        Long timeId,

        @NotNull(message = "테마는 필수 항목입니다.")
        Long themeId
) {
}
