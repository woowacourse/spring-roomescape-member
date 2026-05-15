package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.service.command.ReservationUpdateCommand;

public record ReservationUpdateRequest(
        @NotNull(message = "예약 날짜는 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "예약 시간은 필수입니다.")
        Long timeId) {

    public ReservationUpdateCommand toChangeCommand() {
        return new ReservationUpdateCommand(date, timeId);
    }
}
