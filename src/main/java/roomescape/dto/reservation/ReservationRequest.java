package roomescape.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import roomescape.application.dto.ReservationCreationRequest;
import roomescape.global.validator.ValidLocalDate;

public record ReservationRequest(
        @NotBlank(message = "예약자명은 필수입니다")
        @Size(min = 2, max = 10, message = "이름 길이는 2글자 이상, 10글자 이하여야 합니다.")
        String name,

        @NotNull(message = "예약날짜는 필수입니다.")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "yyyy-MM-dd 형식이어야 합니다.")
        @ValidLocalDate
        String date,

        @NotNull
        Long timeId
) {

    public ReservationCreationRequest toReservationCreationRequest() {
        return new ReservationCreationRequest(name, LocalDate.parse(date), timeId);
    }
}
