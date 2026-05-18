package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.service.command.ReservationSaveCommand;

import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank(message = "예약자 이름은 비어 있을 수 없습니다.")
        String name,

        @NotNull(message = "예약 날짜는 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "예약 시간은 필수입니다.")
        Long timeId,

        @NotNull(message = "테마는 필수입니다.")
        Long themeId
) {

    public ReservationSaveCommand toSaveCommand() {
        return new ReservationSaveCommand(name, date, timeId, themeId);
    }
}
