package roomescape.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.controller.exception.InvalidRequestException;
import roomescape.service.dto.ReservationCreateCommand;

public record ReservationRequest(
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        Long timeId,
        Long themeId
) {
    public ReservationRequest {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException("예약자 이름은 필수입니다");
        }
        if (date == null) {
            throw new InvalidRequestException("예약 날짜는 필수입니다");
        }
        if (timeId == null || timeId <= 0) {
            throw new InvalidRequestException("시간 ID는 양수여야 합니다");
        }
        if (themeId == null || themeId <= 0) {
            throw new InvalidRequestException("테마 ID는 양수여야 합니다");
        }
    }

    public ReservationCreateCommand toCommand() {
        return new ReservationCreateCommand(name, date, timeId, themeId);
    }
}
