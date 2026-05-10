package roomescape.domain.reservation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import org.hibernate.validator.constraints.Length;

public record ReservationCreateRequest(
        @NotBlank(message = "username은 공백일 수 없습니다.")
        @Length(min = 2, max = 5, message = "이름은 2~5글자 문자열만 가능합니다.")
        String username,

        @NotNull(message = "themeId는 null일 수 없습니다.")
        @Positive(message = "themeId는 양수만 가능합니다.")
        Long themeId,

        @NotNull(message = "date은 null일 수 없습니다.")
        LocalDate date,

        @NotNull(message = "timeId는 null일 수 없습니다.")
        @Positive(message = "timeId는 양수만 가능합니다.")
        Long timeId
) {
}
