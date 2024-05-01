package roomescape.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import roomescape.application.dto.ReservationCreationRequest;

public record ReservationRequest(
        @NotBlank(message = "예약자명은 필수입니다")
        @Size(min = 2, max = 10, message = "이름 길이는 2글자 이상, 10글자 이하여야 합니다.")
        String name,

        @NotNull(message = "예약날짜는 필수입니다.")
        LocalDate date,

        @NotNull(message = "시간 아이디는 필수입니다.")
        Long timeId
) {

    public ReservationCreationRequest toReservationCreationRequest() {
        return new ReservationCreationRequest(name, date, timeId);
    }
}
