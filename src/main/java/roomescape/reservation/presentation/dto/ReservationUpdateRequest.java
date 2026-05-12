package roomescape.reservation.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.reservation.application.dto.ReservationUpdateCommand;

public record ReservationUpdateRequest(
        @NotNull(message = "날짜는 비어있을 수 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @NotNull(message = "시간은 비어있을 수 없습니다.")
        Long timeId
) {
    public ReservationUpdateCommand toCommand() {
        return new ReservationUpdateCommand(date, timeId);
    }
}
