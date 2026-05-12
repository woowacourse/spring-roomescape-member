package roomescape.dto;

import java.time.LocalDate;
import roomescape.exception.InvalidInputException;

public record UpdateReservationRequest(LocalDate date, Long timeId) {
    public UpdateReservationRequest {
        if (date == null) {
            throw new InvalidInputException("변경할 날짜는 필수입니다.");
        }
        if (timeId == null || timeId <= 0) {
            throw new InvalidInputException("변경할 시간 ID는 필수입니다.");
        }
    }
}
